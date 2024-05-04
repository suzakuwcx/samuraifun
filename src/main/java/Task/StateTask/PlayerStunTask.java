package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Item.Sword;
import FunctionBus.PlayerBus;
import Schedule.PlayerStateMachineSchedule;

public class PlayerStunTask extends BaseStateTask {
    private Player player;

    private int tick = 0;

    public PlayerStunTask(Player player) {
        this.player = player;

        player.setCooldown(Material.SHIELD, 500000);
    }

    @Override
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (StateEventBus.isPlayerPrepareDash(event)) {
            StateEventBus.onPlayerPrepareDash(event);
        } else if (StateEventBus.isPlayerDash(event)) {
            StateEventBus.onPlayerDash(event);
            PlayerStateMachineSchedule.player_state_map.get(player.getUniqueId()).state = new BattleStateTask(player);
        }
    }

    @Override
    public void run() {
        ++tick;

        if (tick <= 20) {
            PlayerBus.banPlayerJump(player, 2);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 3, false, false));
        } else {
            PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
            PlayerStateMachineSchedule.player_state_map.get(player.getUniqueId()).state = new BattleStateTask(player);
        }
    }
}
