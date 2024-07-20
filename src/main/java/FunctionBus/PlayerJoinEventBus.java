package FunctionBus;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import Schedule.PlayerUISchedule;

public class PlayerJoinEventBus {
    public static void onBusTrigger(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerBus.resetPlayerState(player);
        PlayerUISchedule.init(player);
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
