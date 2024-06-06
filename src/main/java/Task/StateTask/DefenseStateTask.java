package Task.StateTask;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import Assert.Font.FontDatabase;
import FunctionBus.ServerBus;
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
        if (DeflectTask.isPlayerDefense(player) || DeflectTask.isPlayerFakeDeflect(player))
            PlayerUISchedule.setPlayerMainSubtitle(player, FontDatabase.STATUS_SUBTITLE_DEFENSE);
        else
            PlayerUISchedule.setPlayerMainSubtitle(player, "");

        ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.8f);
        PlayerUISchedule.setPlayerMainRing(player, FontDatabase.STATUS_RING_DEFENSE);
    }

    @Override
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (PlayerStateMachineSchedule.isPlayerNoPosture(event.getPlayer())) {
            PlayerUISchedule.setPlayerSideSubtitle(player, FontDatabase.STATUS_SUBTITLE_NO_POSTURE, 4);
        } else if (StateEventBus.isPlayerDash(event)) {
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
