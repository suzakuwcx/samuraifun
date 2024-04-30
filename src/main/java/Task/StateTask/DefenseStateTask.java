package Task.StateTask;

import org.bukkit.entity.Player;

import Schedule.PlayerStateMachineSchedule;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;

public class DefenseStateTask extends BaseStateTask {
    private Player player;

    private int tick = 0;

    public DefenseStateTask(Player player) {
        this.player = player;
    }

    @Override
    public void onPlayerStopUsingItemEvent(PlayerStopUsingItemEvent event) {
        PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new BattleStateTask(event.getPlayer()));
    }

    @Override
    public void run() {
        player.sendMessage("防御");
    }
}
