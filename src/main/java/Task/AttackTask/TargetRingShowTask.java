package Task.AttackTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import DataBus.PlayerDataBus;
import FunctionBus.ServerBus;

public class TargetRingShowTask implements Runnable {
    private static Map<UUID, TargetRingShowTask> task_mapper;
    private Player host;
    private Player target;
    private int tick = 0;

    static {
        task_mapper = new HashMap<>();
    }

    private TargetRingShowTask(Player player, Player target) {
        this.host = player;
        this.target = target;

        for (TextDisplay display : PlayerDataBus.getPlayerItemDisplay(target)) {
            host.showEntity(ServerBus.getPlugin(), display);
        }
    }

    public static void execute(Player player, Player target) {
        TargetRingShowTask task = null;
        UUID uuid = player.getUniqueId();

        if (task_mapper.containsKey(uuid)) {
            task = task_mapper.get(uuid);
            task.tick = 0;
            return;
        }

        task = new TargetRingShowTask(player, target);
        task_mapper.put(uuid, task);
        
        Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
    }

    @Override
    public void run() {
        if (tick == 100) {
            for (TextDisplay display : PlayerDataBus.getPlayerItemDisplay(target)) {
                host.hideEntity(ServerBus.getPlugin(), display);
            }
            task_mapper.remove(host.getUniqueId());
            return;
        }

        ++tick;
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
    }
}
