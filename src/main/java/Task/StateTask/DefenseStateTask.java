package Task.StateTask;

import org.bukkit.entity.Player;

import Schedule.PlayerStateMachineSchedule;
import Task.AttackTask.DeflectTask;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;

public class DefenseStateTask extends BaseStateTask {
    private Player player;

    private int tick = 0;

    public DefenseStateTask(Player player) {
        this.player = player;

        DeflectTask.execute(player);
    }


    @Override
    public void onPlayerStopUsingItemEvent(PlayerStopUsingItemEvent event) {
        PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new BattleStateTask(event.getPlayer()));
    }

    @Override
    public void run() {
    }
}
