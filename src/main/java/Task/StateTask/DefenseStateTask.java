package Task.StateTask;

import org.bukkit.entity.Player;

import Schedule.PlayerStateMachineSchedule;

public class DefenseStateTask implements Runnable {
    private Player player;

    private int tick = 0;

    public DefenseStateTask(Player player) {
        this.player = player;
    }

    private boolean isPlayerStopDefense() {
        if (PlayerStateMachineSchedule.player_defense_map.get(player.getUniqueId()))
            return false;

        return true;
    }

    public void onPlayerStopDefense() {
        PlayerStateMachineSchedule.player_state_map.put(player.getUniqueId(), new NormalStateTask(player));
    }

    @Override
    public void run() {
        if (isPlayerStopDefense()) {
            onPlayerStopDefense();
        }
        player.sendMessage("防御");
    }
}
