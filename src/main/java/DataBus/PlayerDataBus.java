package DataBus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import Assert.Entity.BloodRingEntity;
import Assert.Entity.PostureRingEntity;
import Assert.Entity.RingEntity;
import Assert.Entity.SpawnEntity;

public class PlayerDataBus {
    private static Map<Player, List<TextDisplay>> item_display_mapper = new HashMap<>();
    private static Map<Player, Integer> player_slash_semaphore = new HashMap<>();
    private static Map<Player, Integer> player_drop_item_semaphore = new HashMap<>();

    public static void addPlayerItemDisplay(Player player) {
        SpawnEntity<TextDisplay> display = new RingEntity(player.getLocation(), 0);
        SpawnEntity<TextDisplay> blood = new BloodRingEntity(player.getLocation(), 0);
        SpawnEntity<TextDisplay> posture = new PostureRingEntity(player.getLocation(), 0);
        display.spwan();
        blood.spwan();
        posture.spwan();

        item_display_mapper.put(player, Arrays.asList(display.getEntity(), blood.getEntity(), posture.getEntity()));

        player.addPassenger(display.getEntity());
        player.addPassenger(blood.getEntity());
        player.addPassenger(posture.getEntity());
    }

    public static List<TextDisplay> getPlayerItemDisplay(Player player) {
        if (!item_display_mapper.containsKey(player))
            addPlayerItemDisplay(player);

        return item_display_mapper.get(player);
    }

    public static void removePlayerItemDisplay(Player player) {
        for (TextDisplay display: item_display_mapper.remove(player)) {
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
