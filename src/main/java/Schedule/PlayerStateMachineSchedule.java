package Schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Assert.Config.State;
import Task.StateTask.BaseStateTask;

public class PlayerStateMachineSchedule implements Runnable {
    public static Map<UUID, State> player_state_map = new HashMap<>();

    static {
        player_state_map = new HashMap<>();
    }

    public static void init(Player player) {
        player_state_map.put(player.getUniqueId(), new State(player));
    }

    public static BaseStateTask getStateTask(Player player) {
        return player_state_map.get(player.getUniqueId()).state;
    }

    public static void setStateTask(Player player, BaseStateTask task) {
        player_state_map.get(player.getUniqueId()).state = task;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player_state_map.get(player.getUniqueId()).state.run();
            player_state_map.get(player.getUniqueId()).refresh();
        }
    }
}
