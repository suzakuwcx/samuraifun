package Assert.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SwordSwipe extends ItemStack {
    public static Material material;

    static {
        material = Material.IRON_INGOT;
    }

    public SwordSwipe() {
        super(material);

        ItemMeta meta = this.getItemMeta();

        meta.setCustomModelData(1);
        this.setItemMeta(meta);
    }
}
