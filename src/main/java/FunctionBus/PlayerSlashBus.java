package FunctionBus;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import DataBus.ConfigBus;
import DataBus.PlayerDataBus;
import Task.SwipeTask.KiriotoshiTask;

public class PlayerSlashBus {
    public static boolean isPlayerCanCriticalAttack(Player player) {
        return PlayerBus.isPlayerCanSprintingCriticalAttack(player);
    }

    public static void onPlayerCanCriticalAttack(Player player) {
        double range = ConfigBus.getValue("basic_attack_range", Double.class) * player.getAttackCooldown() * 1.5;
        KiriotoshiTask.execute(player, range);

        for (Zombie e: ServerBus.getNearbyEntities(player.getEyeLocation(), range, 2, range, EntityType.ZOMBIE, Zombie.class)) {
            if (!PlayerBus.isEntityInFrontOfPlayerLevel(player, e, range, 0.75))
                continue;
            
            PlayerDataBus.downPlayerSlash(player);
            player.attack(e);
        }

        for (Player e: ServerBus.getNearbyEntities(player.getEyeLocation(), range, 4, range, EntityType.PLAYER, Player.class)) {
            if (e.equals(player))
                continue;
            
            if (!PlayerBus.isEntityInFrontOfPlayerLevel(player, e, range, 0.75))
                continue;
            
            PlayerDataBus.downPlayerSlash(player);
            player.attack(e);
        }
    }

    public static void onPlayerCommonAttack(Player player) {
        double range = ConfigBus.getValue("basic_attack_range", Double.class) * player.getAttackCooldown();
    }

    public static void onPlayerSlash(Player player) {
        if (isPlayerCanCriticalAttack(player)) {
            onPlayerCanCriticalAttack(player);
        } else {
            onPlayerCommonAttack(player);
        }

    }
}
