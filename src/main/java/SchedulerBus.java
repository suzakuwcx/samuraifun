import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import FunctionBus.ServerBus;
import Schedule.PlayerPostureRecoverSchedule;
import Schedule.PlayerRealVelocityUpdateSchedule;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;

public class SchedulerBus {
    private static Plugin plugin = ServerBus.getPlugin();
    private static BukkitScheduler scler = plugin.getServer().getScheduler();

    public static void run() {
        scler.runTaskTimer(plugin, new PlayerRealVelocityUpdateSchedule(), 0, 3);
        scler.runTaskTimer(plugin, new PlayerStateMachineSchedule(), 0, 1);
        scler.runTaskTimer(plugin, new PlayerUISchedule(), 0, 1);
        scler.runTaskTimer(plugin, new PlayerPostureRecoverSchedule(), 0, 60);
    }
}
