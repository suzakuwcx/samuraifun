package FunctionBus;

import org.bukkit.event.player.PlayerJoinEvent;

import DataBus.PlayerDataBus;

public class PlayerJoinEventBus {
    public static void onBusTrigger(PlayerJoinEvent event) {
        PlayerDataBus.addPlayerItemDisplay(event.getPlayer());
        event.getPlayer().setShieldBlockingDelay(4);
    }

    public static void onBusComplete(PlayerJoinEvent event) {
        
    }

    public static boolean isPlayerOperator(PlayerJoinEvent event) {
        if (!event.getPlayer().isOp())
            return false;
        
        return true;
    }

    public static void onPlayerOperator(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Helloworld");
    }

 
}
