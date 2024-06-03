import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import FunctionBus.EntityDamageByEntityEventBus;
import FunctionBus.InventoryClickEventBus;
import FunctionBus.PlayerDeathEventBus;
import FunctionBus.PlayerDropItemEventBus;
import FunctionBus.PlayerInteractEventBus;
import FunctionBus.PlayerJoinEventBus;
import FunctionBus.PlayerMoveEventBus;
import FunctionBus.PlayerQuitEventBus;
import FunctionBus.PlayerRespawnEventBus;
import FunctionBus.PlayerStopUsingItemEventBus;
import FunctionBus.PlayerSwapHandItemsEventBus;
import Schedule.PlayerStateMachineSchedule;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
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
        } else if (PlayerInteractEventBus.isPlayerUsingBattleFlag(event)) {
            PlayerInteractEventBus.onPlayerUsingBattleFlag(event);
        } else if (PlayerInteractEventBus.isPlayerUsingSmokingDartsEntity(event)) {
            PlayerInteractEventBus.onPlayerUsingSmokingDartsEntity(event);
        } else if (PlayerInteractEventBus.isPlayerUsingMatchlock(event)) {
            PlayerInteractEventBus.onPlayerUsingMatchlock(event);
        } else if (PlayerStateMachineSchedule.getStateTask(event.getPlayer()).isStateEvent(event)) {
            PlayerStateMachineSchedule.getStateTask(event.getPlayer()).onPlayerInteractEvent(event);
        }
    }


    @EventHandler
    public void onPlayerStopUsingItemEvent(PlayerStopUsingItemEvent event) {
        if (PlayerStopUsingItemEventBus.isPlayerStopUsingRifle(event)) {
            PlayerStopUsingItemEventBus.onPlayerStopUsingRifle(event);
        } else if (PlayerStateMachineSchedule.getStateTask(event.getPlayer()).isStateEvent(event)) {
            PlayerStateMachineSchedule.getStateTask(event.getPlayer()).onPlayerStopUsingItemEvent(event);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        PlayerMoveEventBus.onBusTrigger(event);
        PlayerMoveEventBus.onBusComplete(event);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        EntityDamageByEntityEventBus.onBusTrigger(event);

        if (EntityDamageByEntityEventBus.isPlayerAttack(event)) {
            EntityDamageByEntityEventBus.onPlayerAttack(event);
        }

        EntityDamageByEntityEventBus.onBusComplete(event);
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        PlayerSwapHandItemsEventBus.onBusTrigger(event);

        if (PlayerStateMachineSchedule.getStateTask(event.getPlayer()).isStateEvent(event)) {
            PlayerStateMachineSchedule.getStateTask(event.getPlayer()).onPlayerSwapHandItemsEvent(event);
        }

        PlayerSwapHandItemsEventBus.onBusComplete(event);
    }
    
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (InventoryClickEventBus.isPlayerInAdventure(event))
            InventoryClickEventBus.onPlayerInAdventure(event);
    }

    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        if (PlayerStateMachineSchedule.getStateTask(event.getPlayer()).isStateEvent(event)) {
            PlayerStateMachineSchedule.getStateTask(event.getPlayer()).onPlayerItemHeldEvent(event);
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        PlayerDropItemEventBus.onBusTrigger(event);

        if (PlayerStateMachineSchedule.getStateTask(event.getPlayer()).isStateEvent(event)) {
            PlayerStateMachineSchedule.getStateTask(event.getPlayer()).onPlayerDropItemEvent(event);
        }
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (PlayerStateMachineSchedule.getStateTask(event.getPlayer()).isStateEvent(event)) {
            PlayerStateMachineSchedule.getStateTask(event.getPlayer()).onPlayerToggleSneakEvent(event);
        }
    }

    @EventHandler
    public void onPlayerArmSwingEvent(PlayerArmSwingEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        PlayerDeathEventBus.onBusTrigger(event);
        if (PlayerDeathEventBus.isPlayerDeadByPlugin(event))
            PlayerDeathEventBus.onPlayerDeadByPlugin(event);
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        PlayerRespawnEventBus.onBusTrigger(event);
    }
}