package Task.AttackTask;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import Assert.Entity.BattleFlagEntity;
import FunctionBus.ServerBus;
import Task.ModelTask.ItemDisplayAnimationTask;

public class BattleFlagTask implements Runnable {
    private Location location;
    private int tick = 0;

    private Vector vec = new Vector(5, 0, 0);

    public BattleFlagTask(Location location, int duration) {        
        this.location = location.clone();
        this.tick = duration;

        this.location.setDirection(new Vector(1, 0, 0));
        ItemDisplayAnimationTask.execute(new BattleFlagEntity(location), duration);
    }

    @Override
    public void run() {
        if (tick == 0)
            return;

        for (int i = 0; i < 2; i++) {
            vec.rotateAroundY(Math.PI / 36);
            ServerBus.spawnServerParticle(Particle.FIREWORKS_SPARK, ServerBus.toLocalCoordinates(location, vec), 1, 0, 0, 0, 0);
        }
        
        --tick;
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
    }
    
}
