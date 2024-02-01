package FunctionBus;

import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventBus {
    public static boolean isPlayerOperator(PlayerJoinEvent event) {
        if (!event.getPlayer().isOp())
            return false;
        
        return true;
    }

    public static void onPlayerOperator(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Helloworld");
    }
}
