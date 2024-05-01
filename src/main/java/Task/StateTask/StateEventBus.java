package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import Assert.Config.PlayerConfig;
import DataBus.PlayerDataBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;

public class StateEventBus {
    public static boolean isPlayerAttack(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return false;

        return true;
    }

    public static boolean isPlayerAttack(EntityDamageByEntityEvent event) {
        return true;
    }

    public static boolean isPlayerDefense(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (event.getPlayer().getCooldown(Material.SHIELD) != 0)
            return false;

        return true;
    }

    public static boolean isPlayerChargingAttack(PlayerSwapHandItemsEvent event) {
        return true;
    }

    public static void playerSectorSlash(Player player, double scope) {
        for (Player e: ServerBus.getNearbyEntities(player.getEyeLocation(), PlayerConfig.BASIC_ATTACK_RANGE, 2, PlayerConfig.BASIC_ATTACK_RANGE, EntityType.PLAYER, Player.class)) {
            if (e.equals(player))
                continue;

            if (!PlayerBus.isEntityInFrontOfPlayer(player, e, PlayerConfig.BASIC_ATTACK_RANGE, scope))
                continue;
            
            PlayerDataBus.downPlayerSlash(player);
            player.attack(e);
        }
    }

    public static boolean isPlayerSlash(Player player) {
        if (!PlayerDataBus.upPlayerSlash(player))
            return false;

        return true;
    }
}
