package Task;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import FunctionBus.ServerBus;

public class PlayerLongRightClickTask implements Runnable {
    private static Map<UUID, PlayerLongRightClickTask> task_mapper;

    public static class Builder {
        private PlayerLongRightClickTask task;
        private boolean is_execute = true;

        public Builder(Player player) {
            this(player, false);
        }

        public Builder(Player player, boolean is_execute) {
            this.is_execute = is_execute;

            if (task_mapper.containsKey(player.getUniqueId())) {
                task = task_mapper.get(player.getUniqueId());
            } else {
                task = new PlayerLongRightClickTask(player);
                task_mapper.put(player.getUniqueId(), task);
            }
        }

        public Builder setTick(int tick) {
            if (is_execute)
                return this;

            task.tick = tick;

            return this;
        }

        public Builder setCastingFunction(Consumer<Object[]> function, Object... stacks) {
            if (is_execute)
                return this;

            task.casting_function = function;
            task.casting_stacks = stacks;

            return this;
        }

        public Builder setShortPressFunction(Consumer<Object[]> function, Object... stacks) {
            if (is_execute)
                return this;

            task.short_press_function = function;
            task.short_press_stacks = stacks;

            return this;
        }

        public Builder setLongPressFunction(Consumer<Object[]> function, Object... stacks) {
            if (is_execute)
                return this;

            task.long_press_function = function;
            task.long_press_stacks = stacks;

            return this;
        }

        public void execute() {
            if (!is_execute) {
                task.tick += 4;
                Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
            } else {
                task.press();
            }
        }
    }

    private Player player;
    private int tick;

    private Object[] casting_stacks;
    private Consumer<Object[]> casting_function;

    private Object[] long_press_stacks;
    private Consumer<Object[]> long_press_function;

    private Object[] short_press_stacks;
    private Consumer<Object[]> short_press_function;

    private long last_time;

    private int period_tick_count;
    private boolean is_press;

    static {
        task_mapper = new HashMap<>();
    }

    {
        last_time = System.currentTimeMillis();
        tick = 0;
        period_tick_count = 5;
    }

    private PlayerLongRightClickTask(Player player) {
        this.player = player;
    }

    private void press() {
        long time = System.currentTimeMillis();
        if (Math.abs(time - last_time - 200) >= 10) {
            short_press();
            return;
        } else {
            is_press = true;
            last_time = time;
        }
    }

    public static Builder execute(Player player) {
        if (task_mapper.containsKey(player.getUniqueId())) {
            return new Builder(player, true);
        }

        return new Builder(player);
    }

    private void short_press() {
        task_mapper.remove(player.getUniqueId());
        if (short_press_function != null)
            short_press_function.accept(short_press_stacks);
    }

    @Override
    public void run() {
        if (casting_function != null)
            casting_function.accept(casting_stacks);

        --tick;
        --period_tick_count;

        if (period_tick_count == 0) {
            if (!is_press) {
                short_press();
                return;
            }

            period_tick_count = 5;
            is_press = false;
        }

        if (tick <= 0) {
            task_mapper.remove(player.getUniqueId());
            long_press_function.accept(long_press_stacks);
            return;
        }

        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
    }
}
