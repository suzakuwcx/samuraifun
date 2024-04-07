package DataBus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

import FunctionBus.ServerBus;

public class PlayerDataBus {
    private static Map<Player, ItemDisplay> item_display_mapper = new HashMap<>();
    private static Map<Player, Integer> player_slash_semaphore = new HashMap<>();

    public static void addPlayerItemDisplay(Player player) {
        ItemDisplay display = (ItemDisplay) ServerBus.spawnServerEntity(player.getLocation(), EntityType.ITEM_DISPLAY, false);
        display.setItemStack(new ItemStack(Material.GOLDEN_SWORD));
        display.setRotation(player.getYaw(), 0);

        Transformation transformation = display.getTransformation();
        transformation.getTranslation().set(0, -0.6875, -0.375);
        transformation.getLeftRotation().set(0f, 0f, 0.9659242f, 0.2588251f);
        transformation.getRightRotation().set(0f, 0f, 0f, 1f);
        transformation.getScale().set(0.75, 0.75, 0.5625);
        display.setTransformation(transformation);

        item_display_mapper.put(player, display);

        player.addPassenger(display);
    }

    public static ItemDisplay getPlayerItemDisplay(Player player) {
        if (!item_display_mapper.containsKey(player))
            addPlayerItemDisplay(player);

        return item_display_mapper.get(player);
    }

    public static void removePlayerItemDisplay(Player player) {
        ItemDisplay display = item_display_mapper.remove(player);
        player.removePassenger(display);
        display.remove();
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
}
