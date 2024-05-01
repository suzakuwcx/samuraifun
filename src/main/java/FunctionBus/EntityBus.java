package FunctionBus;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class EntityBus {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setEntityNBT(Entity entity, String key, PersistentDataType type, Object value)
    {
        NamespacedKey namespaced_key = ServerBus.getNamespacedKey(key);
        entity.getPersistentDataContainer().set(namespaced_key, type, value);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object getEntityNBT(Entity entity, String key, PersistentDataType type)
    {
        NamespacedKey namespaced_key = ServerBus.getNamespacedKey(key);
        return entity.getPersistentDataContainer().get(namespaced_key, type);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static boolean hasEntityNBT(Entity entity, String key, PersistentDataType type)
    {
        NamespacedKey namespaced_key = ServerBus.getNamespacedKey(key);
        return entity.getPersistentDataContainer().has(namespaced_key, type);
    }   
    
    public static void removeEntityNBT(Entity entity, String key) {
        NamespacedKey namespaced_key = ServerBus.getNamespacedKey(key);
        entity.getPersistentDataContainer().remove(namespaced_key);
    }

    public static Vector getTargetDirection(Entity source, Entity target) {
        return target.getLocation().toVector().subtract(source.getLocation().toVector()).normalize();
    }

    public static double getTargetDistance(Entity source, Entity target) {
        return target.getLocation().toVector().distance(source.getLocation().toVector());
    }
}
