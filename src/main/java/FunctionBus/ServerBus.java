package FunctionBus;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

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


    public static void playServerSound(Location location, Sound sound, float volume, float pitch) {
        for (Player player : location.getWorld().getPlayers()) {
            player.playSound(location, sound, volume, pitch);
        }
    }

    public static void spawnServerParticle(Particle particle, Location location, int count, BlockData data) {
        location.getWorld().spawnParticle(particle, location, count, data);
    }

    public static void spawnServerParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }

    public static void spawnServerParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
    }

    public static Entity spawnServerEntity(Location location, EntityType type, boolean randomizeData) {
        return location.getWorld().spawnEntity(location, type, randomizeData);
    }

    public static void give(Player player, ItemStack item) {
        Map<Integer, ItemStack> to_drop_items;
        Item drop_item;
        
        to_drop_items = player.getInventory().addItem(item);
        for (int i = 0; i < to_drop_items.size(); ++i) {
            drop_item = player.getWorld().dropItem(player.getLocation(), to_drop_items.get(i));
            drop_item.setVelocity(drop_item.getVelocity().zero());
        }
    }

    public static void runCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static Player getPlayerByName(String name) {
        return Bukkit.getPlayerExact(name);
    }

    public static Collection<Entity> getNearbyEntities(Location location, double x, double y, double z, EntityType type) {
        return location.getWorld().getNearbyEntities(location, x, y, z, (e) -> e.getType() == type);
    }

    public static void dropItemStatic(Location location, ItemStack item) {
        location.getWorld().dropItem(location, item).setVelocity(new Vector(0, 0, 0));;
    }
}
