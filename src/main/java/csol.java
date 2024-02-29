import java.util.Objects;

import org.bukkit.plugin.java.JavaPlugin;

import Assert.Command.CsolCommand;

public class csol extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(new EventBus(), this);
        PacketBus.onEnable(this);
        Objects.requireNonNull(getCommand("csol")).setExecutor(new CsolCommand());
        SchedulerBus.run();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
