package Schedule;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import FunctionBus.ScoreBoardBus;
import FunctionBus.ServerBus;
import net.kyori.adventure.text.Component;

public class PostureBarUpdateSchedule implements Runnable {
    private static final int MAX_POSTURE = 250;
    private static final int TICK_LOOP = 8;
    private static final char posture_base_char = 0xebc0;
    private static int tick = 0;

    public static void freeze(Player player) {
        ScoreBoardBus.setPlayerScore(player.getName(), "freeze_posture", 1);
    }

    public static boolean isFreezed(Player player) {
        int freeze = ScoreBoardBus.getPlayerScore(player.getName(), "freeze_posture");
        if (freeze == 0)
            return false;
        return true;
    }

    public static void unfreeze(Player player) {
        ScoreBoardBus.setPlayerScore(player.getName(), "freeze_posture", 0);
    }

    public static void boost(Player player) {
        ScoreBoardBus.setPlayerScore(player.getName(), "boost_posture", 1);
    }

    public static boolean isBoost(Player player) {
        int boost = ScoreBoardBus.getPlayerScore(player.getName(), "boost_posture");
        if (boost == 0)
            return false;
        return true;
    }

    public static void unboost(Player player) {
        ScoreBoardBus.setPlayerScore(player.getName(), "boost_posture", 0);
    }

    public static void damage(Player player, int dmg, int stagger) {
        int posture = ScoreBoardBus.getPlayerScore(player.getName(), "posture");
        int hidden_posture = ScoreBoardBus.getPlayerScore(player.getName(), "hidden_posture");

        posture -= dmg;
        if (posture < 0)
            posture = 0;
        
        if (hidden_posture < stagger) {
            hidden_posture += stagger;
            if (hidden_posture > stagger)
                hidden_posture = stagger;
        }
        
        ScoreBoardBus.setPlayerScore(player.getName(), "posture", posture);
        ScoreBoardBus.setPlayerScore(player.getName(), "hidden_posture", hidden_posture);
    }

    private static int getPlayerPosture(Player player) {
        return ScoreBoardBus.getPlayerScore(player.getName(), "posture");
    }

    private static int getPlayerHiddenPosture(Player player) {
        return ScoreBoardBus.getPlayerScore(player.getName(), "hidden_posture");
    }

    private static void setPlayerPosture(Player player, int value) {
        ScoreBoardBus.setPlayerScore(player.getName(), "posture", value);
    }

    private static void setPlayerHiddenPosture(Player player, int value) {
        ScoreBoardBus.setPlayerScore(player.getName(), "hidden_posture", value);
    }

    private static boolean isPostureMax(Player player) {
        int posture = getPlayerPosture(player);
        if (posture == MAX_POSTURE)
            return true;

        return false;
    }

    private static void updatePosture(Player player) {
        int posture;

        /* A stopwatch to countdown posture recover frozen */
        int hidden_posture;

        posture = getPlayerPosture(player);
        hidden_posture = getPlayerHiddenPosture(player);

        if (!isPostureMax(player))
            player.sendActionBar(Component.text(String.valueOf((char)(posture_base_char + posture))));

        if (tick < TICK_LOOP)
            return;
        
        /* Test if posture recovery is frozen, for example on sprint */
        if (ScoreBoardBus.getPlayerScore(player.getName(), "freeze_posture") == 1)
            return;

        if (hidden_posture > 0) {
            hidden_posture -= 1;
            setPlayerHiddenPosture(player, hidden_posture);
        } else if (posture < MAX_POSTURE) {
            posture += 1;
            setPlayerPosture(player, posture);
        }
    }

    private static Map<Player, Player> player_opponent_mapper = new HashMap<>();
    private static Map<Player, BossBar> player_bossbar_mapper = new HashMap<>();

    private static boolean isOpponentValid(Player player, RayTraceResult result) {
        if (result == null)
            return false;
        if (result.getHitEntity().getType() != EntityType.PLAYER)
            return false;
        if ((Player) result.getHitEntity() == player)
            return false;

        return true;
    }

    private static void showOpponentPosture(Player player) {
        RayTraceResult result = player.getWorld().rayTraceEntities(ServerBus.getDirectionLocation(player.getEyeLocation(), 0.5), player.getEyeLocation().getDirection(), 15);
        Player opponent = null;

        if (isOpponentValid(player, result))
            opponent = (Player) result.getHitEntity();

        if (!player_opponent_mapper.containsKey(player) && opponent == null)
            return;

        if (opponent == null)
            opponent = player_opponent_mapper.get(player);

        if (!opponent.equals(player_opponent_mapper.get(player)))
            player_opponent_mapper.put(player, opponent);
        
        BossBar bar = player_bossbar_mapper.getOrDefault(player, null);
        if (bar == null) {
            bar = Bukkit.createBossBar("⚔", BarColor.RED, BarStyle.SOLID);
            player_bossbar_mapper.put(player, bar);
            bar.setVisible(true);
            bar.addPlayer(player);
        }
        
        bar.setProgress((double)getPlayerPosture(opponent) / (double) MAX_POSTURE);
    }

    private static void setPlayerCanSprinting(Player player) {
        if (isPostureMax(player))
            player.setFoodLevel(20);
        else
            player.setFoodLevel(5);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePosture(player);
            showOpponentPosture(player);
            setPlayerCanSprinting(player);
        }

        if (tick >= TICK_LOOP)
            tick = 0;
        
        ++tick;
    }
}
