package Task.SwipeTask;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import FunctionBus.ServerBus;

// /particle minecraft:wax_off ~ ~2 ~ 0 0 0 0 1
public class YokogiriTask implements Runnable {
    private static Map<Player, YokogiriTask> task_mapper;
    private static final int MAX_TICK = 6;

    private Player player;
    private Location location;
    private Vector vec = new Vector(1.5, 0, 0).rotateAroundY(Math.PI / 6);
    private double rotation = 4 * Math.PI / 3;
    private int tick = MAX_TICK;

    static {
        task_mapper = new HashMap<>();
    }

    public static void execute(Player player) {
        if (task_mapper.containsKey(player))
            return;

        YokogiriTask task = new YokogiriTask();
        task_mapper.put(player, task);
        task.player = player;
        task.location = player.getEyeLocation();

        Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
    }
    
    @Override
    public void run() {
        if (tick > 0) {
            ServerBus.spawnServerParticle(Particle.WAX_OFF, ServerBus.toLocalCoordinates(location, vec), 1, 0, 0, 0, 0);
            vec.rotateAroundY(- rotation / MAX_TICK / 4);
            ServerBus.spawnServerParticle(Particle.WAX_OFF, ServerBus.toLocalCoordinates(location, vec), 1, 0, 0, 0, 0);
            vec.rotateAroundY(- rotation / MAX_TICK / 4);
            ServerBus.spawnServerParticle(Particle.WAX_OFF, ServerBus.toLocalCoordinates(location, vec), 1, 0, 0, 0, 0);
            vec.rotateAroundY(- rotation / MAX_TICK / 4);
            ServerBus.spawnServerParticle(Particle.WAX_OFF, ServerBus.toLocalCoordinates(location, vec), 1, 0, 0, 0, 0);
            --tick;
            vec.rotateAroundY(- rotation / MAX_TICK / 4);
            Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
        } else {
            task_mapper.remove(player);
        }
    }
}
