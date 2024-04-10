package FunctionBus;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import Assert.Config.PlayerConfig;
import DataBus.PlayerDataBus;

public class PlayerSlashBus {
    public static void onPlayerSlash(Player player) {
        double range = PlayerConfig.BASIC_ATTACK_RANGE * player.getAttackCooldown();

        for (Zombie e: ServerBus.getNearbyEntities(player.getEyeLocation(), range, 2, range, EntityType.ZOMBIE, Zombie.class)) {
            if (!PlayerBus.isEntityInFrontOfPlayer(player, e, range, 4 * Math.PI / 3))
                continue;
            
            PlayerDataBus.downPlayerSlash(player);
            player.attack(e);
        }

        for (Player e: ServerBus.getNearbyEntities(player.getEyeLocation(), range, 2, range, EntityType.PLAYER, Player.class)) {
            if (e.equals(player))
                continue;
    
            if (!PlayerBus.isEntityInFrontOfPlayer(player, e, range, 4 * Math.PI / 3))
                continue;
            
            PlayerDataBus.downPlayerSlash(player);
            player.attack(e);
        }

        for (Arrow e: ServerBus.getNearbyEntities(player.getEyeLocation(), range, 2, range, EntityType.ARROW, Arrow.class)) {
            if (PlayerBus.isEntityInFrontOfPlayer(player, e, range, Math.PI / 3))
                e.setVelocity(e.getVelocity().multiply(-1.5));
        }
    }
}
