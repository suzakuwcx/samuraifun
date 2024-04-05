package FunctionBus;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerSlashBus {
    public static void onPlayerSlash(Player player) {
        for (Entity e: ServerBus.getNearbyEntities(player.getEyeLocation(), 4, 2, 4, EntityType.ARROW)) {
            if (PlayerBus.isEntityInFrontOfPlayer(player, e, 4, Math.PI / 4))
            e.setVelocity(e.getVelocity().multiply(-1.5));
        }
    }
}
