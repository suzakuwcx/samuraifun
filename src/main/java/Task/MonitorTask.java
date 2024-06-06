package Task;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.bukkit.Bukkit;

import FunctionBus.ServerBus;

public class MonitorTask implements Runnable {
    private static Map<String, MonitorTask> task_mapper;

    private int tick;
    private int max_tick;
    private String key;

    private Object[] condition_stacks;
    private BiFunction<Object[], Integer, Boolean> condition_function;

    private Object[] target_stacks;
    private BiConsumer<Object[], Integer> target_function;

    private Object[] timeout_stacks;
    private Consumer<Object[]> timeout_function;

    static {
        task_mapper = new HashMap<>();
    }

    {
        tick = 0;
        max_tick = -1;
        timeout_function = null;
    }

    public static class Builder {
        private MonitorTask task;
        private boolean is_execute = true;
        
        public Builder(String key) {
            this(key, false);
        }

        public Builder(String key, boolean is_execute) {
            this.is_execute = is_execute;

            if (task_mapper.containsKey(key)) {
                this.task = task_mapper.get(key);
            } else {
                this.task = new MonitorTask();
                task.key = key;
                task_mapper.put(key, this.task);
            }
        }

        public Builder setConditionFunction(BiFunction<Object[], Integer, Boolean> function, Object... stacks) {
            if (is_execute)
                return this;
            
            task.condition_stacks = stacks;
            task.condition_function = function;
            return this;
        }

        public Builder setTargetFunction(BiConsumer<Object[], Integer> function, Object... stacks) {
            if (is_execute)
                return this;
            
            task.target_stacks = stacks;
            task.target_function = function;
            return this;
        }

        public Builder setMaxTick(int tick) {
            task.max_tick = tick;
            return this;
        }

        public Builder setTimeoutFunction(Consumer<Object[]> function, Object... stacks) {
            if (is_execute)
                return this;
            
            task.timeout_stacks = stacks;
            task.timeout_function = function;
            return this;
        }

        public void execute() {
            Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
        }
    }

    public static Builder execute(String key) {
        if (task_mapper.containsKey(key)) {
            return new Builder(key, true);
        }

        return new Builder(key);
    }

    @Override
    public void run() {
        if (condition_function.apply(condition_stacks, tick)) {
            target_function.accept(target_stacks, tick);
            task_mapper.remove(key);
            return;
        }

        if (max_tick > 0 && tick >= max_tick) {
            task_mapper.remove(key);
            if (timeout_function != null)
                timeout_function.accept(timeout_stacks);
            return;            
        }

        ++tick;
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
    }
}
