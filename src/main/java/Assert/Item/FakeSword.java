package Assert.Item;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

public class FakeSword {
    private static Material material;
    private static Map<Integer, ItemStack> item_mapper;

    static {
        material = Material.CROSSBOW;
        item_mapper = new HashMap<>();

        int data = 0;
        ItemStack item = new ItemStack(material);
        CrossbowMeta meta = (CrossbowMeta) (item.getItemMeta());
        meta.addChargedProjectile(new ItemStack(Material.ARROW));

        for (int i = 0; i < 26; ++i) {
            for (int j = 0; j < 5; ++j) {
                data = j * 1000 + i;
                meta.setCustomModelData(data);
                item.setItemMeta(meta);
                item_mapper.put(data, item.clone());
            }            
        }
    }

    public static ItemStack getItem(int data) {
        return item_mapper.get(data);
    }
}
