package Assert.Entity;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;

import Assert.Item.BattleFlag;

public class BattleFlagEntity extends SpawnEntity<ItemDisplay> {
    public BattleFlagEntity(Location location) {
        super(location, ItemDisplay.class);

        ItemDisplay display = super.getEntity();
        display.setItemStack(new BattleFlag());
    }
}
