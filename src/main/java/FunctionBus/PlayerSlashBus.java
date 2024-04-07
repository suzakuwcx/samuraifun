package FunctionBus;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import DataBus.PlayerDataBus;

public class PlayerSlashBus {
    public static void onPlayerSlash(Player player) {
        for (Zombie e: ServerBus.getNearbyEntities(player.getEyeLocation(), 4, 2, 4, EntityType.ZOMBIE, Zombie.class)) {
            if (!PlayerBus.isEntityInFrontOfPlayer(player, e, 4, 4 * Math.PI / 3))
                continue;
            
            PlayerDataBus.downPlayerSlash(player);
            player.attack(e);
        }

        for (Arrow e: ServerBus.getNearbyEntities(player.getEyeLocation(), 4, 2, 4, EntityType.ARROW, Arrow.class)) {
            if (PlayerBus.isEntityInFrontOfPlayer(player, e, 4, Math.PI / 3))
                e.setVelocity(e.getVelocity().multiply(-1.5));
        }
    }
}
