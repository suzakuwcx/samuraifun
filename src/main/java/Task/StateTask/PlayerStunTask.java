package Task.StateTask;

import org.bukkit.entity.Player;

import FunctionBus.PlayerBus;
import Schedule.PlayerStateMachineSchedule;

public class PlayerStunTask implements Runnable {
    private Player player;

    private int tick = 0;

    public PlayerStunTask(Player player) {
        this.player = player;

        player.setWalkSpeed(0);
    }

    @Override
    public void run() {
        ++tick;

        if (tick <= 40) {
            PlayerBus.banPlayerJump(player, 2);
        } else {
            PlayerStateMachineSchedule.player_state_map.put(player.getUniqueId(), new NormalStateTask(player));
        }
    }
}
