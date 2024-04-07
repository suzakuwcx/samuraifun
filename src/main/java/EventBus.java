import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import FunctionBus.EntityDamageByEntityEventBus;
import FunctionBus.PlayerInteractEventBus;
import FunctionBus.PlayerJoinEventBus;
import FunctionBus.PlayerMoveEventBus;
import FunctionBus.PlayerQuitEventBus;
import FunctionBus.PlayerStopUsingItemEventBus;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;

public class EventBus implements Listener {    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        PlayerJoinEventBus.onBusTrigger(event);

        if (PlayerJoinEventBus.isPlayerOperator(event)) {
            PlayerJoinEventBus.onPlayerOperator(event);
        }

        PlayerJoinEventBus.onBusComplete(event);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        PlayerQuitEventBus.onBusTrigger(event);
        PlayerQuitEventBus.onBusComplete(event);
    }


    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (PlayerInteractEventBus.isPlayerBeginUsingRifle(event)) {
            PlayerInteractEventBus.onPlayerBeginUsingRifle(event);
        } else if (PlayerInteractEventBus.isPlayerSlash(event)) {
            PlayerInteractEventBus.onPlayerSlash(event);
        } else if (PlayerInteractEventBus.isPlayerBeginDefense(event)) {
            PlayerInteractEventBus.onPlayerBeginDefense(event);
        }
    }


    @EventHandler
    public void onPlayerStopUsingItemEvent(PlayerStopUsingItemEvent event) {
        if (PlayerStopUsingItemEventBus.isPlayerStopUsingRifle(event)) {
            PlayerStopUsingItemEventBus.onPlayerStopUsingRifle(event);
        } else if (PlayerStopUsingItemEventBus.isPlayerStopDefense(event)) {
            PlayerStopUsingItemEventBus.onPlayerStopDefense(event);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        PlayerMoveEventBus.onBusTrigger(event);
        PlayerMoveEventBus.onBusComplete(event);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (EntityDamageByEntityEventBus.isPlayerSlash(event)) {
            EntityDamageByEntityEventBus.onPlayerSlash(event);
        }
    }
}