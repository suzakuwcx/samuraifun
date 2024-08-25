package Task.AttackTask;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Assert.Entity.BattleFlagEntity;
import Assert.Entity.SpawnEntity;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.ModelTask.ItemDisplayAnimationTask;

public class BattleFlagTask implements Runnable {
    private Location location;
    private int tick = 0;

    private Vector vec = new Vector(5, 0, 0);

    public BattleFlagTask(Location location, int duration) {        
        this.location = location.clone();
        this.tick = duration;

        this.location.setDirection(new Vector(1, 0, 0));

        Location flag_location = location.clone();
        flag_location.add(0, 1, 0);
        flag_location.setDirection(new Vector(0, -1, 0));

        SpawnEntity<ItemDisplay> entity = new BattleFlagEntity(flag_location);
        ItemDisplayAnimationTask.execute(entity, duration);
    }

    @Override
    public void run() {
        if (tick == 0)
            return;

        for (int i = 0; i < 2; i++) {
            vec.rotateAroundY(Math.PI / 36);
            ServerBus.spawnServerParticle(Particle.FIREWORK, ServerBus.toLocalCoordinates(location, vec), 1, 0, 0, 0, 0);
        }

        if (tick % 80 == 0) {
            for (Player player : ServerBus.getNearbyEntities(location, 5, 5, 5, EntityType.PLAYER, Player.class)) {
                if (player.getLocation().distance(location) <= 5)
                    PlayerStateMachineSchedule.recoverPosture(player, 1);
            }
        }
        
        --tick;
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
    }
    
}
