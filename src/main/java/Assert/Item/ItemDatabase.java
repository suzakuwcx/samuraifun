package Assert.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import Assert.Item.Gun.Rifle;

public class ItemDatabase {
    private static Set<ItemStack> set = new HashSet<>();

    static {
    }

    public static Set<ItemStack> getSet() {
        return set;
    }
}
