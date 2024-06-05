package Task.StateTask;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

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

    private void onNoTarget() {
        if (tick == 4) {
            player.setVelocity(direction.clone().multiply(new Vector(ServerBus.getDistanceVelocity(2), 0, ServerBus.getDistanceVelocity(2))));
        } else if (tick == 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1025), 0, 3, 6);
        } else if (tick == 10) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
            PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
        }
    }

    private void onTarget() {
        if (tick == 4) {
            if (EntityBus.getTargetDistance(player, target) < 3)
                player.setVelocity(direction.clone().multiply(new Vector(ServerBus.getDistanceVelocity(2), 0, ServerBus.getDistanceVelocity(2))));
            else
                player.setVelocity(direction.clone().multiply(new Vector(ServerBus.getDistanceVelocity(6), 0, ServerBus.getDistanceVelocity(6))));

            MonitorTask.execute("ThrushAttackStateTask")
            .setMaxTick(40)
            .setConditionFunction((args, tick) -> {
                Player p1 = (Player) args[0];
                Player t1 = (Player) args[1];
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
                t2.setVelocity(direction.clone().multiply(new Vector(ServerBus.getDistanceVelocity(3), 0, ServerBus.getDistanceVelocity(3))));
                
                PlayerBus.setPlayerInventoryList(target, new Sword(1023), 0, 3, 6);
                PlayerStateMachineSchedule.setStateTask(target, new PlayerStunTask(target));

                PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
                PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
            }, player, target)
            .execute();
        }

        /* For safety */
        if (tick == 40) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
            PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
        }
    }

    @Override
    public void run() {
        if (tick == 0) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1024), 0, 3, 6);
            direction = player.getEyeLocation().getDirection().setY(0).normalize();
            RayTraceResult result = ServerBus.rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 6, 0.3, EntityType.PLAYER, player.getUniqueId());
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
