package PacketBus;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class PacketBus {
    private static ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    public static ProtocolManager getProtocolManager() {
        return manager;
    }

    public static void onEnable(Plugin plugin) {
        manager.addPacketListener(new ServerNamedSoundEffectPackageBus(plugin));
    }

    public static void onDisable() {
    }
}
