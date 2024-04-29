package Task.StateTask;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import FunctionBus.PlayerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.PlayerLongFClickTask;

public class ChargingAttackStateTask extends BaseStateTask {
    private Player player;
    private int tick = 0;

    public ChargingAttackStateTask(Player player) {
        this.player = player;
    }


    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        PlayerLongFClickTask.execute(player).setTick(10000000).setShortPressFunction((args, t) -> {
            Player p = (Player) args[0];

            if (!(PlayerStateMachineSchedule.getStateTask(p) instanceof ChargingAttackStateTask))
                return;

            if (t < 20)
                p.sendMessage("蓄力攻击 0");
            else if (t < 40)
                p.sendMessage("蓄力攻击 1");
            else if (t < 60)
                p.sendMessage("蓄力攻击 2");
            else
                p.sendMessage("蓄力攻击 3");

            PlayerStateMachineSchedule.setStateTask(p, new NormalStateTask(p));
        }, player).setCastingFunction((args, t2) -> {
            Player p2 = (Player) args[0];

            if (t2 < 20)
                p2.sendMessage("重击蓄力 0");
            else if (t2 < 40)
                p2.sendMessage("重击蓄力 1");
            else if (t2 < 60)
                p2.sendMessage("重击蓄力 2");
            else
                p2.sendMessage("重击蓄力 3");

        }, player).execute();
    }


    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (StateEventBus.isPlayerDefense(event))
            PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new DefenseStateTask(event.getPlayer()));
    }


    @Override
    public void run() {
        PlayerBus.banPlayerJump(player, 3);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 1));
    }
}
