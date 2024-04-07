package Task.AttackTask;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import FunctionBus.ServerBus;

public class DeflectTask implements Runnable{
    private static Map<Player, DeflectTask> task_mapper;
    private static final int DeflectTick = 4;

    private Player player;
    private int tick = 0;
    private int failure = 0;

    static {
        task_mapper = new HashMap<>();
    }
    
    public static boolean isPlayerDefense(Player player) {
        if (!task_mapper.containsKey(player))
            return false;

        DeflectTask task = task_mapper.get(player);
        if (task.failure > 2)
            return false;

        return true;
    }

    public static void execute(Player player) {
        DeflectTask task = null;

        if (task_mapper.containsKey(player)) {
            task = task_mapper.get(player);
            ++task.failure;
            task.tick = 0;
            return;
        }

        task = new DeflectTask();
        task_mapper.put(player, task);
        task.player = player;

        Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
    }

    @Override
    public void run() {
        if (tick == DeflectTick) {
            task_mapper.remove(player);
            return;
        }

        ++tick;
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
    }
}
