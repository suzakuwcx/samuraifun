package Task.StateTask;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import Assert.Config.State;
import Assert.Font.FontDatabase;
import Assert.Item.Sword;
import FunctionBus.EntityBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;
import Task.MonitorTask;

public class ThrushAttackStateTask extends BaseStateTask {
    private Player player;

    private Player target;
    private Vector direction;

    private int tick = 0;

    public ThrushAttackStateTask(Player player) {
        this.player = player;

        /* Disable using shield */
        PlayerBus.disableShield(player, 500000);

        PlayerUISchedule.setPlayerMainRing(player, FontDatabase.STATUS_RING_THRUST_ATTACK_WARNING);
        PlayerUISchedule.setPlayerMainSubtitle(player, FontDatabase.STATUS_SUBTITLE_THRUST_ATTACK_WARNING);
    }

    private void playDashSound() {
        ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1f, 0.8f);
        ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1f, 1f);
    }

    private void onNoTarget() {
        if (tick == 4) {
            playDashSound();
            player.setVelocity(direction.clone().multiply(new Vector(ServerBus.getDistanceVelocity(2), 0, ServerBus.getDistanceVelocity(2))));
        } else if (tick == 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(25)), 0, 3, 6);
        } else if (tick == 10) {
            PlayerBus.setPlayerInventoryList(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(3)), 0, 3, 6);
            PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
        }
    }

    private void onTarget() {
        if (tick == 4) {
            if (EntityBus.getTargetDistance(player, target) < 3)
                player.setVelocity(direction.clone().multiply(new Vector(ServerBus.getDistanceVelocity(2), 0, ServerBus.getDistanceVelocity(2))));
            else
                player.setVelocity(direction.clone().multiply(new Vector(ServerBus.getDistanceVelocity(6), 0, ServerBus.getDistanceVelocity(6))));

            playDashSound();

            MonitorTask.execute("ThrushAttackStateTask")
            .setMaxTick(10)
            .setConditionFunction((args, tick) -> {
                Player p1 = (Player) args[0];
                Player t1 = (Player) args[1];

                State state = PlayerStateMachineSchedule.getPlayerState(p1);
                state.is_invincible_frame = true;
                if (EntityBus.getTargetDistance(p1, t1) < 1.5) {
                    return true;
                } else {
                    return false;
                }
            }, player, target)
            .setTargetFunction((args, tick) -> {
                Player p2 = (Player) args[0];
                Player t2 = (Player) args[1];

                p2.setVelocity(new Vector(0, 0, 0));
                t2.setVelocity(direction.clone().multiply(new Vector(ServerBus.getDistanceVelocity(6), 0, ServerBus.getDistanceVelocity(6))));

                State state = PlayerStateMachineSchedule.getPlayerState(p2);
                state.is_invincible_frame = false;

                ServerBus.spawnServerParticle(Particle.GUST, ServerBus.getDirectionLocation(p2.getLocation().add(0, 1, 0), player.getEyeLocation().getDirection().setY(0), 2), 1, 0, 0, 0);
                ServerBus.playServerSound(p2.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 1f);
                ServerBus.playServerSound(p2.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1f, 0.8f);
                ServerBus.playServerSound(p2.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 0.5f);
                
                PlayerBus.setPlayerInventoryList(t2, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(23)), 0, 3, 6);
                PlayerStateMachineSchedule.setStateTask(t2, new PlayerStunTask(t2));

                PlayerBus.setPlayerInventoryList(p2, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(3)), 0, 3, 6);
                PlayerStateMachineSchedule.setStateTask(p2, new BattleStateTask(p2));
            }, player, target)
            .setTimeoutFunction((args) -> {
                Player p3 = (Player) args[0];
                PlayerBus.setPlayerInventoryList(p3, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(3)), 0, 3, 6);
                PlayerStateMachineSchedule.setStateTask(p3, new BattleStateTask(p3));

                State state = PlayerStateMachineSchedule.getPlayerState(p3);
                state.is_invincible_frame = false;
            }, player)
            .execute();
        }
    }

    @Override
    public void run() {
        if (tick == 0) {
            PlayerBus.setPlayerInventoryList(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(24)), 0, 3, 6);
            direction = player.getEyeLocation().getDirection().setY(0).normalize();
            RayTraceResult result = ServerBus.rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 6, 0.6, EntityType.PLAYER, player.getUniqueId());
            if (result != null)
                target = (Player) result.getHitEntity();
        }
        
        if (target == null)
            onNoTarget();
        else
            onTarget();

        ++tick;
    }
}
