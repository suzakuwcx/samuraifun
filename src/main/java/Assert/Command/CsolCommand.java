package Assert.Command;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
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
import ConfigBus.ConfigBus;
import FunctionBus.ItemBus;
import FunctionBus.ServerBus;
import Task.DelayTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

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


    /* /config [key] [type] [value] */
    private boolean onConfigCommand(Player player, Command command, String label, String[] args) {
        if (args.length != 3 && args.length != 4)
            return false;

        String path = args[1];

        /* Getter */
        if (args.length == 3) {
            switch (args[2].toUpperCase()) {
                case "STRING":
                    player.sendMessage(ConfigBus.getValue(path, String.class));
                    break;
                case "INTEGER":
                    player.sendMessage(ConfigBus.getValue(path, Integer.class).toString());
                    break;
                case "DOUBLE":
                    player.sendMessage(ConfigBus.getValue(path, Double.class).toString());
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } else if (args.length == 4) {
            switch (args[2].toUpperCase()) {
                case "STRING":
                    ConfigBus.setValue(path, args[3], String.class);
                    break;
                case "INTEGER":
                    ConfigBus.setValue(path, Integer.valueOf(args[3]), Integer.class);
                    break;
                case "DOUBLE":
                    ConfigBus.setValue(path, Double.valueOf(args[3]), Double.class);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        return true;
    }

    private List<String> onConfigTabComplete(Player player, Command cmd, String commandLabel, String[] args) {
        if (args.length == 2)
            return Arrays.asList(ConfigBus.getKeys().toArray(new String[0]));
        else if (args.length == 3)
            return Arrays.asList("STRING", "INTEGER", "DOUBLE");
        else
            return Arrays.asList();
    }


    private List<String> onLogTabComplete(Player player, Command cmd, String commandLabel, String[] args) {
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


    private List<String> onCharTabComplete(Player player, Command cmd, String commandLabel, String[] args){
        if (args.length == 2)
            return Arrays.asList("MESSAGE", "TITLE", "SUBTITLE", "ACTIONBAR", "BOSSBAR");
        else
            return Arrays.asList();
    }


    private boolean onCharCommand(Player player, Command command, String label, String[] args) {
        if (args.length < 3)
            return false;

        String mode = args[1];
        String raw = "";

        for (int i = 2; i < args.length - 1; ++i) {
            raw += args[i];
            raw += ' ';
        }

        raw += args[args.length - 1];
        
        Pattern pattern = Pattern.compile("0x([0-9A-Fa-f]{4})");
        Matcher matcher = pattern.matcher(raw);
        char ch = ' ';
        Title title;
        BossBar bar;

        StringBuffer result = new StringBuffer();

        try {
            while (matcher.find()) {
                ch = (char) Integer.parseInt(matcher.group(1), 16);
                matcher.appendReplacement(result, String.valueOf(ch));
            }
            matcher.appendTail(result);
        }
        catch (NumberFormatException e) {
            player.sendMessage(e.getMessage());
        }


        switch (mode) {
            case "MESSAGE":
                player.sendMessage(String.valueOf(ch));
                break;
            case "TITLE":
                title = Title.title(Component.text(result.toString()), Component.text(""), Times.times(Duration.ofSeconds(0), Duration.ofSeconds(40), Duration.ofSeconds(10)));
                player.showTitle(title);
                break;
            case "SUBTITLE":
                title = Title.title(Component.text(""), Component.text(result.toString()), Times.times(Duration.ofSeconds(0), Duration.ofSeconds(40), Duration.ofSeconds(10)));
                player.showTitle(title);
                break;
            case "ACTIONBAR":
                player.sendActionBar(Component.text(result.toString()));
                break;
            case "BOSSBAR":
                bar = Bukkit.createBossBar(String.valueOf(result.toString()), BarColor.RED, BarStyle.SOLID);
                bar.addPlayer(player);
                DelayTask.execute((argv) -> {
                    BossBar b = (BossBar) argv[0];
                    b.removeAll();
                }, 300, bar);
                break;
            default:
                return false;
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


    private boolean onUpgradeCommand(CommandSender player, Command command, String label, String[] args) {
        if (args.length != 2)
            return false;

        String path = ServerBus.getServerPath() + "/plugins/csol-1.0.jar.new";
        String src = ServerBus.getServerPath() + "/plugins/csol-1.0.jar";

        switch (args[1]) {
            case "check":
                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    player.sendMessage(e.getMessage());
                    return false;
                }
                    
                try (FileInputStream fis = new FileInputStream(path)) {
                    byte[] byteArray = new byte[1024];
                    int bytesCount;
                    while ((bytesCount = fis.read(byteArray)) != -1) {
                        digest.update(byteArray, 0, bytesCount);
                    }
                } catch (IOException e) {
                    player.sendMessage(e.getMessage());
                    return false;
                }
        
                byte[] bytes = digest.digest();
                StringBuilder builder = new StringBuilder();
                for (byte b : bytes) {
                    builder.append(String.format("%02x", b));
                }
                player.sendMessage(builder.toString());
                break;
            case "replace":
                try {
                    Files.copy(Paths.get(path), Paths.get(src), StandardCopyOption.REPLACE_EXISTING);
                    player.sendMessage("Success");
                } catch (Exception e) {
                    player.sendMessage(e.getMessage());
                    return false;
                }
                break;
            default:
                Bukkit.getScheduler().runTaskAsynchronously(ServerBus.getPlugin(), () -> {
                    try (BufferedInputStream in = new BufferedInputStream(new URL(args[1]).openStream());
                        FileOutputStream out = new FileOutputStream(path)) {
                        byte dataBuffer[] = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            out.write(dataBuffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage("done");
                });
                break;
        }

        return true;
    }


    private List<String> onUpgradeTabComplete(Player player, Command cmd, String commandLabel, String[] args){
        if (args.length == 2)
            return Arrays.asList("check", "replace");

        return null;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        switch (args[0]) {
            case "upgrade":
                return onUpgradeCommand(sender, command, label, args);
            default:
                break;
        }

        if (sender instanceof Player player) {
            switch (args[0]) {
                case "itemnbt":
                    return onItemnbtCommand(player, command, label, args);
                case "itemdb":
                    return onItemDBCommand(player, command, label, args);
                case "config":
                    return onConfigCommand(player, command, label, args);
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
            return Arrays.asList("itemnbt", "itemdb", "config", "log", "char", "os", "upgrade");

        switch (args[0]) {
            case "itemnbt":
                return onItemnbtTabComplete(player, command, label, args);
            case "config":
                return onConfigTabComplete(player, command, label, args);
            case "log":
                return onLogTabComplete(player, command, label, args);
            case "char":
                return onCharTabComplete(player, command, label, args);
            case "upgrade":
                return onUpgradeTabComplete(player, command, label, args);
            default:
                return null;
        }
    } 
}
