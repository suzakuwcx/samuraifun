package Task;

import java.util.function.Consumer;

import org.bukkit.Bukkit;

import FunctionBus.ServerBus;

public class DelayTask implements Runnable {
    private Object[] stacks;
    Consumer<Object[]> function;

    private DelayTask(Consumer<Object[]> function, Object[] stacks) {
        this.stacks = stacks;
        this.function = function;
    }
    
    public static void execute(Consumer<Object[]> function, int tick, Object... args) {
        DelayTask task = new DelayTask(function, args);
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), task, tick);
    }

    @Override
    public void run() {
        function.accept(stacks);
    }
}
