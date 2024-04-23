package Assert.Entity;

import org.bukkit.Location;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import Assert.Item.SwordSwipe;

public class SwordSwipeAnimation extends SpwanEntity<ItemDisplay>{
    public SwordSwipeAnimation(Location location) {
        super(location, ItemDisplay.class);

        ItemStack item = new SwordSwipe();
        ItemDisplay display = super.getEntity();
        display.setItemStack(item);
        display.setBrightness(new Brightness(15, 15));
        display.setTransformation(
            new Transformation(
                new Vector3f(0, 0.5f, 0),
                new Quaternionf(),
                new Vector3f(2.9f, 1f, 2.9f),
                new Quaternionf()
            )
        );
    }
}
