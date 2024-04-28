package FunctionBus;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerMoveEvent;

import DataBus.PlayerDataBus;


public class PlayerMoveEventBus {
    public static void onBusTrigger(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        for (TextDisplay display : PlayerDataBus.getPlayerItemDisplay(player))
            display.setRotation(to.getYaw(), 0);
    }

    public static void onBusComplete(PlayerMoveEvent event) {
        
    }
}
