import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import FunctionBus.ServerBus;
import Schedule.PeriodInformSchedule;

public class SchedulerBus {
    private static Plugin plugin = ServerBus.getPlugin();
    private static BukkitScheduler scler = plugin.getServer().getScheduler();

    public static void run() {
        scler.runTaskTimer(plugin, new PeriodInformSchedule(), 0, 100);
    }
}
