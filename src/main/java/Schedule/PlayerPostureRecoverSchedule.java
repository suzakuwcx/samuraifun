package Schedule;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Assert.Config.PlayerConfig;
import Assert.Config.State;

public class PlayerPostureRecoverSchedule implements Runnable {

    private static void periodRecoverPosture(Player player) {
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        if (state.sword_cooldown < 3 * PlayerConfig.SWORD_COOLDOWN / 4)
            PlayerStateMachineSchedule.recoverPosture(player, 1);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            periodRecoverPosture(player);
        }
    }    
}
