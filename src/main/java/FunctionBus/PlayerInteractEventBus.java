package FunctionBus;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;

import Assert.Item.Sword;
import Assert.Item.Taijutsu;
import Assert.Item.Gun.Rifle;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import Task.GunTask.RifleTask;

public class PlayerInteractEventBus {
    public static boolean isTriggeredByDropItemEvent(PlayerInteractEvent event) {
        if (!PlayerDataBus.upPlayerDropItem(event.getPlayer()))
            return false;

        return true;
    }

    public static void onTriggeredByDropItemEvent(PlayerInteractEvent event) {
        
    }

    public static boolean isTargetBlockInteractAble(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (action == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (!player.isSneaking() && event.getClickedBlock().getType().isInteractable())
                return false;
        }

        return true;
    }

    public static void onTargetBlockInteractAble(PlayerInteractEvent event) {
        return;
    }

    public static boolean isPlayerBeginUsingRifle(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;
        if (!Rifle._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;
        return true;
    }

    public static void onPlayerBeginUsingRifle(PlayerInteractEvent event) {
        RifleTask.shoot(event.getPlayer());
    }

    public static boolean isPlayerSlash(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK)
            return false;
        if (!Sword._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;
        return true;
    }

    public static void onPlayerSlash(PlayerInteractEvent event) {
        PlayerSlashBus.onPlayerSlash(event.getPlayer());
    }

    public static boolean isPlayerBeginDefense(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (!Sword._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;
        return true;
    }

    public static void onPlayerBeginDefense(PlayerInteractEvent event) {
        PlayerStateMachineSchedule.player_defense_map.put(event.getPlayer().getUniqueId(), true);
    }

    public static boolean isPlayerBeginChargedBlow(PlayerInteractEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        Action action = event.getAction();

        if (event.getHand() != EquipmentSlot.HAND)
            return false;

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (!Taijutsu._instanceof(inventory.getItemInMainHand()))
            return false;

        if (!Sword._instanceof(inventory.getItemInOffHand()))
            return false;

        if (event.getPlayer().getCooldown(inventory.getItemInMainHand().getType()) > 0)
            return false;
        
        return true;
    }

    public static void onPlayerBeginChargedBlow(PlayerInteractEvent event) {
        event.setCancelled(true);
    }
}
