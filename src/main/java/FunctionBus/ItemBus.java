package FunctionBus;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemBus {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setItemNBT(ItemStack item, String key, PersistentDataType type, Object value) {
        NamespacedKey namespaced_key = ServerBus.getNamespacedKey(key);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;

        meta.getPersistentDataContainer().set(namespaced_key, type, value);
        item.setItemMeta(meta);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object getItemNBT(ItemStack item, String key, PersistentDataType type) {
        NamespacedKey namespaced_key = ServerBus.getNamespacedKey(key);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return null;

        Object value = meta.getPersistentDataContainer().get(namespaced_key, type);
        return value;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static boolean hasItemNBT(ItemStack item, String key, PersistentDataType type) {
        NamespacedKey namespaced_key = ServerBus.getNamespacedKey(key);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return false;

        return meta.getPersistentDataContainer().has(namespaced_key, type);
    }
}
