package Assert.Entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class SpwanEntity<T extends Entity> {
    private Location location;
    private T entity;

    public SpwanEntity(Location location, Class<T> clazz) {
        this.location = location;

        this.entity = location.getWorld().createEntity(location, clazz);
    }

    public T getEntity() {
        return entity;
    }

    public void spwan() {
        location.getWorld().addEntity(entity);
    }
}
