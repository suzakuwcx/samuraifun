package PacketBus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
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
        manager.addPacketListener(new ServerEntityEquipmentPacketBus(plugin));
    }

    public static Entity getEntityByID(int id) {
        return manager.getEntityFromID(Bukkit.getServer().getWorlds().get(0), id);
    }

    public static void onDisable() {
    }
}
