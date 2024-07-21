package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Config.Role;
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

        player.setCooldown(Material.SHIELD, 500000);
        /* Allow player sprint */
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 0, false, false, false));
        player.setFoodLevel(20);
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
            StateEventBus.replacePlayerSwordSlot(p, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(2)));
        }, 2, event.getPlayer());

        DelayTask.execute((args) -> {
            Player p = (Player) args[0];
            StateEventBus.replacePlayerSwordSlot(p, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(3)));
        }, 4, event.getPlayer());
        PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new BattleStateTask(event.getPlayer()));
    }


    @Override
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        event.setCancelled(false);
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
