package Assert.Entity;

import org.bukkit.Location;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay;

import Assert.Item.SmokingDarts;

public class SmokingDartsEntity extends SpawnEntity<ItemDisplay> {
    public SmokingDartsEntity(Location location) {
        super(location, ItemDisplay.class);
    
        ItemDisplay display = super.getEntity();
        display.setItemStack(new SmokingDarts());
        display.setBrightness(new Brightness(15, 15));
        display.setInterpolationDelay(0);
        display.setTeleportDuration(1);
    }
}
