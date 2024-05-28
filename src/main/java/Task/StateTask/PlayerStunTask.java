package Task.StateTask;

import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Font.FontDatabase;
import Assert.Item.Sword;
import DataBus.PlayerDataBus;
import FunctionBus.PlayerBus;
import Schedule.PlayerStateMachineSchedule;
import net.kyori.adventure.text.Component;

public class PlayerStunTask extends BaseStateTask {
    private Player player;

    private int tick = 0;

    public PlayerStunTask(Player player) {
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
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (StateEventBus.isPlayerDash(event)) {
            StateEventBus.onPlayerDash(event);
            PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
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
