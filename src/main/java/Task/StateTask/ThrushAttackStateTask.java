package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import Assert.Font.FontDatabase;
import Assert.Item.Sword;
import FunctionBus.EntityBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;

public class ThrushAttackStateTask extends BaseStateTask {
    private Player player;

    private Player target;
    private Vector direction;

    private int tick = 0;

    public ThrushAttackStateTask(Player player) {
        this.player = player;

        player.setCooldown(Material.SHIELD, 500000);
        /* Disable using shield */
        player.completeUsingActiveItem();

        PlayerUISchedule.setPlayerMainRing(player, FontDatabase.STATUS_RING_THRUST_ATTACK_WARNING);
    }

    @Override
    public void run() {
        if (tick == 0) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1024), 0, 3, 6);
            direction = player.getEyeLocation().getDirection().setY(0).normalize();
            RayTraceResult result = ServerBus.rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 6, 0.3, EntityType.PLAYER, player.getUniqueId());
            if (result != null)
                target = (Player) result.getHitEntity();
        } else if (tick == 4) {
            if (target != null) {
                double distance = EntityBus.getTargetDistance(player, target) - 0.3;
                player.setVelocity(direction.multiply(new Vector(ServerBus.getDistanceVelocity(distance), 0, ServerBus.getDistanceVelocity(distance))));
            } else {
                player.setVelocity(direction.multiply(new Vector(ServerBus.getDistanceVelocity(2), 0, ServerBus.getDistanceVelocity(2))));
            }
        } else if (tick == 8) {
            /* Successfully hit */
            if (target != null) {
                target.setVelocity(direction.multiply(new Vector(ServerBus.getDistanceVelocity(2), 0, ServerBus.getDistanceVelocity(2))));
                PlayerStateMachineSchedule.setStateTask(target, new PlayerStunTask(target));
            }
        } else if (tick == 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1025), 0, 3, 6);
        } else if (tick == 10) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
            PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
        }

        ++tick;
    }
}
