package FunctionBus;

import org.bukkit.GameMode;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEventBus {
    public static boolean isPlayerInAdventure(InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode() != GameMode.ADVENTURE)
            return false;

        return true;
    }

    public static void onPlayerInAdventure(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
