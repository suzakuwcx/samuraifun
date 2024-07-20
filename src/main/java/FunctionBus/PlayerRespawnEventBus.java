package FunctionBus;

import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnEventBus {
    public static void onBusTrigger(PlayerRespawnEvent event) {
        PlayerBus.resetPlayerState(event.getPlayer());
    }
}
