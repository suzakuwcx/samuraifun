package FunctionBus;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.joml.Math;

import Task.DelayTask;
import Task.AttackTask.DashEffectTask;

public class PlayerToggleSneakEventBus {
    public static boolean isPlayerPrepareDash(PlayerToggleSneakEvent event) {
        if (!event.isSneaking())
            return false;

        if (Math.abs(event.getPlayer().getVelocity().getY() + 0.0784) > 0.00001)
            return false;

        return true;
    }

    public static void onPlayerPrepareDash(PlayerToggleSneakEvent event) {
        DashEffectTask.execute(event.getPlayer());
    }

    public static boolean isPlayerDash(PlayerToggleSneakEvent event) {
        if (event.isSneaking())
            return false;

        if (Math.abs(event.getPlayer().getVelocity().getY() + 0.0784) > 0.00001)
            return false;

        return true;
    }

    public static void onPlayerDash(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();
        DashEffectTask.stop(p);
        DelayTask.execute((args) -> {
            Player player = (Player) args[0];
            Location location = player.getLocation();
            Vector direction = location.getDirection();
            Vector vec = (location.toVector().subtract((Vector) args[1])).setY(0);
            float angle = vec.angle(direction);
            if (vec.lengthSquared() != 0) {
                ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1f, 0.8f);
                ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1f, 1f);
                /* decrease forward dash value */
                if (angle < Math.PI / 2)
                    player.setVelocity(vec.normalize().multiply(0.5 + Math.sin(angle)));
                else
                    player.setVelocity(vec.normalize().multiply(1));
            }
        }, 2, p, p.getLocation().toVector());
    }
}
