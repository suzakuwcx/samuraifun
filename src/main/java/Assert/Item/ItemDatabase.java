package Assert.Item;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import Assert.Config.Role;
import Assert.Item.Gun.Matchlock;
import Assert.Item.Gun.Rifle;

public class ItemDatabase {
    private static Set<ItemStack> set = new HashSet<>();

    static {
        set.add(new Rifle());
        set.add(new Sword());
        set.add(new BackCompass());
        set.add(new Taijutsu());
        set.add(new BattleFlag());
        set.add(new SmokingDarts());
        set.add(new Matchlock());
        set.add(new Bow());
        
        for (Role role : Role.values()) {
            set.add(new Helmet(role, false));
            set.add(ChestPlate.getItem(role, false));
            set.add(Legging.getItem(role, false));
            set.add(Boot.getItem(role, false));
            set.add(new Helmet(role, true));
            set.add(ChestPlate.getItem(role, true));
            set.add(Legging.getItem(role, true));
            set.add(Boot.getItem(role, true));
        }
    }

    public static Set<ItemStack> getSet() {
        return set;
    }
}
