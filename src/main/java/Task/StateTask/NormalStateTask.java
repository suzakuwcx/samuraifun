package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import Assert.Item.Sword;
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
        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            p.getInventory().setItem(0, new Sword(1002));
            p.getInventory().setItem(3, new Sword(1002));
            p.getInventory().setItem(6, new Sword(1002));
        }, 2, event.getPlayer());

        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            p.getInventory().setItem(0, new Sword(1003));
            p.getInventory().setItem(3, new Sword(1003));
            p.getInventory().setItem(6, new Sword(1003));
        }, 4, event.getPlayer());
        PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new BattleStateTask(event.getPlayer()));
    }


    @Override
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void run() {

    }
}
