import java.util.Objects;

import org.bukkit.plugin.java.JavaPlugin;

import Assert.Command.CsolCommand;
import Assert.Command.CuCommand;
import DataBus.ConfigBus;
import PacketBus.PacketBus;

public class csol extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new EventBus(), this);
        PacketBus.onEnable(this);
        ConfigBus.onEnable(getConfig());
        Objects.requireNonNull(getCommand("csol")).setExecutor(new CsolCommand());
        Objects.requireNonNull(getCommand("cu")).setExecutor(new CuCommand());

        SchedulerBus.run();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.saveConfig();
    }
}
