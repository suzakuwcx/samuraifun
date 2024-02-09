package Task.GunTask;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustTransition;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import FunctionBus.ServerBus;

public class RifleTask implements Runnable {
    private static Map<Player, RifleTask> task_mapper;
    private Player player;
    private int task_id = 0;

    static {
        task_mapper = new HashMap<>();
    }

    public RifleTask(Player player) {
        this.player = player;
    }

    private void shoot_core()
    {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
        Vector playerDirection = this.player.getLocation().getDirection();
        Location location = player.getEyeLocation().subtract(0, 0.2, 0);
        for (int index = 0; index < 30; index++) {
            location.add(playerDirection.normalize());
            player.spawnParticle(Particle.DUST_COLOR_TRANSITION, location, 1, 0f, 0f, 0f, new DustTransition(Color.AQUA, Color.BLACK, 1));
        }
    }

    @Override
    public void run() {
        shoot_core();
        PlayerPerspectiveRecoilTask.do_recoil(player);
        this.task_id = Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 2).getTaskId();
    }
    
    public static void shoot(Player player) {
        RifleTask task;
        task = task_mapper.get(player);
        if (task != null)
            return;
        task = new RifleTask(player);
        task_mapper.put(player, task);
        task.task_id = Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task).getTaskId();
    }

    public static void stop(Player player)
    {
        RifleTask task;
        task = task_mapper.get(player);
        if (task == null)
            return;
        Bukkit.getScheduler().cancelTask(task.task_id);
        task_mapper.remove(player);
    }
}
