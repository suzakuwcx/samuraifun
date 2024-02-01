package FunctionBus;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class ServerBus {
    private static Plugin plugin;
    private static Random random;

    static {
        plugin = Bukkit.getServer().getPluginManager().getPlugin("csol");
        random = new Random(System.currentTimeMillis());
    }

    public static NamespacedKey getNamespacedKey(String key) {
        return new NamespacedKey(getPlugin(), key);
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static Random getRandom() {
        return random;
    }
}
