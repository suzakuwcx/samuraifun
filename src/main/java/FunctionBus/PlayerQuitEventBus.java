package FunctionBus;

import org.bukkit.event.player.PlayerQuitEvent;

import DataBus.PlayerDataBus;

public class PlayerQuitEventBus {
    public static void onBusTrigger(PlayerQuitEvent event) {
        PlayerDataBus.removePlayerItemDisplay(event.getPlayer());
    }

    public static void onBusComplete(PlayerQuitEvent event) {
        
    }
}
