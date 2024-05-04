package FunctionBus;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import Assert.Item.Sword;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;
import Task.AttackTask.DeflectTask;

public class EntityDamageByEntityEventBus {
    public static void onBusTrigger(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    public static void onBusComplete(EntityDamageByEntityEvent event) {
        if (isPlayerAttackNoInvincibleFrameEntity(event))
            onPlayerAttackNoInvincibleFrameEntity(event);
    }

    public static boolean isPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER)
            return false;
        
        return true;
    }

    public static void onPlayerAttack(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        
        if (PlayerStateMachineSchedule.getStateTask(player).isStateEvent(event)) {
            PlayerStateMachineSchedule.getStateTask(player).onEntityDamageByEntityEvent(event);
        }
    }

    public static boolean isPlayerSlash(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER)
            return false;

        Player player = (Player) event.getDamager();
        if (!Sword._instanceof(player.getInventory().getItemInMainHand()))
            return false;
        
        return true;
    }

    public static void onPlayerSlash(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        if (PlayerStateMachineSchedule.getStateTask(player).isStateEvent(event)) {
            PlayerStateMachineSchedule.getStateTask(player).onEntityDamageByEntityEvent(event);
        }
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

    public static boolean isPlayerAttackNoInvincibleFrameEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.ZOMBIE && event.getEntityType() != EntityType.PLAYER)
            return false;

        if (event.getDamager().getType() != EntityType.PLAYER)
            return false;
    
        return true;
    }

    public static void onPlayerAttackNoInvincibleFrameEntity(EntityDamageByEntityEvent event) {
        DelayTask.execute((args) -> {
            LivingEntity entity = (LivingEntity) args[0];
            entity.setNoDamageTicks(0);
        }, 1, event.getEntity());
    }
}
