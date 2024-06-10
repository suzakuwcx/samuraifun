package Assert.Config;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import Assert.Item.BattleFlag;
import Assert.Item.Boot;
import Assert.Item.Bow;
import Assert.Item.ChestPlate;
import Assert.Item.Helmet;
import Assert.Item.Legging;
import Assert.Item.Razor;
import Assert.Item.SmokingDarts;
import Assert.Item.Sword;
import Assert.Item.Gun.Matchlock;
import FunctionBus.PlayerBus;
import FunctionBus.ScoreBoardBus;
import Schedule.PlayerStateMachineSchedule;
import Task.StateTask.NormalStateTask;

public enum Role {
    COMMON(0),
    SAMURAI(1),
    RONIN(2),
    SHINBI(3),
    SOHEI(4);

    private final int value;
    
    private Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getSwordModelData(int data) {
        return value * 1000 + data;
    }

    public static void refreshPlayerArmor(Player player, Role role) {
        Team team = ScoreBoardBus.getPlayerTeam(player);
        if (team == null || team.getName().equals("red_team")) {
            PlayerBus.setPlayerInventoryList(player, new Helmet(role, false), 39);
            PlayerBus.setPlayerInventoryList(player, ChestPlate.getItem(role, false), 38);
            PlayerBus.setPlayerInventoryList(player, Legging.getItem(role, false), 37);
            PlayerBus.setPlayerInventoryList(player, Boot.getItem(role, false), 36);
        } else {
            PlayerBus.setPlayerInventoryList(player, new Helmet(role, true), 39);
            PlayerBus.setPlayerInventoryList(player, ChestPlate.getItem(role, true), 38);
            PlayerBus.setPlayerInventoryList(player, Legging.getItem(role, true), 37);
            PlayerBus.setPlayerInventoryList(player, Boot.getItem(role, true), 36);
        }
        
        PlayerBus.setPlayerInventoryList(player, new Bow(), 2, 5, 8);
        PlayerBus.setPlayerInventoryList(player, new Sword(role.getSwordModelData(1)), 0, 3, 6);
        PlayerStateMachineSchedule.setStateTask(player, new NormalStateTask(player));

        switch (role) {
            case SAMURAI:
                PlayerBus.setPlayerInventoryList(player, new BattleFlag(), 1, 4, 7);
                break;
            case RONIN:
                PlayerBus.setPlayerInventoryList(player, new Razor(), 1, 4, 7);
                break;
            case SHINBI:
                PlayerBus.setPlayerInventoryList(player, new SmokingDarts(), 1, 4, 7);
                break;
            case SOHEI:
                PlayerBus.setPlayerInventoryList(player, new Matchlock(), 1, 4, 7);
                break;
            case COMMON:
                PlayerBus.setPlayerInventoryList(player, new ItemStack(Material.AIR), 1, 4, 7);
            default:
                break;
        }
    }

    public static String getRoleAlias(Role role) {
        switch (role) {
            case SAMURAI:
                return "武士";
            case RONIN:
                return "浪人";
            case SHINBI:
                return "忍者";
            case SOHEI:
                return "僧兵";
            case COMMON:
            default:
                return "学徒";
        }
    }
}
