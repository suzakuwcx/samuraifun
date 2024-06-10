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
        this(Role.COMMON, false);
    }

    public Helmet(Role role, boolean is_blue) {
        super(material);
        ItemMeta meta = this.getItemMeta();

        if (!is_blue) {
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
        } else {
            switch (role) {
                case COMMON:
                case SAMURAI:
                    meta.setCustomModelData(14);
                    break;
                case RONIN:
                    meta.setCustomModelData(15);
                    break;
                case SHINBI:
                    meta.setCustomModelData(16);
                    break;
                case SOHEI:
                    meta.setCustomModelData(17);
                    break;
            }    
        }

        meta.displayName(display_name);
        this.setItemMeta(meta);
    }
}
