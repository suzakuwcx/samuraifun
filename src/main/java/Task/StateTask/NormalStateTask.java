package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerDropItemEvent;

import Assert.Config.Role;
import Assert.Config.State;
import Assert.Font.FontDatabase;
import Assert.Item.Sword;
import DataBus.PlayerDataBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;
import net.kyori.adventure.text.Component;

public class NormalStateTask extends BaseStateTask {
    private Player player;
    private int tick = 0;

    public NormalStateTask(Player player) {
        this.player = player;

        TextDisplay display = PlayerDataBus.getPlayerRingDisplay(player);
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        
        if (display != null)
            display.text(Component.text(FontDatabase.getRingFont(state.role)));

        player.setCooldown(Material.SHIELD, 500000);
    }

    public NormalStateTask(Player player, Role role) {
        this.player = player;

        TextDisplay display = PlayerDataBus.getPlayerHealthDisplay(player);
        
        if (display != null)
            display.text(Component.text(FontDatabase.getRingFont(role)));

        player.setCooldown(Material.SHIELD, 500000);
    }

    @Override
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        ServerBus.playServerSound(event.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1f, 1f);
        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            PlayerBus.setPlayerInventoryList(p, new Sword(1002), 0, 3, 6);
        }, 2, event.getPlayer());

        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            PlayerBus.setPlayerInventoryList(p, new Sword(1003), 0, 3, 6);
        }, 4, event.getPlayer());
        PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new BattleStateTask(event.getPlayer()));
    }

    @Override
    public void run() {
        ++tick;

        if (tick == 20) {
            PlayerStateMachineSchedule.recoverPosture(player, 1);
            tick = 0;
        }
    }
}
