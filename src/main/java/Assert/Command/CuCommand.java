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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import Assert.Config.Role;
import Assert.Config.State;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;

public class CuCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        State state;

        if (!(sender instanceof BlockCommandSender))
            return false;

        if (args.length != 2)
            return false;

        BlockCommandSender bsender = (BlockCommandSender) sender;
        Role role = Role.valueOf(args[0]);
        boolean refreshArmor = Boolean.valueOf(args[1]);

        for (Player player : ServerBus.getNearbyEntities(bsender.getBlock().getLocation(), 7, 7, 7, EntityType.PLAYER, Player.class)) {
            state = PlayerStateMachineSchedule.getPlayerState(player);
            state.role = role;
            player.sendMessage(String.format("你已切换为: <%s>", Role.getRoleAlias(role)));

            if (refreshArmor)
                Role.refreshPlayerArmor(player, role);

            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return null;

        if (args.length == 1)
            return Arrays.asList("COMMON", "SAMURAI", "RONIN", "SHINBI", "SOHEI");

        if (args.length == 2)
            return Arrays.asList("true", "false");

        return Arrays.asList("");
    } 
}
