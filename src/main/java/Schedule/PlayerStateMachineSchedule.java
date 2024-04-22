package Schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Task.StateTask.NormalStateTask;

public class PlayerStateMachineSchedule implements Runnable {
    public static Map<UUID, Boolean> player_leftclick_map;
    public static Map<UUID, Boolean> player_normal_attack_map;
    public static Map<UUID, Boolean> player_jump_map;
    public static Map<UUID, Boolean> player_defense_map;

    public static Map<UUID, Runnable> player_state_map;

    static {
        player_leftclick_map = new HashMap<>();
        player_normal_attack_map = new HashMap<>();
        player_jump_map = new HashMap<>();
        player_defense_map = new HashMap<>();
        player_state_map = new HashMap<>();
    }

    public static void init(Player player) {
        player_leftclick_map.put(player.getUniqueId(), false);
        player_normal_attack_map.put(player.getUniqueId(), false);
        player_jump_map.put(player.getUniqueId(), false);
        player_defense_map.put(player.getUniqueId(), false);
        player_state_map.put(player.getUniqueId(), new NormalStateTask(player));
    }

    private static void refreshPlayerState(Player player) {
        player_leftclick_map.put(player.getUniqueId(), false);
        player_normal_attack_map.put(player.getUniqueId(), false);
        player_jump_map.put(player.getUniqueId(), false);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player_state_map.get(player.getUniqueId()).run();
            refreshPlayerState(player);
        }
    }
}
