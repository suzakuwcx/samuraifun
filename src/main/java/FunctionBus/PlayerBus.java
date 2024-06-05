package FunctionBus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.kyori.adventure.text.Component;

public class PlayerBus {
    public static boolean isPlayerOnline(Player player) {
        Player p = Bukkit.getServer().getPlayerExact(player.getName());
        if (p == null)
            return false;
        
        if (!p.getName().equalsIgnoreCase(player.getName())) {
            return false;
        }

        return true;
    }

    public static boolean isPlayerCanCriticalAttack(Player player) {
        if (!isPlayerCanCriticalAttack(player))
            return false;

        if (player.isSprinting())
            return false;

        return true;
    }

    public static boolean isPlayerCanSprintingCriticalAttack(Player player) {
        if (player.getFallDistance() == 0)
            return false;

        if (player.getVelocity().getY() == 0)
            return false;
        
        if (player.isClimbing())
            return false;

        if (player.isInWater())
            return false;

        if (player.hasPotionEffect(PotionEffectType.BLINDNESS))
            return false;

        if (player.getAttackCooldown() < 0.848f)
            return false;

        return true;
    }

    private static Map<Player, ItemStack> player_head = new HashMap<>(); // prevent duplicate socket request to mojang

    public static ItemStack getPlayerHeader(Player player) {
        if (player == null)
            return null;

        ItemStack item = player_head.get(player);
        if (item != null)
            return item;

        item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.displayName(Component.text(player.getName()));
        skull.setPlayerProfile(player.getPlayerProfile());
        item.setItemMeta(skull);

        player_head.put(player, item);
        return item;
    }

    /**
     * @param scope The sector angle of the player's perspective, in radians
     */
    public static boolean isEntityInFrontOfPlayer(Player player, Entity entity, double distance, double scope) {
        Location player_location = player.getLocation();
        Location entity_location = entity.getLocation();

        Vector player_to_entity = entity_location.clone().toVector().setY(0).subtract(player_location.toVector().setY(0));

        /* 0.5 is the target bounding box radius */
        if (player_location.distance(entity_location) - 0.5 > distance)
            return false;

        if (player_location.getDirection().angle(player_to_entity) > scope / 2)
            return false;

        return true;
    }

    /**
     * @param scope This scope is the level length of the real world, in meter
     */
    public static boolean isEntityInFrontOfPlayerLevel(Player player, Entity entity, double max_distance, double scope) {
        Vector player_location = player.getLocation().clone().toVector().setY(0);
        Vector entity_location = entity.getLocation().clone().toVector().setY(0);

        Vector player_to_entity = entity_location.clone().subtract(player_location);
        double distance = player_location.distance(entity_location);
        Vector direction = player.getLocation().getDirection().setY(0).normalize();

        /* 0.5 is the target bounding box radius */
        if (distance - 0.5 > max_distance)
            return false;

        if (player_to_entity.dot(direction) < 0)
            return false;

        if (player_to_entity.normalize().multiply(distance).crossProduct(direction).length() / direction.length() > scope / 2)
            return false;

        return true;
    }

    public static void banPlayerLeftClickAnimation(Player player, int duration) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, duration, 39, false, false));
    }

    public static void banPlayerJump(Player player, int duration) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration, 128, false, false));
    }

    public static void setPlayerInventoryList(Player player, ItemStack item, int... slots) {
        Inventory inventory = player.getInventory();
        for (int i : slots)
            inventory.setItem(i, item);
    }

    public static void disableShield(Player player, int tick) {
        player.setCooldown(Material.SHIELD, tick);
        if (player.getActiveItem().getType() == Material.SHIELD)
            player.completeUsingActiveItem();
    }
}
