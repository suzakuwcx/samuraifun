import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import FunctionBus.ServerBus;
import Schedule.PlayerRealVelocityUpdateSchedule;
import Schedule.PostureBarUpdateSchedule;

public class SchedulerBus {
    private static Plugin plugin = ServerBus.getPlugin();
    private static BukkitScheduler scler = plugin.getServer().getScheduler();

    public static void run() {
        scler.runTaskTimer(plugin, new PlayerRealVelocityUpdateSchedule(), 0, 3);
        scler.runTaskTimer(plugin, new PostureBarUpdateSchedule(), 0, 1);
    }
}
