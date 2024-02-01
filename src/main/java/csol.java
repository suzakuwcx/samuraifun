import org.bukkit.plugin.java.JavaPlugin;

public class csol extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(new EventBus(), this);
        SchedulerBus.run();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
