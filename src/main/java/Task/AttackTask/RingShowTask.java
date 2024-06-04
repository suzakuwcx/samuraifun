package Task.AttackTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import Assert.Config.State;
import DataBus.PlayerDataBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import net.kyori.adventure.text.Component;

public class RingShowTask implements Runnable {
    private static Map<UUID, RingShowTask> task_mapper;
    private Player player;
    private int task_id = -1;

    static {
        task_mapper = new HashMap<>();
    }

    private RingShowTask(Player player) {
        this.player = player;
    }

    public static void execute(Player player, String text, int tick) {
        RingShowTask task = null;
        UUID uuid = player.getUniqueId();

        if (task_mapper.containsKey(uuid)) {
            task = task_mapper.get(uuid);

            updateRing(player, text);

            Bukkit.getScheduler().cancelTask(task.task_id);
            task.task_id = Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), task, tick).getTaskId();
            return;
        }

        task = new RingShowTask(player);
        task.task_id = Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), task, tick).getTaskId();
        updateRing(player, text);

        task_mapper.put(uuid, task);
    }

    public static boolean isPlayerShowingSideRing(Player player) {
        return task_mapper.containsKey(player.getUniqueId());
    }

    private static void updateRing(Player player, String text) {
        TextDisplay display = PlayerDataBus.getPlayerRingDisplay(player);
        display.text(Component.text(text));
    }

    @Override
    public void run() {
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        updateRing(player, state.status_ring);
        task_mapper.remove(player.getUniqueId());
    }
}
