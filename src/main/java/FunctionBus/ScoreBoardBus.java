package FunctionBus;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.kyori.adventure.text.Component;

public class ScoreBoardBus {
    private static Scoreboard board;

    static {
        board = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    private static Objective getOrCreateObjective(String name) {
        Objective obj = board.getObjective(name);
        if (obj == null) {
            board.registerNewObjective(name, Criteria.DUMMY, Component.text(name), RenderType.INTEGER);
            obj = board.getObjective(name);
        }
        return obj;
    }

    public static int getPlayerScore(String entry, String name) {
        Objective obj = getOrCreateObjective(name);
        Score score = obj.getScore(entry);
        return score.getScore();
    }

    public static void setPlayerScore(String entry, String name, int score) {
        Objective obj = getOrCreateObjective(name);
        Score s = obj.getScore(entry);
        s.setScore(score);
    }

    public static void setObjDisplay(String name, DisplaySlot slot) {
        Objective obj = getOrCreateObjective(name);
        obj.setDisplaySlot(slot);
    }

    public static SortedSet<Map.Entry<String, Integer>> getRank(String name) {
        TreeMap<String, Integer> map = new TreeMap<>();
        Objective obj = getOrCreateObjective(name);
        for (String entry : board.getEntries()) {
            Score score = obj.getScore(entry);
            int s = score.getScore();
            if (s != 0)
                map.put(entry, s);
        }

        SortedSet<Map.Entry<String, Integer>> sortedEntries = new TreeSet<Map.Entry<String, Integer>>(
            new Comparator<Map.Entry<String, Integer>>() {
                @Override public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                    int res = -e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1;
                }
            }
        );

        sortedEntries.addAll(map.entrySet());

        return sortedEntries;
    }

    public static boolean isPlayerSameTeam(Player first, Player second) {
        Team team = board.getEntityTeam(first);
        if (team == null)
            return false;
        return team.hasEntity(second);
    }
}
