package Task.StateTask;

import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import Assert.Font.FontDatabase;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import Task.AttackTask.DeflectTask;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import net.kyori.adventure.text.Component;

public class DefenseStateTask extends BaseStateTask {
    private Player player;

    private int tick = 0;

    public DefenseStateTask(Player player) {
        this.player = player;

        DeflectTask.execute(player);
        TextDisplay display = PlayerDataBus.getPlayerRingDisplay(player);
        
        if (display != null)
            display.text(Component.text(FontDatabase.STATUS_RING_DEFENSE));
    }

    @Override
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (StateEventBus.isPlayerDash(event)) {
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
