package DataBus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import Assert.Entity.HealthRingEntity;
import Assert.Entity.PostureRingEntity;
import Assert.Entity.RingEntity;
import Assert.Entity.SpawnEntity;
import FunctionBus.ServerBus;

public class PlayerDataBus {
    private static Map<Player, List<TextDisplay>> item_display_mapper = new HashMap<>();
    private static Map<Player, Integer> player_slash_semaphore = new HashMap<>();
    private static Map<Player, Integer> player_drop_item_semaphore = new HashMap<>();

    public static void addPlayerItemDisplay(Player player) {
        SpawnEntity<TextDisplay> display = new RingEntity(player.getLocation());
        SpawnEntity<TextDisplay> blood = new HealthRingEntity(player.getLocation(), 0);
        SpawnEntity<TextDisplay> posture = new PostureRingEntity(player.getLocation(), 0);
        display.spwan();
        blood.spwan();
        posture.spwan();

        item_display_mapper.put(player, Arrays.asList(display.getEntity(), blood.getEntity(), posture.getEntity()));

        player.addPassenger(display.getEntity());
        ServerBus.playerHideServerEntity(player, display.getEntity());

        player.addPassenger(blood.getEntity());
        ServerBus.playerHideServerEntity(player, blood.getEntity());

        player.addPassenger(posture.getEntity());
        ServerBus.playerHideServerEntity(player, posture.getEntity());
    }

    public static List<TextDisplay> getPlayerItemDisplay(Player player) {
        return item_display_mapper.get(player);
    }

    public static TextDisplay getPlayerRingDisplay(Player player) {
        List<TextDisplay> display = getPlayerItemDisplay(player);
        if (display == null)
            return null;
        else
            return display.get(0);
    }

    public static TextDisplay getPlayerHealthDisplay(Player player) {
        List<TextDisplay> display = getPlayerItemDisplay(player);
        if (display == null)
            return null;
        else
            return display.get(1);
    }

    public static TextDisplay getPlayerPostureDisplay(Player player) {
        List<TextDisplay> display = getPlayerItemDisplay(player);
        if (display == null)
            return null;
        else
            return display.get(2);
    }

    public static void removePlayerItemDisplay(Player player) {
        List<TextDisplay> displays = getPlayerItemDisplay(player);
        if (displays == null)
            return;

        for (TextDisplay display: displays) {
            player.removePassenger(display);
            display.remove();
        }
    }

    public static void downPlayerSlash(Player player) {
        int sem = player_slash_semaphore.getOrDefault(player, 0);
        ++sem;
        player_slash_semaphore.put(player, sem);
    }

    public static boolean upPlayerSlash(Player player) {
        int sem = player_slash_semaphore.getOrDefault(player, 0);
        if (sem == 0)
            return false;
        
        --sem;
        player_slash_semaphore.put(player, sem);
        return true;
    }

    public static void downPlayerDropItem(Player player) {
        int sem = player_drop_item_semaphore.getOrDefault(player, 0);
        ++sem;
        player_drop_item_semaphore.put(player, sem);
    }

    public static boolean upPlayerDropItem(Player player) {
        int sem = player_drop_item_semaphore.getOrDefault(player, 0);
        if (sem == 0)
            return false;
        
        --sem;
        player_drop_item_semaphore.put(player, sem);
        return true;
    }
}
