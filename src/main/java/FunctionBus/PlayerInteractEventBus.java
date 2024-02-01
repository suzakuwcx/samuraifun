package FunctionBus;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import Assert.Item.Gun.Rifle;
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
}
