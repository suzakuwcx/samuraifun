package FunctionBus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import DataBus.PlayerDataBus;

public class PlayerSpawnLocationEventBus {
    public static void onBusTrigger(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        
        /* Init player position, following the PlayerJoinEvent will trigger */
        if (PlayerDataBus.isPlayerFirstJoin(player)) {
            event.setSpawnLocation(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
        }
    }
}
