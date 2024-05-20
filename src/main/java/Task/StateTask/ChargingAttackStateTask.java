package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Config.State;
import Assert.Item.Sword;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.PlayerLongFClickTask;

public class ChargingAttackStateTask extends BaseStateTask {
    private Player player;

    public ChargingAttackStateTask(Player player) {
        this.player = player;

        player.setCooldown(Material.SHIELD, 500000);
        charging(player);
    }

    private static void charging(Player player) {
        PlayerLongFClickTask.execute(player).setTick(42).setShortPressFunction((args, t) -> {
            Player p = (Player) args[0];
            State state = PlayerStateMachineSchedule.getPlayerState(player);

            if (!(PlayerStateMachineSchedule.getStateTask(p) instanceof ChargingAttackStateTask))
                return;

            if (t < 11)
                PlayerStateMachineSchedule.setStateTask(p, new ChargedAttackAnimStateTask(player, 0));
            else if (t < 21)
                PlayerStateMachineSchedule.setStateTask(p, new ChargedAttackAnimStateTask(player, 1));
            else if (t < 31)
                PlayerStateMachineSchedule.setStateTask(p, new ChargedAttackAnimStateTask(player, 2));
            else
                PlayerStateMachineSchedule.setStateTask(p, new ChargedAttackAnimStateTask(player, 3));

            state.charging = 0;
        }, player).setCastingFunction((args, t2) -> {
            Player p2 = (Player) args[0];
            State state = PlayerStateMachineSchedule.getPlayerState(player);

            if (t2 <= 40)
                state.charging = t2;

            if (t2 == 0)
                ServerBus.playServerSound(p2.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0f);

            if (t2 < 10) {
                PlayerBus.setPlayerInventoryList(player, new Sword(1013), 0, 3, 6);
            } else if (t2 == 10) {
                ServerBus.playServerSound(p2.getLocation(), Sound.ITEM_TRIDENT_RETURN, 1f, 1f);
            } else if (t2 < 20) {
                PlayerBus.setPlayerInventoryList(player, new Sword(1014), 0, 3, 6);
            } else if (t2 == 20) {
                ServerBus.playServerSound(p2.getLocation(), Sound.ITEM_TRIDENT_RETURN, 1f, 1.5f);
            } else if (t2 < 30) {
                PlayerBus.setPlayerInventoryList(player, new Sword(1015), 0, 3, 6);
            } else if (t2 == 30) {
                ServerBus.playServerSound(p2.getLocation(), Sound.ITEM_TRIDENT_RETURN, 1f, 2f);
            } else {
                PlayerBus.setPlayerInventoryList(player, new Sword(1016), 0, 3, 6);
            }

        }, player).setLongPressFunction((args) -> {
            Player p3 = (Player) args[0];
            State state = PlayerStateMachineSchedule.getPlayerState(player);

            if (!(PlayerStateMachineSchedule.getStateTask(p3) instanceof ChargingAttackStateTask))
                return;

            PlayerStateMachineSchedule.setStateTask(p3, new ChargedAttackAnimStateTask(player, 3));

            state.charging = 0;
        }, player).execute();
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        charging(event.getPlayer());
    }


    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (StateEventBus.isPlayerDefense(event)) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
            PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new DefenseStateTask(event.getPlayer()));
        }
    }


    @Override
    public void run() {
        PlayerBus.banPlayerJump(player, 3);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 1));
    }
}
