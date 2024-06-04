package Task.StateTask;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import Assert.Font.FontDatabase;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;

public class StopDefenseStunTask extends BaseStateTask {
    private Player player;
    private int tick = 0;

    public StopDefenseStunTask(Player player) {
        this.player = player;

        PlayerUISchedule.setPlayerMainRing(player, FontDatabase.STATUS_RING_STOP_DEFENSE);
    }

    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (StateEventBus.isPlayerDefense(event)) {
            StateEventBus.onPlayerDefense(event);
        }
    }

    @Override
    public void run() {
        if (tick == 2) {
            PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
        }

        ++tick;
    }
}
