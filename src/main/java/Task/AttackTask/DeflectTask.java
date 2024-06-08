package Task.AttackTask;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Assert.Font.FontDatabase;
import DataBus.ConfigBus;
import FunctionBus.ServerBus;
import Schedule.PlayerUISchedule;

public class DeflectTask implements Runnable{
    private static Map<Player, DeflectTask> task_mapper;
    private static final int DEFLECT_TICK = ConfigBus.getValue("deflect_tick", Integer.class);

    private Player player;
    private int tick = 0;
    private int failure = 0;

    static {
        task_mapper = new HashMap<>();
    }
    
    public static boolean isPlayerDefense(Player player) {
        if (!task_mapper.containsKey(player))
            return false;

        DeflectTask task = task_mapper.get(player);
        if (task.failure > 3)
            return false;

        return true;
    }

    public static boolean isPlayerDeflect(Player player) {
        if (!task_mapper.containsKey(player))
            return false;

        DeflectTask task = task_mapper.get(player);
        if (task.failure >= 1)
            return false;

        return true;
    }


    public static boolean isPlayerFakeDeflect(Player player) {
        if (!task_mapper.containsKey(player))
            return false;

        DeflectTask task = task_mapper.get(player);
        if (task.failure > 3 || task.failure < 1)
            return false;

        return true;
    }

    public static void execute(Player player) {
        DeflectTask task = null;

        if (task_mapper.containsKey(player)) {
            task = task_mapper.get(player);
            ++task.failure;
            task.tick = 0;
            return;
        }

        task = new DeflectTask();
        task_mapper.put(player, task);
        task.player = player;

        PlayerUISchedule.setPlayerSideRing(player, FontDatabase.STATUS_RING_CAN_DEFLECT, DEFLECT_TICK);
        PlayerUISchedule.setPlayerSideSubtitle(player, FontDatabase.STATUS_SUBTITLE_CAN_DEFLECT, DEFLECT_TICK);
        Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
    }

    @Override
    public void run() {
        if (tick == 2 * DEFLECT_TICK) {
            task_mapper.remove(player);
            return;
        }

        ++tick;
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
    }
}
