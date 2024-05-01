package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import Assert.Config.State;
import Assert.Item.Sword;
import FunctionBus.PlayerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;

public class BattleStateTask extends BaseStateTask {
    private Player player;

    public BattleStateTask(Player player) {
        this.player = player;

        player.setCooldown(Material.SHIELD, 0);
    }

    public static void onPlayerAttack(PlayerInteractEvent event) {
        State state = PlayerStateMachineSchedule.player_state_map.get(event.getPlayer().getUniqueId());
        state.state = new NormalAttackStateTask(event.getPlayer());
    }

    public static void onPlayerAttack(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        PlayerStateMachineSchedule.setStateTask(player, new NormalAttackStateTask(player));
    }

    private static void onPlayerDefense(PlayerInteractEvent event) {
        PlayerBus.setPlayerInventoryList(event.getPlayer(), new Sword(1003), 0, 3, 6);
        PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new DefenseStateTask(event.getPlayer()));
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
            onPlayerDefense(event);
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
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
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
