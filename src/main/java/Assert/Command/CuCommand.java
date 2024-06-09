package Assert.Command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import Assert.Config.Role;
import Assert.Config.State;
import Assert.Item.BattleFlag;
import Assert.Item.Boot;
import Assert.Item.Bow;
import Assert.Item.ChestPlate;
import Assert.Item.Helmet;
import Assert.Item.Legging;
import Assert.Item.Razor;
import Assert.Item.SmokingDarts;
import Assert.Item.Gun.Matchlock;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;

public class CuCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        State state;

        if (!(sender instanceof BlockCommandSender))
            return false;

        if (args.length != 1)
            return false;

        BlockCommandSender bsender = (BlockCommandSender) sender;
        Role role = Role.valueOf(args[0]);

        for (Player player : ServerBus.getNearbyEntities(bsender.getBlock().getLocation(), 2, 2, 2, EntityType.PLAYER, Player.class)) {
            state = PlayerStateMachineSchedule.getPlayerState(player);
            state.role = role;
            PlayerBus.setPlayerInventoryList(player, new Helmet(role), 39);
            PlayerBus.setPlayerInventoryList(player, ChestPlate.getItem(role), 38);
            PlayerBus.setPlayerInventoryList(player, Legging.getItem(role), 37);
            PlayerBus.setPlayerInventoryList(player, Boot.getItem(role), 36);
            PlayerBus.setPlayerInventoryList(player, new Bow(), 2, 5, 8);

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
                default:
                    break;
            }
            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return null;

        if (args.length != 1)
            return null;

        return Arrays.asList("COMMON", "SAMURAI", "RONIN", "SHINBI", "SOHEI");
    } 
}
