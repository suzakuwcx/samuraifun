package Task.AttackTask;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import Assert.Config.State;
import Assert.Entity.SmokingDartsEntity;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;

public class SmokingDartsTask implements Runnable {
    private static final Vector scale = new Vector(3, 3, 3);
    private static final Vector gravity = new Vector(0, 0.1, 0);

    private Location location;
    private Vector direction;
    private int tick = 0;
    private int delay_tick = 40;
    private boolean is_ground = false;
    private SmokingDartsEntity entity;

    public SmokingDartsTask(Location location, int duration) {
        this.location = location;
        this.direction = location.getDirection().normalize().multiply(scale);
        this.tick = duration;

        entity = new SmokingDartsEntity(location);
        entity.spwan();
    }


    @Override
    public void run() {
        if (tick == 0) {
            entity.getEntity().remove();
            return;
        }

        if (!is_ground) {
            direction.subtract(gravity);
            RayTraceResult result = location.getWorld().rayTraceBlocks(location, direction, direction.length(), FluidCollisionMode.NEVER, true);
            if (result == null) {
                location.add(direction);
                location.setDirection(direction);
                entity.getEntity().teleport(location);
            } else {
                is_ground = true;
                Vector pos = result.getHitPosition();
                location.set(pos.getX(), pos.getY(), pos.getZ());
                entity.getEntity().teleport(location);
            }
        }

        
        if (is_ground)
            --delay_tick;
        if (delay_tick < 0) {
            --tick;
            ServerBus.spawnServerDustParticle(location, 15, 2, 2, 2, new DustOptions(Color.WHITE, 75.0f));

            if (tick % 80 == 0) {
                for (Player player : ServerBus.getNearbyEntities(location, 5, 5, 5, EntityType.PLAYER, Player.class)) {
                    if (player.getLocation().distance(location) <= 5) {
                        State state = PlayerStateMachineSchedule.getPlayerState(player);
                        state.sword_cooldown = 0;
                    }
                }
            }
        }

        
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
    }
    
}
