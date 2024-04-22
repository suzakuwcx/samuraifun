package FunctionBus;

import org.bukkit.event.player.PlayerJoinEvent;

import Assert.Config.PlayerConfig;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;

public class PlayerJoinEventBus {
    public static void onBusTrigger(PlayerJoinEvent event) {
        PlayerDataBus.addPlayerItemDisplay(event.getPlayer());
        event.getPlayer().setShieldBlockingDelay(PlayerConfig.DEFLECT_TICK);

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
