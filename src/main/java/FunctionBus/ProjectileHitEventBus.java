package FunctionBus;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import Schedule.PlayerStateMachineSchedule;
import Task.AttackTask.DeflectTask;
import Task.StateTask.BaseStateTask;
import Task.StateTask.ChargedAttackAnimStateTask;
import Task.StateTask.ChargingAttackStateTask;
import Task.StateTask.PlayerStunTask;

public class ProjectileHitEventBus {
    public static boolean isArrowHittingPlayer(ProjectileHitEvent event) {
        Entity entity = event.getHitEntity();
        if (entity == null)
            return false;

        if (!(entity instanceof Player))
            return false;

        if (event.getEntity().getType() != EntityType.ARROW)
            return false;

        Arrow arrow = (Arrow) event.getEntity();

        if (!(arrow.getShooter() instanceof Player))
            return false;

        return true;
    }

    private static void onPlayerDefense(ProjectileHitEvent event) {
        Player player = (Player) event.getHitEntity();
        Vector direction = event.getEntity().getVelocity().setY(0).normalize();
        player.setVelocity(direction.multiply(1));
        ServerBus.playServerSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 0.5f, 0.8f);
        ServerBus.playServerSound(player.getLocation(), Sound.BLOCK_BELL_USE, 1f, 2f);
        ServerBus.playServerSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.2f, 2f);
    }

    private static void onDamageTarget(ProjectileHitEvent event) {
        Player player = (Player) event.getHitEntity();
        Vector direction = event.getEntity().getVelocity().setY(0).normalize();
        player.setVelocity(direction.multiply(2));
        BaseStateTask task = PlayerStateMachineSchedule.getStateTask(player);

        ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 1f);
        ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_HIT, 1f, 1f);

        PlayerStateMachineSchedule.damagePosture(player, 1);

        if (task instanceof ChargedAttackAnimStateTask)
            return;
        
        if (task instanceof ChargingAttackStateTask)
            return;

        if (!PlayerStateMachineSchedule.isPlayerNoPosture(player))
            PlayerStateMachineSchedule.setStateTask(player, new PlayerStunTask(player));
    }

    public static void onArrowHittingPlayer(ProjectileHitEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getHitEntity();
        PlayerStateMachineSchedule.resetSwordCooldown(player);

        if (DeflectTask.isPlayerDeflect(player) || DeflectTask.isPlayerFakeDeflect(player) || DeflectTask.isPlayerDefense(player) || player.isBlocking()) {
            onPlayerDefense(event);
        } else {
            onDamageTarget(event);
        }
    }

    public static void onBusComplete(ProjectileHitEvent event) {
        Arrow arrow = (Arrow) event.getEntity();
        if (!(arrow.getShooter() instanceof Player))
            return;

        event.getEntity().remove();
        Player player = (Player) arrow.getShooter();
        PlayerStateMachineSchedule.resetBowCooldown(player);
        player.getInventory().removeItemAnySlot(new ItemStack(Material.ARROW));
        player.getInventory().setHeldItemSlot(0);
    }
}
