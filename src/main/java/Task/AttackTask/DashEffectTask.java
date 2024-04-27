package Task.AttackTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;

public class DashEffectTask implements Runnable {
    private static Map<UUID, DashEffectTask> task_mapper;

    private Player player;
    private int task_id;

    static {
        task_mapper = new HashMap<>();
    }

    public static void execute(Player player) {
        if (task_mapper.containsKey(player.getUniqueId()))
            return;

        DashEffectTask task = new DashEffectTask(player);
        task_mapper.put(player.getUniqueId(), task);
        task.task_id = Bukkit.getScheduler().runTaskTimer(ServerBus.getPlugin(), task, 0, 3).getTaskId();
    }

    public static void stop(Player player) {
        if (!task_mapper.containsKey(player.getUniqueId()))
            return;
        
        DashEffectTask task = task_mapper.get(player.getUniqueId());
        Bukkit.getScheduler().cancelTask(task.task_id);
        task_mapper.remove(player.getUniqueId());
    }
    
    private DashEffectTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        PlayerBus.banPlayerJump(player, 5);
    }
}
