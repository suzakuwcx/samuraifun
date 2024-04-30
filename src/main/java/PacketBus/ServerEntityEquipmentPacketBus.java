package PacketBus;

import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.Pair;

import Assert.Config.State;
import Assert.Item.FakeSword;
import Schedule.PlayerStateMachineSchedule;
import Task.StateTask.NormalAttackStateTask;

public class ServerEntityEquipmentPacketBus extends PacketAdapter {
    public ServerEntityEquipmentPacketBus(Plugin plugin) {
        super(plugin, PacketType.Play.Server.ENTITY_EQUIPMENT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        plugin.getLogger().log(Level.FINE, String.format("onPacketSending: %s", getClass().getName()));

        PacketContainer container = event.getPacket();

        int entity_id = container.getIntegers().read(0);
        Player player = (Player) PacketBus.getEntityByID(entity_id);
        State state = PlayerStateMachineSchedule.getPlayerState(player);

        if (!(state.state instanceof NormalAttackStateTask))
            return;


        Pair<EnumWrappers.ItemSlot, ItemStack> pair = container.getSlotStackPairLists().read(0).get(0);

        if (!pair.getFirst().equals(ItemSlot.MAINHAND))
            return;

        pair.setSecond(FakeSword.getItem(state.current_sword_frame));
        container.getSlotStackPairLists().write(0, Arrays.asList(pair));
        event.setPacket(container);
    }
}
