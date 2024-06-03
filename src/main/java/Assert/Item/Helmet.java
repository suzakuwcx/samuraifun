package Assert.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Assert.Config.Role;
import net.kyori.adventure.text.Component;

public class Helmet extends ItemStack {
    private static Component display_name;
    private static Material material;

    static {
        display_name = Component.text("");
        material = Material.GOLD_INGOT;
    }

    public Helmet() {
        this(Role.COMMON);
    }

    public Helmet(Role role) {
        super(material);
        ItemMeta meta = this.getItemMeta();

        switch (role) {
            case COMMON:
            case SAMURAI:
                meta.setCustomModelData(1);
                break;
            case RONIN:
                meta.setCustomModelData(2);
                break;
            case SHINBI:
                meta.setCustomModelData(3);
                break;
            case SOHEI:
                meta.setCustomModelData(4);
                break;
        }

        meta.displayName(display_name);
        this.setItemMeta(meta);
    }
}
