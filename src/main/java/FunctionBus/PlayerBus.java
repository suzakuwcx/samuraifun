package FunctionBus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import Assert.Config.State;
import Assert.Item.Sword;
import ConfigBus.ConfigBus;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;
import Task.StateTask.NormalStateTask;
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

    public static void replacePlayerInventoryAnySlot(Player player, ItemStack dst, Function<ItemStack, Boolean> function) {
        Inventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        ItemStack item;
        for (int i = 0; i < items.length; ++i) {
            item = items[i];
            if (function.apply(item))
                items[i] = dst; 
        }

        inventory.setContents(items);
    }

    public static void disableShield(Player player, int tick) {
        player.setCooldown(Material.SHIELD, tick);
        if (player.getActiveItem().getType() == Material.SHIELD)
            player.completeUsingActiveItem();
    }

    public static void resetPlayerState(Player player) {
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        state.posture = ConfigBus.getValue("max_posture", Integer.class);
        state.health = ConfigBus.getValue("max_health", Integer.class);
        state.state = new NormalStateTask(player);

        player.setShieldBlockingDelay(ConfigBus.getValue("deflect_tick", Integer.class));
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
        player.setWalkSpeed(0.2f);
        player.getInventory().setHeldItemSlot(0);

        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            PlayerDataBus.initPlayerItemDisplay(player);
            
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, PotionEffect.INFINITE_DURATION, 3, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION, 3, false, false));
        }, 1, player);

        replacePlayerInventoryAnySlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(1)), (src) -> {
            if (src == null)
                return false;

            return Sword._instanceof(src);
        });
    }
}
