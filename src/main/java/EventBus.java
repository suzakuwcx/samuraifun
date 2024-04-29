import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import FunctionBus.EntityDamageByEntityEventBus;
import FunctionBus.InventoryClickEventBus;
import FunctionBus.PlayerDropItemEventBus;
import FunctionBus.PlayerInteractEventBus;
import FunctionBus.PlayerItemHeldEventBus;
import FunctionBus.PlayerJoinEventBus;
import FunctionBus.PlayerMoveEventBus;
import FunctionBus.PlayerQuitEventBus;
import FunctionBus.PlayerStopUsingItemEventBus;
import FunctionBus.PlayerSwapHandItemsEventBus;
import FunctionBus.PlayerToggleSneakEventBus;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;

public class EventBus implements Listener {    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        PlayerJoinEventBus.onBusTrigger(event);

        if (PlayerJoinEventBus.isPlayerOperator(event)) {
            PlayerJoinEventBus.onPlayerOperator(event);
        }

        PlayerJoinEventBus.onBusComplete(event);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        PlayerQuitEventBus.onBusTrigger(event);
        PlayerQuitEventBus.onBusComplete(event);
    }


    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (PlayerInteractEventBus.isTriggeredByDropItemEvent(event)) {
            PlayerInteractEventBus.onTriggeredByDropItemEvent(event);
        } else if (PlayerInteractEventBus.isTargetBlockInteractAble(event)) {
            PlayerInteractEventBus.onTargetBlockInteractAble(event);
        } else if (PlayerInteractEventBus.isPlayerBeginUsingRifle(event)) {
            PlayerInteractEventBus.onPlayerBeginUsingRifle(event);
        } else if (PlayerInteractEventBus.isPlayerSlash(event)) {
            PlayerInteractEventBus.onPlayerSlash(event);
        } else if (PlayerInteractEventBus.isPlayerBeginDefense(event)) {
            PlayerInteractEventBus.onPlayerBeginDefense(event);
        } else if (PlayerInteractEventBus.isPlayerUsingBattleFlag(event)) {
            PlayerInteractEventBus.onPlayerUsingBattleFlag(event);
        } else if (PlayerInteractEventBus.isPlayerUsingSmokingDartsEntity(event)) {
            PlayerInteractEventBus.onPlayerUsingSmokingDartsEntity(event);
        } else if (PlayerInteractEventBus.isPlayerUsingMatchlock(event)) {
            PlayerInteractEventBus.onPlayerUsingMatchlock(event);
        }
    }


    @EventHandler
    public void onPlayerStopUsingItemEvent(PlayerStopUsingItemEvent event) {
        if (PlayerStopUsingItemEventBus.isPlayerStopUsingRifle(event)) {
            PlayerStopUsingItemEventBus.onPlayerStopUsingRifle(event);
        } else if (PlayerStopUsingItemEventBus.isPlayerStopDefense(event)) {
            PlayerStopUsingItemEventBus.onPlayerStopDefense(event);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        PlayerMoveEventBus.onBusTrigger(event);
        PlayerMoveEventBus.onBusComplete(event);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (EntityDamageByEntityEventBus.isPlayerSlash(event)) {
            EntityDamageByEntityEventBus.onPlayerSlash(event);
        } else if (EntityDamageByEntityEventBus.isPlayerDefense(event)) {
            EntityDamageByEntityEventBus.onPlayerDefense(event);
        } else if (EntityDamageByEntityEventBus.isPlayerDeflect(event)) {
            EntityDamageByEntityEventBus.onPlayerDeflect(event);
        } else if (EntityDamageByEntityEventBus.isPlayerAttackNoInvincibleFrameEntity(event)) {
            EntityDamageByEntityEventBus.onPlayerAttackNoInvincibleFrameEntity(event);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        PlayerSwapHandItemsEventBus.onBusTrigger(event);

        PlayerSwapHandItemsEventBus.onBusComplete(event);
    }
    
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (InventoryClickEventBus.isPlayerInAdventure(event))
            InventoryClickEventBus.onPlayerInAdventure(event);
    }

    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        if (PlayerItemHeldEventBus.isPlayerInAdventure(event))
            PlayerItemHeldEventBus.onPlayerInAdventure(event);
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        PlayerDropItemEventBus.onBusTrigger(event);
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (PlayerToggleSneakEventBus.isPlayerPrepareDash(event)) {
            PlayerToggleSneakEventBus.onPlayerPrepareDash(event);
        } else if (PlayerToggleSneakEventBus.isPlayerDash(event)) {
            PlayerToggleSneakEventBus.onPlayerDash(event);
        }
    }
}