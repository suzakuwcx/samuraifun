package Task.GameTask;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import Assert.Item.Buddha;
import DataBus.ConfigBus;
import DataBus.PlayerDataBus;
import FunctionBus.PlayerBus;
import FunctionBus.ScoreBoardBus;
import FunctionBus.ServerBus;
import Schedule.PlayerUISchedule;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;

public class GameTask implements Runnable {
    private static GameTask task;

    private int tick;
    private int task_id;
    private Map<ItemDisplay, Integer> buddha_map;

    {
        buddha_map = new HashMap<>();
    }

    private static void choose_random_spawn(GameTask task) {
        int red = ServerBus.getRandom().nextInt(0, task.buddha_map.size());
        int blue = ServerBus.getRandom().nextInt(0, task.buddha_map.size());
        while (blue == red) {
            blue = ServerBus.getRandom().nextInt(0, task.buddha_map.size());
        }

        task.buddha_map.put(task.buddha_map.keySet().toArray(new ItemDisplay[0])[red], 0);
        task.buddha_map.put(task.buddha_map.keySet().toArray(new ItemDisplay[0])[blue], ConfigBus.getValue("buddha_blood", Integer.class));
    }

    private static void set_player_spawnpoint() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setRespawnLocation(ConfigBus.getValue("respawn_point", Location.class), true);

            /* Player cannnot teleport if has passenger on it, but why???? */
            PlayerDataBus.removePlayerItemDisplay(player);
            player.teleport(ConfigBus.getValue("respawn_point", Location.class), TeleportCause.COMMAND);
            PlayerDataBus.addPlayerItemDisplay(player);

            PlayerBus.toGhost(player);
        }
    }

    public static void start() {
        GameTask new_task = new GameTask();
        new_task.task_id = Bukkit.getScheduler().runTaskTimer(ServerBus.getPlugin(), new_task, 0, 1).getTaskId();
        task = new_task;

        for (ItemDisplay display : Bukkit.getServer().getWorlds().get(0).getEntitiesByClass(ItemDisplay.class)) {
            ItemStack item = display.getItemStack();
            if (!Buddha._instanceof(item))
                continue;

            task.buddha_map.put(display, ConfigBus.getValue("buddha_blood", Integer.class) / 2);
            display.setGlowing(true);
            display.setGlowColorOverride(org.bukkit.Color.WHITE);
        }

        choose_random_spawn(task);
        set_player_spawnpoint();
    }

    public static boolean isInGame() {
        return task != null;
    }

    private void add_buddha(ItemDisplay display, int value) {
        int blood = buddha_map.get(display) + value;
        if (blood < 0)
            blood = 0;
        else if (blood > ConfigBus.getValue("buddha_blood", Integer.class))
            blood = ConfigBus.getValue("buddha_blood", Integer.class);

        buddha_map.put(display, blood);
    }

    public void showBossBar() {
        boolean has_red;
        boolean has_blue;

        for (ItemDisplay display : buddha_map.keySet()) {
            has_red = false;
            has_blue = false;
            for (Player player :ServerBus.getNearbyEntities(display.getLocation(), 8, 7, 8, EntityType.PLAYER, Player.class)) {
                BossBar bar = PlayerUISchedule.getPlayerFirstBossbar(player);
                bar.progress((float) buddha_map.get(display) / (float) ConfigBus.getValue("buddha_blood", Integer.class));
                if (buddha_map.get(display) < ConfigBus.getValue("buddha_blood", Integer.class) / 3) {
                    bar.color(Color.RED);
                } else if (buddha_map.get(display) < 2 * ConfigBus.getValue("buddha_blood", Integer.class) / 3) {
                    bar.color(Color.WHITE);
                } else {
                    bar.color(Color.BLUE);
                }

                if (player.getLocation().toVector().setY(0).distance(display.getLocation().toVector().setY(0)) <= 5) {
                    if (ScoreBoardBus.getPlayerTeam(player) == null) {

                    } else if (ScoreBoardBus.getPlayerTeam(player).getName().equals("red_team")) {
                        has_red = true;
                    } else if (ScoreBoardBus.getPlayerTeam(player).getName().equals("blue_team")) {
                        has_blue = true;
                    }
                    PlayerUISchedule.refreshPlayerFirstBossbar(player);
                } else {
                    bar.color(Color.PURPLE);
                    PlayerUISchedule.refreshPlayerFirstBossbar(player);
                }
            }

            if (has_red && !has_blue) {
                add_buddha(display, -1);
            } else if (!has_red && has_blue){
                add_buddha(display, 1);
            }

            if (buddha_map.get(display) < ConfigBus.getValue("buddha_blood", Integer.class) / 3) {
                display.setGlowColorOverride(org.bukkit.Color.fromRGB(0xB22222));
            } else if (buddha_map.get(display) < 2 * ConfigBus.getValue("buddha_blood", Integer.class) / 3) {
                display.setGlowColorOverride(org.bukkit.Color.WHITE);
            } else {
                display.setGlowColorOverride(org.bukkit.Color.AQUA);
            }
        }
    }

    @Override
    public void run() {
        if (tick == ConfigBus.getValue("game_time", Integer.class)) {
            Bukkit.getScheduler().cancelTask(task_id);
            return;
        }

        showBossBar();
        ++tick;
    }
}
