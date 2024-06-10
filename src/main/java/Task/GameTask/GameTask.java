package Task.GameTask;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

import Assert.Item.Buddha;
import DataBus.ConfigBus;
import DataBus.PlayerDataBus;
import FunctionBus.PlayerBus;
import FunctionBus.ScoreBoardBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.text.Component;

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
            Team team = ScoreBoardBus.getPlayerTeam(player);
            if (team == null || team.getName().equals("white_team")) {
                player.getInventory().clear();
                player.setGameMode(GameMode.SPECTATOR);
                continue;
            }

            player.setRespawnLocation(ConfigBus.getValue("respawn_point", Location.class), true);

            /* Player cannnot teleport if has passenger on it, but why???? */
            PlayerDataBus.removePlayerItemDisplay(player);
            player.teleport(ConfigBus.getValue("respawn_point", Location.class), TeleportCause.COMMAND);
            PlayerDataBus.addPlayerItemDisplay(player);

            PlayerBus.toGhost(player);
            PlayerStateMachineSchedule.recoverHealth(player, ConfigBus.getValue("max_health", Integer.class));
            PlayerStateMachineSchedule.recoverPosture(player, ConfigBus.getValue("max_posture", Integer.class));
        }
    }

    private static void init_scoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ScoreBoardBus.setPlayerScore(player.getName(), "kill", 0);
        }
    }

    public static void start() {
        GameTask new_task = new GameTask();
        ScoreBoardBus.setObjDisplay("global", DisplaySlot.SIDEBAR, "占领情况");
        new_task.task_id = Bukkit.getScheduler().runTaskTimer(ServerBus.getPlugin(), new_task, 0, 20).getTaskId();
        task = new_task;

        for (ItemDisplay display : Bukkit.getServer().getWorlds().get(0).getEntitiesByClass(ItemDisplay.class)) {
            ItemStack item = display.getItemStack();
            if (!Buddha._instanceof(item))
                continue;

            task.buddha_map.put(display, ConfigBus.getValue("buddha_blood", Integer.class) / 2);
            display.setGlowing(true);
            display.setGlowColorOverride(org.bukkit.Color.WHITE);
        }

        ItemDisplay[] displays = task.buddha_map.keySet().toArray(new ItemDisplay[0]);
        for(int i = 0; i < ConfigBus.getValue("buddha_discard", Integer.class); ++i) {
            ItemDisplay dp = displays[i];
            dp.setGlowing(false);
            task.buddha_map.remove(dp);
        }

        choose_random_spawn(task);
        set_player_spawnpoint();
        init_scoreboard();
    }

    public static void stop() {
        for (ItemDisplay display : task.buddha_map.keySet())
            display.setGlowing(false);

        Bukkit.getScheduler().cancelTask(task.task_id);
        task = null;
        Bukkit.broadcast(Component.text("游戏结束"));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setRespawnLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
            PlayerDataBus.removePlayerItemDisplay(player);
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            PlayerBus.resetPlayerGame(player);
            player.setGameMode(GameMode.ADVENTURE);
            PlayerDataBus.addPlayerItemDisplay(player);
            BossBar bar = PlayerUISchedule.getPlayerFirstBossbar(player);
            bar.name(Component.text(""));
            PlayerUISchedule.refreshPlayerFirstBossbar(player);
        }
    }

    public static boolean isInGame() {
        return task != null;
    }

    public static int getLastTick() {
        return ConfigBus.getValue("game_time", Integer.class) - task.tick;
    }

    private void add_buddha(ItemDisplay display, int value) {
        int blood = buddha_map.get(display) + value;
        if (blood < 0)
            blood = 0;
        else if (blood > ConfigBus.getValue("buddha_blood", Integer.class))
            blood = ConfigBus.getValue("buddha_blood", Integer.class);

        buddha_map.put(display, blood);
    }

    private void player_sound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.1f);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1f, 0.5f);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1f, 1f);
    }

    public void showBossBar() {
        boolean has_red;
        boolean has_blue;

        for (ItemDisplay display : buddha_map.keySet()) {
            has_red = false;
            has_blue = false;
            for (Player player :ServerBus.getNearbyEntities(display.getLocation(), 15, 7, 15, EntityType.PLAYER, Player.class)) {
                if (PlayerBus.isPlayerGhost(player))
                    continue;
                
                if (player.getGameMode() != GameMode.ADVENTURE)
                    continue;

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
                    player_sound(player);
                    if (ScoreBoardBus.getPlayerTeam(player) == null) {

                    } else if (ScoreBoardBus.getPlayerTeam(player).getName().equals("red_team")) {
                        has_red = true;
                    } else if (ScoreBoardBus.getPlayerTeam(player).getName().equals("blue_team")) {
                        has_blue = true;
                    }
                } else {
                    bar.color(Color.PURPLE);
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

            for (Player player: Bukkit.getOnlinePlayers()) {
                int minutes = GameTask.getLastTick() / 60;
                int seconds = GameTask.getLastTick() % 60;
                BossBar bar = PlayerUISchedule.getPlayerFirstBossbar(player);
                bar.name(Component.text(String.format("%d:%d", minutes, seconds)));
                PlayerUISchedule.refreshPlayerFirstBossbar(player);
            }
        }
    }
    
    private void release() {
        stop();

        ScoreBoardBus.setObjDisplay("kill", DisplaySlot.SIDEBAR, "击杀数");
    }

    public static void red_win() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ScoreBoardBus.setPlayerScore(player.getName(), "total_play", ScoreBoardBus.getPlayerScore(player.getName(), "total_play") + 1);

            if (ScoreBoardBus.getPlayerTeam(player).getName().equals("red_team")) {
                ScoreBoardBus.setPlayerScore(player.getName(), "total_win", ScoreBoardBus.getPlayerScore(player.getName(), "total_win") + 1);
            }
        }
    }

    public static void blue_win() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ScoreBoardBus.setPlayerScore(player.getName(), "total_play", ScoreBoardBus.getPlayerScore(player.getName(), "total_play") + 1);

            if (ScoreBoardBus.getPlayerTeam(player).getName().equals("blue_team")) {
                ScoreBoardBus.setPlayerScore(player.getName(), "total_win", ScoreBoardBus.getPlayerScore(player.getName(), "total_win") + 1);
            }
        }
    }

    public static void draw() {
        for (Player player : Bukkit.getOnlinePlayers())
            ScoreBoardBus.setPlayerScore(player.getName(), "total_play", ScoreBoardBus.getPlayerScore(player.getName(), "total_play") + 1);
    }

    private void victory_detection(boolean force) {
        int red_count = 0;
        int blue_count = 0;

        for (ItemDisplay display : buddha_map.keySet()) {
            if (buddha_map.get(display) < ConfigBus.getValue("buddha_blood", Integer.class) / 3) {
                ++red_count;
            } else if (buddha_map.get(display) > 2 * ConfigBus.getValue("buddha_blood", Integer.class) / 3) {
                ++blue_count;
            }
        }

        if (red_count == buddha_map.size()) {
            Bukkit.broadcast(Component.text("红方胜利"));
            red_win();
            release();
        } else if (blue_count == buddha_map.size()) {
            Bukkit.broadcast(Component.text("蓝方胜利"));
            blue_win();
            release();
        }

        if (force) {
            if (red_count == blue_count) {
                Bukkit.broadcast(Component.text("平局"));
                draw();
                release();
            } else if (red_count > blue_count) {
                Bukkit.broadcast(Component.text("红方胜利"));
                red_win();
                release();
            } else {
                Bukkit.broadcast(Component.text("蓝方胜利"));
                blue_win();
                release();
            }
        }

        ScoreBoardBus.setPlayerScore("red_team", "global", red_count);
        ScoreBoardBus.setPlayerScore("blue_team", "global", blue_count);
    }

    private static void updateStatusBar() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            Team team = ScoreBoardBus.getPlayerTeam(player);
            if (team == null)
                continue;

            for (Player p : player.getWorld().getPlayers()) {
                if (ScoreBoardBus.getPlayerTeam(p).getName().equals(team.getName())) {
                    player.hideEntity(ServerBus.getPlugin(), PlayerDataBus.getPlayerRingDisplay(p));
                    player.hideEntity(ServerBus.getPlugin(), PlayerDataBus.getPlayerHealthDisplay(p));
                    player.hideEntity(ServerBus.getPlugin(), PlayerDataBus.getPlayerPostureDisplay(p));
                }
            }
        }
    }

    @Override
    public void run() {
        if (tick == ConfigBus.getValue("game_time", Integer.class)) {
            victory_detection(true);
            return;
        }

        showBossBar();
        updateStatusBar();
        victory_detection(false);
        ++tick;
    }
}
