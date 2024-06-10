package FunctionBus;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Config.State;
import DataBus.ConfigBus;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;
import Task.GameTask.GameTask;

public class PlayerRespawnEventBus {
    public static void onBusTrigger(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        state.posture = ConfigBus.getValue("max_posture", Integer.class);
        state.health = ConfigBus.getValue("max_health", Integer.class);

        if (GameTask.isInGame()) {
            PlayerBus.toGhost(player);
        } else {
            PlayerBus.resetPlayerGame(player);
            DelayTask.execute((args) -> {
                PlayerDataBus.addPlayerItemDisplay((Player) args[0]);
            }, 1, player);

        }
        
        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, PotionEffect.INFINITE_DURATION, 3, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION, 3, false, false));
        }, 1, player);
    }
}
