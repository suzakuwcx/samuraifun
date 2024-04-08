package PacketBus;

import java.util.logging.Level;

import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class ServerNamedSoundEffectPackageBus extends PacketAdapter{
    public ServerNamedSoundEffectPackageBus(Plugin plugin) {
        super(plugin, PacketType.Play.Server.NAMED_SOUND_EFFECT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        plugin.getLogger().log(Level.FINE, String.format("onPacketSending: %s", getClass().getName()));

        Sound sound = event.getPacket().getSoundEffects().read(0);
        if (sound == null)
            return;
        
        switch (sound) {
        /* 
            // Because player damage to entity was cancel so player cannot sweep 
            case ENTITY_PLAYER_ATTACK_SWEEP:
            if (!Sword._instanceof(player.getInventory().getItemInMainHand()))
                event.setCancelled(true);
        */
            case ENTITY_PLAYER_ATTACK_NODAMAGE:
            case ENTITY_PLAYER_ATTACK_WEAK:
            case ENTITY_PLAYER_ATTACK_STRONG:
            case ENTITY_PLAYER_ATTACK_KNOCKBACK:
            case ENTITY_PLAYER_ATTACK_CRIT:
                event.setCancelled(true);
            default:
        }
    }
}
