package Task.SwipeTask;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import FunctionBus.ServerBus;

/* 
 *  particle minecraft:wax_off ~ ~2 ~ 0 0 0 0 1 
 */
public class YokogiriTask implements Runnable {
    private static Map<Player, YokogiriTask> task_mapper;
    private static final int MAX_TICK = 4;
    private static final int MAX_FRAME = 8;

    private Player player;
    private Location location;
    private double range;
    private Vector vec;
    private Vector axis = new Vector(0, 1, 0).rotateAroundZ(0).normalize();
    
    private double rotation = - 4 * Math.PI / 3;
    private int tick = MAX_TICK;

    static {
        task_mapper = new HashMap<>();
    }

    private YokogiriTask(Player player, Location location, double range) {
        this.player = player;
        this.location = location;
        this.range = range;

        vec = (new Vector(0, 0, 1).crossProduct(axis)).normalize().multiply(-range).rotateAroundAxis(axis, Math.PI / 6);
    }

    public static void execute(Player player, double range) {
        if (task_mapper.containsKey(player))
            return;

        YokogiriTask task = new YokogiriTask(player, player.getEyeLocation(), range);
        task_mapper.put(player, task);
        /* delay compensation */
        // task.location.add(PlayerRealVelocityUpdateSchedule.getVelocity(player).clone().setY(0).multiply(9));

        ServerBus.playServerSound(task.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1.5f);

        Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
    }
    
    @Override
    public void run() {
        if (tick > 0) {
            for (int i = 0; i < MAX_FRAME; ++i) {
                ServerBus.spawnServerParticle(Particle.WAX_OFF, ServerBus.toLocalCoordinates(location, vec), 1, 0, 0, 0, 0);
                vec.rotateAroundAxis(axis, rotation / MAX_TICK / MAX_FRAME);
            }
            --tick;
            Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
        } else {
            task_mapper.remove(player);
        }
    }
}
