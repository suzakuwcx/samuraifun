package Task.StateTask;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import Assert.Font.FontDatabase;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;
import Task.AttackTask.DeflectTask;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;

public class DefenseStateTask extends BaseStateTask {
    private Player player;

    private int tick = 0;

    public DefenseStateTask(Player player) {
        this.player = player;

        DeflectTask.execute(player);
        
        PlayerUISchedule.setPlayerMainRing(player, FontDatabase.STATUS_RING_DEFENSE);
        PlayerUISchedule.setPlayerMainSubtitle(player, FontDatabase.STATUS_SUBTITLE_DEFENSE);        
    }

    @Override
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (StateEventBus.isPlayerDash(event) && !PlayerStateMachineSchedule.isPlayerNoPosture(event.getPlayer())) {
            PlayerStateMachineSchedule.damagePosture(player, 1, false);
            PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new ThrushAttackStateTask(event.getPlayer()));
        }
    }

    @Override
    public void onPlayerStopUsingItemEvent(PlayerStopUsingItemEvent event) {
        PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new StopDefenseStunTask(event.getPlayer()));
    }

    @Override
    public void run() {
    }
}
