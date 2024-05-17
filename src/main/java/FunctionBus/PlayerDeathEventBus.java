package FunctionBus;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import DataBus.PlayerDataBus;

public class PlayerDeathEventBus {
    public static void onBusTrigger(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        PlayerDataBus.removePlayerItemDisplay(player);
    }
}
