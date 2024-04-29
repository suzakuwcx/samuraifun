package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

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
}
