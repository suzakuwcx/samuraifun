package Assert.Command;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import Assert.Item.ItemDatabase;
import FunctionBus.ItemBus;
import FunctionBus.ServerBus;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;

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


    private boolean onItemDBCommand(Player player, Command command, String label, String[] args) {
        Inventory inventory = Bukkit.createInventory(null, 54, Component.text("物品"));
        inventory.addItem(ItemDatabase.getSet().toArray(new ItemStack[0]));
        player.openInventory(inventory);
        return true;
    }


    private List<String> onLogTabComplete(Player player, Command cmd, String commandLabel, String[] args){
        return null;
    }


    private boolean onLogCommand(Player player, Command command, String label, String[] args) {
        /*
         * Will not take effect if using 'Bukkit.getLogger().addHandler()' with unknown reason
         */
        Logger logger = ServerBus.getPlugin().getLogger();
        if (!PlayerMessageLogHandler.hasHandler(player)) {
            player.sendMessage("Log Enable");
            logger.addHandler(PlayerMessageLogHandler.getHandler(player));
        }
        else {
            player.sendMessage("Log Disable");
            logger.removeHandler(PlayerMessageLogHandler.getHandler(player));
        }
            
        return true;
    }


    private boolean onCharCommand(Player player, Command command, String label, String[] args) {
        if (args.length != 2)
            return false;

        String raw = args[1];
        char ch;

        try {
            if (raw.startsWith("0x"))
                ch = (char) Integer.parseInt(raw, 2, raw.length(), 16);
            else
                ch = (char) Integer.parseInt(args[1], 16);

            player.sendMessage(String.valueOf(ch));
        }
        catch (NumberFormatException e) {
            player.sendMessage(e.getMessage());
        }
        
        return true;
    }


    private boolean onOsCommand(Player player, Command command, String label, String[] args) {
        if (args.length == 1) {
            String path = ServerBus.getServerPath() + "/start.sh";

            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    player.sendMessage(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else if (args[1].equals("check")) {
            ProcessHandle currentProcess = ProcessHandle.current();
            long pid = currentProcess.pid();

            Bukkit.getScheduler().runTaskAsynchronously(ServerBus.getPlugin(), () -> {
                try {
                    Process process = new ProcessBuilder("/bin/sh", "-c", String.format("ps -p %d -o args=", pid)).start();
                    try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                        String cmdline = reader.readLine();
                        player.sendMessage(cmdline);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            return true;
        } else {
            String path = ServerBus.getServerPath() + "/start.sh";
            StringBuilder cmdline = new StringBuilder();

            for (int i = 1; i < args.length; ++i)
                cmdline.append(args[i]).append(' ');

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                writer.write(cmdline.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        switch (args[0]) {
            default:
                break;
        }

        if (sender instanceof Player player) {
            switch (args[0]) {
                case "itemnbt":
                    return onItemnbtCommand(player, command, label, args);
                case "itemdb":
                    return onItemDBCommand(player, command, label, args);
                case "log":
                    return onLogCommand(player, command, label, args);
                case "char":
                    return onCharCommand(player, command, label, args);
                case "os":
                    return onOsCommand(player, command, label, args);
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
        if (!(sender instanceof Player player)) {
            return null;
        }

        if (args.length == 1)
            return Arrays.asList("itemnbt", "itemdb", "log", "char", "os");

        switch (args[0]) {
            case "itemnbt":
                return onItemnbtTabComplete(player, command, label, args);
            case "log":
                return onLogTabComplete(player, command, label, args);
            default:
                return null;
        }
    } 
}
