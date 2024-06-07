package Assert.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Assert.Config.Role;
import Assert.Font.FontDatabase;
import net.kyori.adventure.text.Component;

public class Sword extends ItemStack {
    public static Component display_name;
    public static Material material;

    static {
        display_name = Component.text(String.format("%c%c%c", FontDatabase.SWORD_CHOSEN, FontDatabase.SKILL_ITEM_NO_CHOSEN, FontDatabase.BOW_NO_CHOSEN));
        material = Material.SHIELD;
    }

    public Sword() {
        this(1001);
    }

    public Sword(int date) {
        this(Role.COMMON, date);
    }

    public Sword(Role role, int date) {
        super(material);

        ItemMeta meta = this.getItemMeta();

        meta.displayName(display_name);
        meta.setUnbreakable(true);
        meta.setCustomModelData(date);
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


    public static Material _getType() {
        return material;
    }
}
