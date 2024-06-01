package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffectType;

import Assert.Config.PlayerConfig;
import Assert.Config.State;
import Assert.Font.FontDatabase;
import Assert.Item.Sword;
import DataBus.PlayerDataBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;
import net.kyori.adventure.text.Component;

public class BattleStateTask extends BaseStateTask {
    private Player player;

    public BattleStateTask(Player player) {
        this.player = player;

        TextDisplay display = PlayerDataBus.getPlayerRingDisplay(player);
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        
        if (display != null)
            display.text(Component.text(FontDatabase.getRingFont(state.role)));

        player.setCooldown(Material.SHIELD, 0);
        /* Disable player sprint */
        player.removePotionEffect(PotionEffectType.SPEED);
        player.setFoodLevel(1);
    }

    public static void onPlayerAttack(PlayerInteractEvent event) {
        State state = PlayerStateMachineSchedule.player_state_map.get(event.getPlayer().getUniqueId());
        state.state = new NormalAttackStateTask(event.getPlayer());
    }

    public static void onPlayerAttack(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        PlayerStateMachineSchedule.setStateTask(player, new NormalAttackStateTask(player));
    }

    public static void onPlayerChargingAttack(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        PlayerStateMachineSchedule.setStateTask(player, new ChargingAttackStateTask(player));
    }

    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (StateEventBus.isPlayerAttack(event)) {
            onPlayerAttack(event);
        } else if (StateEventBus.isPlayerDefense(event)) {
            StateEventBus.onPlayerDefense(event);
        }
    }

    @Override
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (StateEventBus.isPlayerAttack(event)) {
            onPlayerAttack(event);
        }
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        if (StateEventBus.isPlayerChargingAttack(event)) {
            onPlayerChargingAttack(event);
        }
    }

    @Override
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        event.setCancelled(false);
        if (event.getNewSlot() == 0 || event.getNewSlot() == 3 || event.getNewSlot() == 6) {
            ServerBus.playServerSound(event.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1f, 1f);
        } else if (event.getNewSlot() == 1 || event.getNewSlot() == 4 || event.getNewSlot() == 7) {
            ServerBus.playServerSound(event.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_GOLD, 1f, 1f);
        } else if (event.getNewSlot() == 2 || event.getNewSlot() == 5 || event.getNewSlot() == 8) {
            ServerBus.playServerSound(event.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1f, 1f);            
            ServerBus.playServerSound(event.getPlayer().getLocation(), Sound.ITEM_CROSSBOW_LOADING_START, 1f, 1f);
            ServerBus.playServerSound(event.getPlayer().getLocation(), Sound.ITEM_CROSSBOW_LOADING_END, 1f, 1f);
        }
    }

    @Override
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        if (state.sword_cooldown != 0)
            return;

        state.sword_cooldown = PlayerConfig.SWORD_COOLDOWN;
        ServerBus.playServerSound(event.getPlayer().getLocation(), Sound.ITEM_AXE_SCRAPE, 0.5f, 1f);
        ServerBus.playServerSound(event.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1f, 0.8f);
        
        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            PlayerBus.setPlayerInventoryList(p, new Sword(1002), 0, 3, 6);
        }, 2, event.getPlayer());

        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            PlayerBus.setPlayerInventoryList(p, new Sword(1001), 0, 3, 6);
        }, 4, event.getPlayer());
        PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new NormalStateTask(event.getPlayer()));
    }

    @Override
    public void run() {

    }
}
