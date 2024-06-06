package FunctionBus;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import Assert.Entity.DeadAnimationEntity;
import Assert.Font.FontDatabase;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;
import net.kyori.adventure.text.Component;

public class PlayerDeathEventBus {
    public static void onBusTrigger(PlayerDeathEvent event) {
        PlayerDataBus.removePlayerItemDisplay(event.getPlayer());
    }

    public static boolean isPlayerDeadByPlugin(PlayerDeathEvent event) {
        if (PlayerDataBus.isPlayerDeadByPlugin(event.getPlayer()))
            return true;
        
        return false;
    }

    public static void onPlayerDeadByPlugin(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Player killer = PlayerDataBus.upPlayerDeadByPlugin(player);
        DeadAnimationEntity entity = new DeadAnimationEntity(player.getLocation());
        PlayerStateMachineSchedule.recoverHealth(killer, 3);
        PlayerStateMachineSchedule.recoverPosture(killer, 3);
        event.deathMessage(Component.text(String.format("%s 击杀了 %s", killer.getName(), player.getName())));

        PlayerStateMachineSchedule.getPlayerState(killer).main_title = String.valueOf(FontDatabase.STATUS_MAINTITLE_KILL);

        ServerBus.playServerSound(player.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1f, 0.1f);

        entity.spwan();
        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            Entity e = (Entity) args[1];
            e.remove();
            PlayerStateMachineSchedule.getPlayerState(killer).main_title = "";
        }, 40,killer , entity.getEntity());
    }
}
