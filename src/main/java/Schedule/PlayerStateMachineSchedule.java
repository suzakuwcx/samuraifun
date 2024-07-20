package Schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Assert.Config.Role;
import Assert.Config.State;
import Assert.Item.Sword;
import ConfigBus.ConfigBus;
import FunctionBus.PlayerBus;
import Task.StateTask.BaseStateTask;
import Task.StateTask.PlayerPostureCrashTask;

public class PlayerStateMachineSchedule implements Runnable {
    public static Map<UUID, State> player_state_map = new HashMap<>();

    static {
        player_state_map = new HashMap<>();
    }

    public static State getPlayerState(Player player) {
        State state = player_state_map.get(player.getUniqueId());
        if (state == null) {
            state = new State(player);
            player_state_map.put(player.getUniqueId(), state);
        }

        return state;
    }

    public static Role getPlayerRole(Player player) {
        return getPlayerState(player).role;
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
            PlayerBus.setPlayerInventoryList(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(23)), 0, 3, 6);
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
        if (state.posture > ConfigBus.getValue("max_posture", Integer.class))
            state.posture = ConfigBus.getValue("max_posture", Integer.class);
    }

    public static void recoverHealth(Player player, int value) {
        State state = getPlayerState(player);
        state.health += value;
        if (state.health > ConfigBus.getValue("max_health", Integer.class))
            state.health = ConfigBus.getValue("max_health", Integer.class);
    }

    public static void resetSwordCooldown(Player player) {
        State state = getPlayerState(player);
        state.sword_cooldown = ConfigBus.getValue("sword_cooldown", Integer.class);
    }

    public static void resetBowCooldown(Player player) {
        State state = getPlayerState(player);
        state.bow_cooldown = ConfigBus.getValue("skill_cooldown", Integer.class);
    }

    private static void updateCooldown(Player player) {
        State state = getPlayerState(player);
        state.dash_cooldown = noMinusDecrease(state.dash_cooldown, 1);
        state.sword_cooldown = noMinusDecrease(state.sword_cooldown, 1);
        state.skill_cooldown = noMinusDecrease(state.skill_cooldown, 1);
        state.bow_cooldown = noMinusDecrease(state.bow_cooldown, 1);

        if (state.bow_cooldown == 1)
            player.getInventory().setItem(9, new ItemStack(Material.ARROW));
    }

    private static void updatePlayerEffect(Player player) {
        if (player.isSneaking()) {
            PlayerBus.banPlayerJump(player, 3);
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            getPlayerState(player).state.run();
            getPlayerState(player).refresh();
            updateCooldown(player);
            updatePlayerEffect(player);
        }
    }
}
