package Task.StateTask;

import org.bukkit.entity.Player;

import FunctionBus.PlayerBus;
import Schedule.PlayerStateMachineSchedule;

public class NormalStateTask implements Runnable {
    private Player player;

    public NormalStateTask(Player player) {
        this.player = player;

        player.setWalkSpeed(0.2f);
    }

    private static boolean isPlayerInNormalAttack(Player player) {
        if (!PlayerStateMachineSchedule.player_leftclick_map.get(player.getUniqueId()))
            return false;

        if (PlayerStateMachineSchedule.player_jump_map.get(player.getUniqueId()))
            return false;
        
        if (PlayerStateMachineSchedule.player_defense_map.get(player.getUniqueId()))
            return false;

        if (PlayerBus.isPlayerCanSprintingCriticalAttack(player))
            return false;

        return true;
    }

    private static void onPlayerInNormalAttack(Player player) {
        PlayerStateMachineSchedule.player_state_map.put(player.getUniqueId(), new NormalAttackStateTask(player));
    }

    private static boolean isPlayerDefense(Player player) {
        if (!PlayerStateMachineSchedule.player_defense_map.get(player.getUniqueId()))
            return false;

        return true;
    }

    private static void onPlayerDefense(Player player) {
        PlayerStateMachineSchedule.player_state_map.put(player.getUniqueId(), new DefenseStateTask(player));
    }

    @Override
    public void run() {
        if (isPlayerDefense(player)) {
            onPlayerDefense(player);
        } else if (isPlayerInNormalAttack(player)) {
            onPlayerInNormalAttack(player);
        } else {
        }
    }
}
