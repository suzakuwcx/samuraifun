package Assert.Item.Gun;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

public class Rifle extends ItemStack {
    public static Component display_name;
    public static Material material;

    static {
        display_name = Component.text("Rifle");
        material = Material.CROSSBOW;
    }

    public Rifle() {
        super(material);

        ItemMeta meta = this.getItemMeta();

        meta.displayName(display_name);
        meta.addEnchant(Enchantment.QUICK_CHARGE, 6, true);
        meta.setUnbreakable(true);
        this.setItemMeta(meta);
    }
}
