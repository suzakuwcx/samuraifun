package Task.GameTask;

import org.bukkit.Bukkit;

import DataBus.ConfigBus;
import FunctionBus.ServerBus;

public class GameTask implements Runnable {
    private static GameTask task;
    private int tick;
    private int task_id;

    public static void start() {
        GameTask new_task = new GameTask();
        new_task.task_id = Bukkit.getScheduler().runTaskTimer(ServerBus.getPlugin(), new_task, 0, 1).getTaskId();
        task = new_task;
    }

    public static boolean isInGame() {
        return task != null;
    }

    @Override
    public void run() {
        if (tick == ConfigBus.getValue("game_time", Integer.class)) {
            Bukkit.getScheduler().cancelTask(task_id);
            return;
        }

        ++tick;
    }
}
