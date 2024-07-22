package FunctionBus;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Triple;
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
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.math.MatrixUtil;

import Schedule.PlayerRealVelocityUpdateSchedule;

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

    public static String getServerPath() {
        return plugin.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath();
    }


    public static void playServerSound(Location location, Sound sound, float volume, float pitch) {
        for (Player player : location.getWorld().getPlayers()) {
            player.playSound(location, sound, volume, pitch);
        }
    }

    public static void spawnServerParticle(Particle particle, Location location, int count, BlockData data) {
        location.getWorld().spawnParticle(particle, location, count, data);
    }

    public static void spawnServerDustParticle(Location location, int count, double offsetX, double offsetY, double offsetZ, Particle.DustOptions options) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location, count, offsetX, offsetY, offsetZ, options);
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

    public static RayTraceResult rayTraceEntities(Location location, Vector direction, double distance) {
        return location.getWorld().rayTraceEntities(location, direction, distance);
    }

    public static <T extends Entity> RayTraceResult rayTraceEntities(Location location, Vector direction, double distance, double deviation, EntityType type, UUID ...excluses) {
        return location.getWorld().rayTraceEntities(location, direction, distance, deviation, e -> {
            if (e.getType() != type)
                return false;
            
            for (UUID uuid : excluses) {
                if (e.getUniqueId().equals(uuid)) return false;
            }

            return true;
        });
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
    
    @SuppressWarnings({"unchecked"})
    public static <T extends Entity> Collection<T> getNearbyEntities(Location location, double x, double y, double z, EntityType type, Class<T> clazz) {
        return (Collection<T>) getNearbyEntities(location, x, y, z, type);
    }

    public static <T extends Entity> T getNearestEntity(Location location, double x, double y, double z, EntityType type, Class<T> clazz) {
        T target = null;
        double distance;
        double min = x * x + y * y + z * z;

        for (T entity : getNearbyEntities(location, x, y, z, type, clazz)) {
            distance = entity.getLocation().distance(location);
            if (distance < min) {
                target = entity;
                min = distance;
            }
        }

        return target;
    }

    public static void dropItemStatic(Location location, ItemStack item) {
        location.getWorld().dropItem(location, item).setVelocity(new Vector(0, 0, 0));;
    }

    public static Transformation newTransformation(Matrix4f mat) {
        float f = 1.0F / mat.m33();
        Triple<Quaternionf, Vector3f, Quaternionf> triple = MatrixUtil.svdDecompose((new Matrix3f(mat)).scale(f));
        Vector3f translation = mat.getTranslation(new Vector3f()).mul(f);
        Quaternionf leftRotation = new Quaternionf(triple.getLeft());
        Vector3f scale = new Vector3f(triple.getMiddle());
        Quaternionf rightRotation = new Quaternionf(triple.getRight());
        return new Transformation(translation, leftRotation, scale, rightRotation);
    }

    /*
     * @cite {https://www.spigotmc.org/threads/local-coordinates-calcul.456561/}
     */
    public static Location toLocalCoordinates(Location origin, double left, double up, double forwards) {
        double f = cos((origin.getYaw() + 90.0F) * ((float)Math.PI / 180F));
        double g = sin((origin.getYaw() + 90.0F) * ((float)Math.PI / 180F));
        double h = cos(-origin.getPitch() * ((float)Math.PI / 180F));
        double i = sin(-origin.getPitch() * ((float)Math.PI / 180F));
        double j = cos((-origin.getPitch() + 90.0F) * ((float)Math.PI / 180F));
        double k = sin((-origin.getPitch() + 90.0F) * ((float)Math.PI / 180F));
    
        Vector vec32 = new Vector(f * h, i, (double)(g * h));
        Vector vec33 = new Vector(f * j, k, (double)(g * j));
        Vector vec34 = vec32.clone().crossProduct(vec33).multiply(-1.0D);
        double d = vec32.getX() * forwards + vec33.getX() * up + vec34.getX() * left;
        double e = vec32.getY() * forwards + vec33.getY() * up + vec34.getY() * left;
        double l = vec32.getZ() * forwards + vec33.getZ() * up + vec34.getZ() * left;
        return new Location(origin.getWorld(), origin.getX() + d, origin.getY() + e, origin.getZ() + l);
    }

    public static Location toLocalCoordinates(Location origin, Vector vec) {
        return toLocalCoordinates(origin, vec.getX(), vec.getY(), vec.getZ());
    }

    public static Location getDirectionLocation(Location location, Vector direction, double delta) {
        return location.clone().add(direction.normalize().multiply(delta));
    }

    public static Location getDirectionLocation(Location location, double delta) {
        return getDirectionLocation(location, location.getDirection(), delta);
    }

    public static void knockback(Player player, Vector direction, double level) {
        Vector velocity = PlayerRealVelocityUpdateSchedule.getVelocity(player);
        player.setVelocity(direction.normalize().multiply(level).setY(velocity.getY()));
    }

    /**
     * To get velocity for entity that can reach the distance
     * 
     * @param distance The distance to reach
     * @return the velocity should entity set
     */
    public static double getDistanceVelocity(double distance) {
        return distance * 0.38;
    }

    public static void playerHideServerEntity(Player player, Entity entity) {
        for (Player p : player.getWorld().getPlayers()) {
            p.hideEntity(plugin, entity);
        }
    }
}
