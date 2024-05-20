package FunctionBus;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Config.PlayerConfig;
import Assert.Config.State;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;

public class PlayerRespawnEventBus {
    public static void onBusTrigger(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        state.posture = PlayerConfig.MAX_POSTURE;
        state.health = PlayerConfig.MAX_HEALTH;

        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            PlayerDataBus.addPlayerItemDisplay(p);
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
        }, 1, player);
    }
}
