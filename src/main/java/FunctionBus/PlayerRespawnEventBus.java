package FunctionBus;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import DataBus.PlayerDataBus;
import Task.DelayTask;

public class PlayerRespawnEventBus {
    public static void onBusTrigger(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerDataBus.addPlayerItemDisplay(player);

        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
        }, 1, player);
    }
}
