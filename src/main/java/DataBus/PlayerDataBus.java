package DataBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import Assert.Entity.HealthRingEntity;
import Assert.Entity.PostureRingEntity;
import Assert.Entity.RingEntity;
import Assert.Entity.SpawnEntity;

public class PlayerDataBus {
    private static Map<UUID, List<TextDisplay>> item_display_mapper = new HashMap<>();
    private static Map<UUID, Integer> player_slash_semaphore = new HashMap<>();
    private static Map<UUID, Integer> player_drop_item_semaphore = new HashMap<>();
    private static Map<UUID, Player> player_dead_by_plugin_mapper = new HashMap<>();


    public static void initPlayerItemDisplay(Player player) {
        addDisplay(player, new RingEntity(player.getLocation()), 0);
        addDisplay(player, new HealthRingEntity(player.getLocation(), 0), 1);
        addDisplay(player, new PostureRingEntity(player.getLocation(), 0), 2);
    }


    public static List<TextDisplay> getPlayerItemDisplay(Player player) {
        List<TextDisplay> displays = item_display_mapper.get(player.getUniqueId());
        if (displays == null) {
            displays = Arrays.asList(null, null, null);
            item_display_mapper.put(player.getUniqueId(), displays);
        }

        return displays;
    }


    private static TextDisplay addDisplay(Player player, SpawnEntity<TextDisplay> display, int index) {
        List<TextDisplay> displays = getPlayerItemDisplay(player);

        removeDisplay(player, index);

        display.spwan();
        player.addPassenger(display.getEntity());
        displays.set(index, display.getEntity());
        return display.getEntity();
    }

    private static void removeDisplay(Player player, int index) {
        List<TextDisplay> displays = getPlayerItemDisplay(player);
        TextDisplay display = displays.get(index);

        if (display == null)
            return;
            
        player.removePassenger(display);
        display.remove();
        displays.set(index, null);
    }


    private static TextDisplay getDisplay(Player player, int index) {
        List<TextDisplay> displays = getPlayerItemDisplay(player);
        TextDisplay display = displays.get(index);

        if (display != null && !display.isValid()) {
            removeDisplay(player, index);
            return null;
        }

        return display;
    }


    public static TextDisplay getPlayerRingDisplay(Player player) {
        TextDisplay display = getDisplay(player, 0);

        if (display == null)
            display = addDisplay(player, new RingEntity(player.getLocation()), 0);
        
        return display;
    }


    public static TextDisplay getPlayerHealthDisplay(Player player) {
        TextDisplay display = getDisplay(player, 1);

        if (display == null)
            display = addDisplay(player, new HealthRingEntity(player.getLocation(), 0), 1);
        
        return display;
    }


    public static TextDisplay getPlayerPostureDisplay(Player player) {
        TextDisplay display = getDisplay(player, 1);

        if (display == null)
            display = addDisplay(player, new PostureRingEntity(player.getLocation(), 0), 2);
        
        return display;
    }


    public static void removePlayerItemDisplay(Player player) {
        removeDisplay(player, 0);
        removeDisplay(player, 1);
        removeDisplay(player, 2);
    }


    public static void downPlayerSlash(Player player) {
        int sem = player_slash_semaphore.getOrDefault(player.getUniqueId(), 0);
        ++sem;
        player_slash_semaphore.put(player.getUniqueId(), sem);
    }

    public static boolean upPlayerSlash(Player player) {
        int sem = player_slash_semaphore.getOrDefault(player.getUniqueId(), 0);
        if (sem == 0)
            return false;
        
        --sem;
        player_slash_semaphore.put(player.getUniqueId(), sem);
        return true;
    }

    public static void downPlayerDropItem(Player player) {
        int sem = player_drop_item_semaphore.getOrDefault(player.getUniqueId(), 0);
        ++sem;
        player_drop_item_semaphore.put(player.getUniqueId(), sem);
    }

    public static boolean upPlayerDropItem(Player player) {
        int sem = player_drop_item_semaphore.getOrDefault(player.getUniqueId(), 0);
        if (sem == 0)
            return false;
        
        --sem;
        player_drop_item_semaphore.put(player.getUniqueId(), sem);
        return true;
    }

    public static void downPlayerDeadByPlugin(Player player, Player target) {
        player_dead_by_plugin_mapper.put(player.getUniqueId(), target);
    }

    public static boolean isPlayerDeadByPlugin(Player player) {
        return player_dead_by_plugin_mapper.containsKey(player.getUniqueId());
    }

    public static Player upPlayerDeadByPlugin(Player player) {
        return player_dead_by_plugin_mapper.remove(player.getUniqueId());
    }
}
