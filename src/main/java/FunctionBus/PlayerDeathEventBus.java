package FunctionBus;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import DataBus.PlayerDataBus;
import net.kyori.adventure.text.Component;

public class PlayerDeathEventBus {
    public static void onBusTrigger(PlayerDeathEvent event) {
        PlayerDataBus.removePlayerItemDisplay(event.getPlayer());
    }

    public static boolean isPlayerDeadByPlugin(PlayerDeathEvent event) {
        if (PlayerDataBus.isPlayerDeadByPlugin(event.getPlayer()))
            return true;
        
        return false;
    }

    public static void onPlayerDeadByPlugin(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        event.deathMessage(Component.text(String.format("%s 击杀了 %s", PlayerDataBus.upPlayerDeadByPlugin(player).getName(), player.getName())));
    }
}
