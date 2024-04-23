package Task.ModelTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.ItemDisplay;

import Assert.Entity.SpwanEntity;
import FunctionBus.ServerBus;

public class ItemDisplayAnimationTask implements Runnable {
    private ItemDisplay display;

    public static void execute(SpwanEntity<ItemDisplay> entity, int tick) {
        entity.spwan();
        ItemDisplayAnimationTask task = new ItemDisplayAnimationTask(entity.getEntity());
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), task, tick);
    }

    private ItemDisplayAnimationTask(ItemDisplay display) {
        this.display = display;
    }

    @Override
    public void run() {
        display.remove();
    }
}
