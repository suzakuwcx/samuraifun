package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import Assert.Item.Sword;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;

public class NormalStateTask extends BaseStateTask {
    private Player player;

    public NormalStateTask(Player player) {
        this.player = player;

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

    }
}
