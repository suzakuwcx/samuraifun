package Assert.Item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Assert.Config.Role;
import net.kyori.adventure.text.Component;

public class ChestPlate {
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
                item = new ItemStack(Material.DIAMOND_CHESTPLATE);
                break;
            case RONIN:
                item = new ItemStack(Material.IRON_CHESTPLATE);
                break;
            case SHINBI:
                item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                break;
            case SOHEI:
                item = new ItemStack(Material.GOLDEN_CHESTPLATE);
                break;
            default:
                item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        }

        meta = item.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        meta.displayName(display_name);
        item.setItemMeta(meta);
        return item;
    }
}
