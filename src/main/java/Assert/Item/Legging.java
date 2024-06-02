package Assert.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Assert.Config.Role;
import net.kyori.adventure.text.Component;

public class Legging {
    private static Component display_name;

    static {
        display_name = Component.text("");
    }

    public static ItemStack getItem(Role role) {
        ItemStack item;
        ItemMeta meta;

        switch (role) {
            case COMMON:
            case SAMURAI:
                item = new ItemStack(Material.DIAMOND_LEGGINGS);
                break;
            case RONIN:
                item = new ItemStack(Material.IRON_LEGGINGS);
                break;
            case SHINBI:
                item = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                break;
            case SOHEI:
                item = new ItemStack(Material.GOLDEN_LEGGINGS);
                break;
            default:
                item = new ItemStack(Material.DIAMOND_LEGGINGS);
        }

        meta = item.getItemMeta();

        meta.displayName(display_name);
        item.setItemMeta(meta);
        return item;
    }
}
