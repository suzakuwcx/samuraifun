package FunctionBus;

import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerItemHeldEventBus {
    public static boolean isPlayerInAdventure(PlayerItemHeldEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return false;

        return true;
    }

    public static void onPlayerInAdventure(PlayerItemHeldEvent event) {
        event.setCancelled(true);
    }
}
