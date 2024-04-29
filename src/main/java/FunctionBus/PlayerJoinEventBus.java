package FunctionBus;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Config.PlayerConfig;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;

public class PlayerJoinEventBus {
    public static void onBusTrigger(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerDataBus.addPlayerItemDisplay(player);
        player.setShieldBlockingDelay(PlayerConfig.DEFLECT_TICK);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 40));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 40));

        PlayerStateMachineSchedule.init(event.getPlayer());
    }

    public static void onBusComplete(PlayerJoinEvent event) {
        
    }

    public static boolean isPlayerOperator(PlayerJoinEvent event) {
        if (!event.getPlayer().isOp())
            return false;
        
        return true;
    }

    public static void onPlayerOperator(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Helloworld");
    }

 
}
