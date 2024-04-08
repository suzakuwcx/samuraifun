package FunctionBus;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import Assert.Item.Sword;
import DataBus.PlayerDataBus;
import Task.AttackTask.DeflectTask;
import Task.DelayTask.SetNoDamageTicksTask;

public class EntityDamageByEntityEventBus {
    public static boolean isPlayerSlash(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER)
            return false;

        Player player = (Player) event.getDamager();
        if (!Sword._instanceof(player.getInventory().getItemInMainHand()))
            return false;

        if (PlayerDataBus.upPlayerSlash(player))
            return false;
        
        return true;
    }

    public static void onPlayerSlash(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
        PlayerSlashBus.onPlayerSlash((Player) event.getDamager());
    }

    public static boolean isPlayerDefense(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER)
            return false;

        if (event.getDamage(DamageModifier.BLOCKING) >= - 0.01)
            return false;

        return true;
    }

    public static void onPlayerDefense(EntityDamageByEntityEvent event) {
    }

    public static boolean isPlayerDeflect(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER)
            return false;

        Player player = (Player) event.getEntity();
        if (event.getDamage(DamageModifier.BLOCKING) < - 0.01)
            return false;

        if (!DeflectTask.isPlayerDefense(player))
            return false;

        return true;
    }

    public static void onPlayerDeflect(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getEntity();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1f, 2f);
    }

    public static boolean isPlayerAttackZombie(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.ZOMBIE)
            return false;

        if (event.getDamager().getType() != EntityType.PLAYER)
            return false;
    
        return true;
    }

    public static void onPlayerAttackZombie(EntityDamageByEntityEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();
        SetNoDamageTicksTask.execute(entity);
    }
}
