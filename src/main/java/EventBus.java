import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import FunctionBus.PlayerJoinEventBus;

public class EventBus implements Listener {    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (PlayerJoinEventBus.isPlayerOperator(event)) {
            PlayerJoinEventBus.onPlayerOperator(event);
        }
    }
}