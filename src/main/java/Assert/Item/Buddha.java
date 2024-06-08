package Assert.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

public class Buddha extends ItemStack {
    private static Component display_name;
    private static Material material;
    private static int custom_mode_data;

    static {
        display_name = Component.text("Buddha");
        material = Material.GOLD_INGOT;
        custom_mode_data = 12;
    }

    public Buddha() {
        super(material);

        ItemMeta meta = this.getItemMeta();

        meta.displayName(display_name);
        meta.setCustomModelData(custom_mode_data);
        this.setItemMeta(meta);
    }


    public static boolean _instanceof(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (item.getType() != material)
            return false;
        else if (meta.getCustomModelData() != custom_mode_data)
            return false;

        return true;
    }
}
