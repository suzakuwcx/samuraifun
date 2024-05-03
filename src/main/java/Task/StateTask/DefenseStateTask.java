package Task.StateTask;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

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
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (StateEventBus.isPlayerPrepareDash(event)) {
            StateEventBus.onPlayerPrepareDash(event);
        } else if (StateEventBus.isPlayerDash(event)) {
            PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new ThrushAttackStateTask(event.getPlayer()));
        }
    }

    @Override
    public void onPlayerStopUsingItemEvent(PlayerStopUsingItemEvent event) {
        PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new BattleStateTask(event.getPlayer()));
    }

    @Override
    public void run() {
    }
}
