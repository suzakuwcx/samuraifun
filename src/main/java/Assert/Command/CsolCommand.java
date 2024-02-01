package Assert.Command;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import FunctionBus.ItemBus;

public class CsolCommand implements CommandExecutor, TabCompleter {

    /*
     * /itemnbt [key] [Type] [value]
     */
    private boolean onItemnbtCommand(Player player, Command command, String label, String[] args) {
        if (args.length != 4) {
            return false;
        }

        switch (args[2].toUpperCase()) {
            case "STRING":
                ItemBus.setItemNBT(player.getInventory().getItemInMainHand(), args[1], PersistentDataType.STRING, args[3]);
                break;
            case "INTEGER":
                ItemBus.setItemNBT(player.getInventory().getItemInMainHand(), args[1], PersistentDataType.INTEGER, Integer.valueOf(args[3]));
                break;
            case "DOUBLE":
                ItemBus.setItemNBT(player.getInventory().getItemInMainHand(), args[1], PersistentDataType.DOUBLE, Double.valueOf(args[3]));
                break;
            default:
                throw new IllegalArgumentException();
        }

        return true;
    }


    private List<String> onItemnbtTabComplete(Player player, Command cmd, String commandLabel, String[] args){
        if (args.length == 3)
            return Arrays.asList("STRING", "INTEGER", "DOUBLE");
        
        return null;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (args[0]) {
                case "itemnbt":
                    return onItemnbtCommand(player, command, label, args);
                default:
                    return false;
            }
        }
        else if (sender instanceof BlockCommandSender commandblock) {
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
        if (!(sender instanceof Player player)) {
            return null;
        }

        if (args.length == 1)
            return Arrays.asList("itemnbt");

        switch (args[0]) {
            case "itemnbt":
                return onItemnbtTabComplete(player, command, label, args);
            default:
                return null;
        }
    } 
}
