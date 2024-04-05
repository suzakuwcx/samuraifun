package FunctionBus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;

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

        if (player.isSprinting())
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
}
