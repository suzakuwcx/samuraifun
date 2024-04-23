package Task.ModelTask;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import FunctionBus.ServerBus;

public class ItemDisplayAnimationTask implements Runnable {
    private ItemDisplay display;

    public static void execute(Location location, ItemStack item, int tick, Vector3f translation, Quaternionf left_rotation, Vector3f scale, Quaternionf right_rotation) {
        ItemDisplay display = (ItemDisplay) ServerBus.spawnServerEntity(location, EntityType.ITEM_DISPLAY, false);
        display.setTransformation(
            new Transformation(
                Objects.requireNonNullElse(translation, new Vector3f()), 
                Objects.requireNonNullElse(left_rotation, new Quaternionf()),
                Objects.requireNonNullElse(scale, new Vector3f(1, 1, 1)),
                Objects.requireNonNullElse(left_rotation, new Quaternionf()
            )
        ));
        display.setItemStack(item);
        ItemDisplayAnimationTask task = new ItemDisplayAnimationTask(display);
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
