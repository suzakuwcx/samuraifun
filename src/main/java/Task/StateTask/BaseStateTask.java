package Task.StateTask;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;

import Assert.Item.Sword;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;

public abstract class BaseStateTask implements Runnable {
    private Player player;

    public boolean isStateEvent(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND)
            return false;

        if (!Sword._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;

        return true;
    }

    public boolean isStateEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER)
            return false;

        Player player = (Player) event.getDamager();

        if (!Sword._instanceof(player.getInventory().getItemInMainHand()))
            return false;

        return true; 
    }

    public boolean isStateEvent(PlayerSwapHandItemsEvent event) {
        if (!Sword._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;

        return true;
    }

    public boolean isStateEvent(PlayerToggleSneakEvent event) {
        return true;
    }

    public boolean isStateEvent(PlayerDropItemEvent event) {
        if (!Sword._instanceof(event.getItemDrop().getItemStack()))
            return false;

        return true;
    }

    public boolean isStateEvent(PlayerItemHeldEvent event) {
        return true;
    }

    public boolean isStateEvent(PlayerStopUsingItemEvent event) {
        if (!Sword._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;
        
        return true;
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {};
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {};
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {};
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {};
    public void onPlayerStopUsingItemEvent(PlayerStopUsingItemEvent event) {};

    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        event.setCancelled(true);
    };

    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (StateEventBus.isPlayerDash(event)) {
            StateEventBus.onPlayerDash(event);
        }
    };
}
