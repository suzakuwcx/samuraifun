package Assert.Item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import Assert.Config.Role;
import net.kyori.adventure.text.Component;

public class Boot {
    private static Component display_name;

    static {
        display_name = Component.text("");
    }

    public static ItemStack getItem(Role role, boolean is_blue) {
        ItemStack item;
        ArmorMeta meta;

        if (!is_blue) {
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

            meta = (ArmorMeta)item.getItemMeta();
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            meta.displayName(display_name);
        } else {
            item = new ItemStack(Material.LEATHER_BOOTS);
            meta = (ArmorMeta) item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);

            switch (role) {
                case COMMON:
                case SAMURAI:
                    meta.setTrim(new ArmorTrim(TrimMaterial.IRON, TrimPattern.DUNE));
                    break;
                case RONIN:
                    meta.setTrim(new ArmorTrim(TrimMaterial.IRON, TrimPattern.RAISER));
                    break;
                case SHINBI:
                    meta.setTrim(new ArmorTrim(TrimMaterial.IRON, TrimPattern.COAST));
                    break;
                case SOHEI:
                    meta.setTrim(new ArmorTrim(TrimMaterial.IRON, TrimPattern.HOST));
                    break;
                default:
                    item = new ItemStack(Material.DIAMOND_BOOTS);
            }
        }

        item.setItemMeta(meta);
        return item;
    }
}
