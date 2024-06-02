package Assert.Item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Assert.Config.Role;
import net.kyori.adventure.text.Component;

public class Boot {
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
                item = new ItemStack(Material.DIAMOND_BOOTS);
                break;
            case RONIN:
                item = new ItemStack(Material.IRON_BOOTS);
                break;
            case SHINBI:
                item = new ItemStack(Material.CHAINMAIL_BOOTS);
                break;
            case SOHEI:
                item = new ItemStack(Material.GOLDEN_BOOTS);
                break;
            default:
                item = new ItemStack(Material.DIAMOND_BOOTS);
        }

        meta = item.getItemMeta();

        meta.displayName(display_name);
        item.setItemMeta(meta);
        item.addEnchantment(Enchantment.BINDING_CURSE, 1);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return item;
    }
}
