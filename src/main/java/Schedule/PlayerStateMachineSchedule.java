package Schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Assert.Config.PlayerConfig;
import Assert.Config.State;
import Assert.Item.Sword;
import FunctionBus.PlayerBus;
import Task.StateTask.BaseStateTask;
import Task.StateTask.NormalStateTask;
import Task.StateTask.PlayerPostureCrashTask;

public class PlayerStateMachineSchedule implements Runnable {
    public static Map<UUID, State> player_state_map = new HashMap<>();

    static {
        player_state_map = new HashMap<>();
    }

    public static void init(Player player) {
        State state;
        if (!player_state_map.containsKey(player.getUniqueId())) {
            player_state_map.put(player.getUniqueId(), new State(player));
        } else {
            state = player_state_map.get(player.getUniqueId());
            state.state = new NormalStateTask(player);
        }
    }

    public static State getPlayerState(Player player) {
        return player_state_map.get(player.getUniqueId());
    }

    public static BaseStateTask getStateTask(Player player) {
        return getPlayerState(player).state;
    }

    public static void setStateTask(Player player, BaseStateTask task) {
        getPlayerState(player).state = task;
    }

    private static int noMinusDecrease(int arg, int value) {
        int ret = arg - value;
        if (ret < 0)
            return 0;
        
        return ret;
    }

    public static void damageHealth(Player target, int damage) {
        State state = getPlayerState(target);
        state.health = noMinusDecrease(state.health, damage);
    }

    public static boolean isPlayerNoHealth(Player player) {
        State state = getPlayerState(player);
        return state.health == 0;
    }

    public static void damagePosture(Player player, int damage, boolean is_crash) {
        State state = getPlayerState(player);
        state.posture = noMinusDecrease(state.posture, damage);
        if (is_crash && state.posture == 0) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1023), 0, 3, 6);
            state.state = new PlayerPostureCrashTask(player);
        }
    }

    public static void damagePosture(Player player, int damage) {
        damagePosture(player, damage, true);
    }

    public static boolean isPlayerNoPosture(Player player) {
        State state = getPlayerState(player);
        return state.posture == 0;
    }

    public static void recoverPosture(Player player, int value) {
        State state = getPlayerState(player);
        state.posture += value;
        if (state.posture > PlayerConfig.MAX_POSTURE)
            state.posture = PlayerConfig.MAX_POSTURE;
    }

    public static void resetSwordCooldown(Player player) {
        State state = player_state_map.get(player.getUniqueId());
        state.sword_cooldown = PlayerConfig.SWORD_COOLDOWN;
    }

    private static void updateCooldown(Player player) {
        State state = player_state_map.get(player.getUniqueId());
        state.dash_cooldown = noMinusDecrease(state.dash_cooldown, 1);
        state.sword_cooldown = noMinusDecrease(state.sword_cooldown, 1);
        state.skill_cooldown = noMinusDecrease(state.skill_cooldown, 1);
        state.bow_cooldown = noMinusDecrease(state.bow_cooldown, 1);
    }

    private static void updatePlayerEffect(Player player) {
        if (player.isSneaking()) {
            PlayerBus.banPlayerJump(player, 3);
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player_state_map.get(player.getUniqueId()).state.run();
            player_state_map.get(player.getUniqueId()).refresh();
            updateCooldown(player);
            updatePlayerEffect(player);
        }
    }
}
