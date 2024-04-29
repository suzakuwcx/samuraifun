package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import Assert.Config.State;
import Schedule.PlayerStateMachineSchedule;

public class NormalStateTask extends BaseStateTask {
    private Player player;

    public NormalStateTask(Player player) {
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
    public void run() {

    }
}
