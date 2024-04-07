package FunctionBus;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import Assert.Item.Sword;
import DataBus.PlayerDataBus;

public class EntityDamageByEntityEventBus {
    public static boolean isPlayerSlash(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER)
            return false;

        Player player = (Player) event.getDamager();
        if (!Sword._instanceof(player.getInventory().getItemInMainHand()))
            return false;

        if (PlayerDataBus.upPlayerSlash(player))
            return false;
        
        return true;
    }

    public static void onPlayerSlash(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
        PlayerSlashBus.onPlayerSlash((Player) event.getDamager());
    }
}
