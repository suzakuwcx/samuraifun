package FunctionBus;

import org.bukkit.event.player.PlayerDropItemEvent;

import DataBus.PlayerDataBus;

public class PlayerDropItemEventBus {
    public static void onBusTrigger(PlayerDropItemEvent event) {
        event.setCancelled(true);

        PlayerDataBus.downPlayerDropItem(event.getPlayer());
    }
}
