package FunctionBus;

import Assert.Item.Sword;
import Assert.Item.Gun.Rifle;
import DataBus.PlayerDataBus;
import Task.GunTask.RifleTask;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;

public class PlayerStopUsingItemEventBus {
    public static boolean isPlayerStopUsingRifle(PlayerStopUsingItemEvent event) {
        if (!Rifle._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;
        return true;
    }

    public static void onPlayerStopUsingRifle(PlayerStopUsingItemEvent event) {
        RifleTask.stop(event.getPlayer());
    }

    public static boolean isPlayerStopDefense(PlayerStopUsingItemEvent event) {
        if (!Sword._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;
        return true;
    }

    public static void onPlayerStopDefense(PlayerStopUsingItemEvent event) {
        // PlayerDataBus.playerStopDefense(event.getPlayer());
    }
}
