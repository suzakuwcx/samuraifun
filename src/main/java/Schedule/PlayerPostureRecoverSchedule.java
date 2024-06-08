package Schedule;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Assert.Config.State;
import DataBus.ConfigBus;

public class PlayerPostureRecoverSchedule implements Runnable {

    private static void periodRecoverPosture(Player player) {
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        if (state.sword_cooldown < 3 * ConfigBus.getValue("sword_cooldown", Integer.class) / 4)
            PlayerStateMachineSchedule.recoverPosture(player, 1);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            periodRecoverPosture(player);
        }
    }    
}
