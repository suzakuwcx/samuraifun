package Task.DelayTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import FunctionBus.ServerBus;

public class SetNoDamageTicksTask implements Runnable{
    private LivingEntity entity;

    public SetNoDamageTicksTask(LivingEntity entity) {
        this.entity = entity;
    }

    public static void execute(LivingEntity entity) {
        SetNoDamageTicksTask task = new SetNoDamageTicksTask(entity);
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), task, 1);
    }

    @Override
    public void run() {
        entity.setNoDamageTicks(0);
    }
}
