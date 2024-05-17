package FunctionBus;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

import DataBus.PlayerDataBus;

public class PlayerRespawnEventBus {
    public static void onBusTrigger(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerDataBus.addPlayerItemDisplay(player);
    }
}
