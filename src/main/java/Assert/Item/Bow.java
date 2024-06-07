package Assert.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Assert.Font.FontDatabase;
import net.kyori.adventure.text.Component;

public class Bow extends ItemStack {
    private static Component display_name;
    private static Material material;
    private static int custom_mode_data;

    static {
        display_name = Component.text(String.format("%c%c%c", FontDatabase.SWORD_NO_CHOSEN, FontDatabase.SKILL_ITEM_NO_CHOSEN, FontDatabase.BOW_CHOSEN));
        material = Material.BOW;
        custom_mode_data = 1;
    }

    public Bow() {
        super(material);

        ItemMeta meta = this.getItemMeta();

        meta.displayName(display_name);
        meta.setCustomModelData(custom_mode_data);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        this.setItemMeta(meta);
    }


    public static boolean _instanceof(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (item.getType() != material)
            return false;
        else if (!(display_name.equals(meta.displayName())))
            return false;
        else if (meta.getCustomModelData() != custom_mode_data)
            return false;

        return true;
    }
}
