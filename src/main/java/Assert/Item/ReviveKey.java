package Assert.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

public class ReviveKey extends ItemStack {
    public static Component display_name;
    public static Material material;

    static {
        display_name = Component.text("");
        material = Material.FEATHER;
    }

    public ReviveKey() {
        super(material);

        ItemMeta meta = this.getItemMeta();

        meta.displayName(display_name);
        meta.setCustomModelData(1);
        this.setItemMeta(meta);
    }


    public static boolean _instanceof(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (item.getType() != material)
            return false;
        else if (!(display_name.equals(meta.displayName())))
            return false;
        return true;
    }
}
