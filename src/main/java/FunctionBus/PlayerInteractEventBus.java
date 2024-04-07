package FunctionBus;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import Assert.Item.Sword;
import Assert.Item.Gun.Rifle;
import DataBus.PlayerDataBus;
import Task.GunTask.RifleTask;

public class PlayerInteractEventBus {
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

        if (action == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (!player.isSneaking() && event.getClickedBlock().getType().isInteractable())
                return false;
        }

        if (!Sword._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;
        return true;
    }

    public static void onPlayerBeginDefense(PlayerInteractEvent event) {
        PlayerDataBus.playerStartDefense(event.getPlayer());
    }
}
