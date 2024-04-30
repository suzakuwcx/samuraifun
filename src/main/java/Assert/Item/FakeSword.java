package Assert.Item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

public class FakeSword {
    private static Material material;
    private static List<ItemStack> item_list;

    static {
        material = Material.CROSSBOW;
        item_list = new ArrayList<>();

        ItemStack item = new ItemStack(material);
        CrossbowMeta meta = (CrossbowMeta) (item.getItemMeta());
        meta.addChargedProjectile(new ItemStack(Material.ARROW));

        for (int i = 1000; i < 1026; ++i) {
            meta.setCustomModelData(i);
            item.setItemMeta(meta);
            item_list.add(item.clone());
        }
    }

    public static ItemStack getItem(int data) {
        return item_list.get(data - 1000);
    }
}
