package Task.AttackTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Assert.Config.State;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;

public class SubTitleShowTask implements Runnable {
    private static Map<UUID, SubTitleShowTask> task_mapper;
    private Player player;
    private String text;
    private int task_id = -1;

    static {
        task_mapper = new HashMap<>();
    }

    private SubTitleShowTask(Player player, String text) {
        this.player = player;
        this.text = text;
    }

    public static void execute(Player player, String text, int tick) {
        State state = null;
        SubTitleShowTask task = null;
        UUID uuid = player.getUniqueId();

        if (task_mapper.containsKey(uuid)) {
            task = task_mapper.get(uuid);

            updateSubtitle(player, text);

            Bukkit.getScheduler().cancelTask(task.task_id);
            task.task_id = Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), task, tick).getTaskId();
            return;
        }

        state = PlayerStateMachineSchedule.getPlayerState(player);
        task = new SubTitleShowTask(player, state.sub_title);
        task.task_id = Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), task, tick).getTaskId();
        updateSubtitle(player, text);

        task_mapper.put(uuid, task);
    }

    public static void setMainSubtitle(Player player, String text) {
        if (task_mapper.containsKey(player.getUniqueId())) {
            task_mapper.get(player.getUniqueId()).text = text;
        } else {
            updateSubtitle(player, text);
        }
    }

    private static void updateSubtitle(Player player, String text) {
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        state.sub_title = text;
    }

    @Override
    public void run() {
        updateSubtitle(player, text);
        task_mapper.remove(player.getUniqueId());
    }
}
