package FunctionBus;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import Assert.Item.Sword;
import Assert.Item.Gun.Rifle;
import Task.GunTask.RifleTask;
import Task.SwipeTask.KatateHidariKiriageTask;

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

    public static boolean isPlayerAttack(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK)
            return false;
        if (!Sword._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;
        return true;
    }

    public static void onPlayerAttack(PlayerInteractEvent event) {
        KatateHidariKiriageTask.execute(event.getPlayer());
    }
}
