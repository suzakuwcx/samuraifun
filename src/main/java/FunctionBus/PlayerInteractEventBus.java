package FunctionBus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;

import Assert.Item.BattleFlag;
import Assert.Item.SmokingDarts;
import Assert.Item.Sword;
import Assert.Item.Taijutsu;
import Assert.Item.Gun.Rifle;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import Task.AttackTask.BattleFlagTask;
import Task.AttackTask.SmokingDartsTask;
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
            if (!player.isSneaking() && !event.getClickedBlock().getType().isInteractable())
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

    public static boolean isPlayerUsingBattleFlag(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (event.getHand() != EquipmentSlot.HAND)
            return false;

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (!BattleFlag._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;

        return true;
    }


    public static void onPlayerUsingBattleFlag(PlayerInteractEvent event) {
        BattleFlagTask task = new BattleFlagTask(event.getPlayer().getLocation(), 400);
        Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
    }

    
    public static boolean isPlayerUsingSmokingDartsEntity(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (event.getHand() != EquipmentSlot.HAND)
            return false;

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (!SmokingDarts._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;

        return true;
    }


    public static void onPlayerUsingSmokingDartsEntity(PlayerInteractEvent event) {
        SmokingDartsTask task = new SmokingDartsTask(event.getPlayer().getEyeLocation(), 400);
        Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
    }
}
