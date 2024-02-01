package FunctionBus;

import org.bukkit.Material;

import Task.GunTask.RifleTask;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;

public class PlayerStopUsingItemEventBus {
    public static boolean isPlayerStopUsingRifle(PlayerStopUsingItemEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.CROSSBOW)
            return false;
        return true;
    }

    public static void onPlayerStopUsingRifle(PlayerStopUsingItemEvent event) {
        RifleTask.stop(event.getPlayer());
    }
}
