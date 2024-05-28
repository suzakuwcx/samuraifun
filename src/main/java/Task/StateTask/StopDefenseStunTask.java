package Task.StateTask;

import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerInteractEvent;

import Assert.Config.State;
import Assert.Font.FontDatabase;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import net.kyori.adventure.text.Component;

public class StopDefenseStunTask extends BaseStateTask {
    private Player player;
    private int tick = 0;

    public StopDefenseStunTask(Player player) {
        this.player = player;

        TextDisplay display = PlayerDataBus.getPlayerRingDisplay(player);
        
        if (display != null)
            display.text(Component.text(FontDatabase.STATUS_RING_STUN));
    }

    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (StateEventBus.isPlayerDefense(event)) {
            StateEventBus.onPlayerDefense(event);
        }
    }

    @Override
    public void run() {
        if (tick == 2) {
            PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
        }

        ++tick;
    }
}
