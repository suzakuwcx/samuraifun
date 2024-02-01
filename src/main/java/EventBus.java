import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import FunctionBus.PlayerInteractEventBus;
import FunctionBus.PlayerJoinEventBus;
import FunctionBus.PlayerStopUsingItemEventBus;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;

public class EventBus implements Listener {    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (PlayerJoinEventBus.isPlayerOperator(event)) {
            PlayerJoinEventBus.onPlayerOperator(event);
        }
    }


    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (PlayerInteractEventBus.isPlayerBeginUsingRifle(event)) {
            PlayerInteractEventBus.onPlayerBeginUsingRifle(event);
        }
    }


    @EventHandler
    public void onPlayerStopUsingItemEvent(PlayerStopUsingItemEvent event) {
        if (PlayerStopUsingItemEventBus.isPlayerStopUsingRifle(event)) {
            PlayerStopUsingItemEventBus.onPlayerStopUsingRifle(event);
        }
    }
}