package FunctionBus;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import DataBus.PlayerDataBus;


public class PlayerMoveEventBus {
    public static void onBusTrigger(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemDisplay display = (ItemDisplay) PlayerDataBus.getPlayerItemDisplay(player);
        display.setTeleportDuration(1);

        Location to = event.getTo();
        display.setRotation(to.getYaw(), 0);
    }

    public static void onBusComplete(PlayerMoveEvent event) {
        
    }
}
