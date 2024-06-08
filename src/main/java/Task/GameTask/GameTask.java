package Task.GameTask;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

import Assert.Item.Buddha;
import DataBus.ConfigBus;
import FunctionBus.ServerBus;

public class GameTask implements Runnable {
    private static GameTask task;
    private int tick;
    private int task_id;
    private Set<ItemDisplay> buddha_set;

    {
        buddha_set = new HashSet<>();
    }

    public static void start() {
        GameTask new_task = new GameTask();
        new_task.task_id = Bukkit.getScheduler().runTaskTimer(ServerBus.getPlugin(), new_task, 0, 1).getTaskId();
        task = new_task;

        for (ItemDisplay display : Bukkit.getServer().getWorlds().get(0).getEntitiesByClass(ItemDisplay.class)) {
            ItemStack item = display.getItemStack();
            if (!Buddha._instanceof(item))
                continue;

            task.buddha_set.add(display);
        }
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
