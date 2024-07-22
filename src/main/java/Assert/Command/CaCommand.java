package Assert.Command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import Assert.Config.State;
import DataBus.PlayerDataBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;

public class CaCommand implements CommandExecutor, TabCompleter{

    private Player getTargetPlayer(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        Location location = null;
        Player player = null;

        if (sender instanceof Player)
            location = ((Player) sender).getLocation();
        else if (sender instanceof BlockCommandSender)
            location = ((BlockCommandSender) sender).getBlock().getLocation();
        else
            return null;

        if (args.length == 1)
            player = ServerBus.getNearestEntity(location, 10, 10, 10, EntityType.PLAYER, Player.class);
        else if (args.length == 2)
            player = ServerBus.getPlayerByName(args[1]);

        return player;
    }

    private boolean onDumpCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (args.length != 1 && args.length != 2)
            return false;

        Player player = getTargetPlayer(sender, command, label, args);

        if (player == null) {
            sender.sendMessage("Error: cannot find target player");
            return false;
        }

        State state = PlayerStateMachineSchedule.getPlayerState(player);

        sender.sendMessage(String.format("""
            Role: %s
            State: %s
            Health: %d
            Posture: %d
            Sword Cooldown: %d
            """,
            state.role.toString(),
            state.state.getClass().getSimpleName(),
            state.health,
            state.posture,
            state.sword_cooldown));

        return true;
    }

    private boolean onResetCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (args.length != 1 && args.length != 2)
            return false;

        Player player = getTargetPlayer(sender, command, label, args);

        if (player == null) {
            sender.sendMessage("Error: cannot find target player");
            return false;
        }

        PlayerBus.resetPlayerState(player);

        return true;
    }


    private boolean onMonitorCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (args.length != 2)
            return false;

        Player player = getTargetPlayer(sender, command, label, args);

        if (player == null) {
            sender.sendMessage("Error: cannot find target player");
            return false;
        }

        if (PlayerDataBus.hasMonitorPlayer(player)) {
            sender.sendMessage(String.format("Disable monitor target: %s", player.getName()));
            PlayerDataBus.removeMonitorPlayer(player);
        } else {
            sender.sendMessage(String.format("Enable monitor target: %s", player.getName()));
            PlayerDataBus.addMonitorPlayer(player);
        }
        
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        switch (args[0]) {
            case "dump":
                return onDumpCommand(sender, command, label, args);
            case "reset":
                return onResetCommand(sender, command, label, args);
            case "monitor":
                return onMonitorCommand(sender, command, label, args);
            default:
                break;
        }

        if (sender instanceof Player player) {
            switch (args[0]) {
                default:
                    return false;
            }
        } else if (sender instanceof BlockCommandSender commandblock) {
            switch (args[0]) {
                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return null;

        if (args.length == 1)
            return Arrays.asList("dump", "reset", "monitor");

        return null;
    }
    
}
