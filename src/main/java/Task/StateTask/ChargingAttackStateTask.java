package Task.StateTask;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Config.State;
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
            State state = PlayerStateMachineSchedule.getPlayerState(player);

            if (!(PlayerStateMachineSchedule.getStateTask(p) instanceof ChargingAttackStateTask))
                return;

            if (t < 21)
                p.sendMessage("蓄力攻击 0");
            else if (t < 41)
                p.sendMessage("蓄力攻击 1");
            else if (t < 61)
                p.sendMessage("蓄力攻击 2");
            else
                p.sendMessage("蓄力攻击 3");

            state.charging = 0;

            PlayerStateMachineSchedule.setStateTask(p, new NormalStateTask(p));
        }, player).setCastingFunction((args, t2) -> {
            Player p2 = (Player) args[0];
            State state = PlayerStateMachineSchedule.getPlayerState(player);

            if (t2 <= 80)
                state.charging = t2 / 2;


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
