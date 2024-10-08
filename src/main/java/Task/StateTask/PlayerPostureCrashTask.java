package Task.StateTask;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Font.FontDatabase;
import Assert.Item.Sword;
import ConfigBus.ConfigBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;


public class PlayerPostureCrashTask extends BaseStateTask {
    private Player player;
    private int tick = 0;

    public PlayerPostureCrashTask(Player player) {
        this.player = player;

        player.setCooldown(Material.SHIELD, 500000);
        player.completeUsingActiveItem();

        PlayerUISchedule.setPlayerMainRing(player, FontDatabase.STATUS_RING_POSTURE_CRASH);
        PlayerUISchedule.setPlayerMainSubtitle(player, FontDatabase.STATUS_SUBTITLE_POSTURE_CRASH);
    }

    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        return;
    }

    @Override
    public void run() {
        if (tick == 30) {
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(3)));
            PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
            PlayerStateMachineSchedule.getPlayerState(player).posture = ConfigBus.getValue("max_posture", Integer.class);
        }
        
        PlayerBus.banPlayerJump(player, 3);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 3, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 3, 5, false, false));
        ServerBus.spawnServerDustParticle(player.getLocation(), 3, 1, 1, 1, new DustOptions(Color.YELLOW, 3));
        ++tick;
    }
}
