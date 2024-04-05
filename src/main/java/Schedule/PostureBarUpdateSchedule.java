package Schedule;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import FunctionBus.ScoreBoardBus;
import net.kyori.adventure.text.Component;

public class PostureBarUpdateSchedule implements Runnable {
    private static final int MAX_POSTURE = 32;
    private static final int TICK_LOOP = 8;
    private static final char posture_base_char = 0xebc0;
    private static int tick = 0;

    public static void freeze(Player player) {
        ScoreBoardBus.setPlayerScore(player.getName(), "freeze_posture", 1);
    }

    public static void unfreeze(Player player) {
        ScoreBoardBus.setPlayerScore(player.getName(), "freeze_posture", 0);
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

    @Override
    public void run() {
        int posture;

        /* A stopwatch to countdown posture recover frozen */
        int hidden_posture;

        for (Player player : Bukkit.getOnlinePlayers()) {
            posture = ScoreBoardBus.getPlayerScore(player.getName(), "posture");
            hidden_posture = ScoreBoardBus.getPlayerScore(player.getName(), "hidden_posture");

            player.sendActionBar(Component.text(String.valueOf((char)(posture_base_char + posture))));

            if (tick < TICK_LOOP)
                continue;
            
            /* Test if posture recovery is frozen, for example on sprint */
            if (ScoreBoardBus.getPlayerScore(player.getName(), "freeze_posture") == 1)
                continue;

            if (hidden_posture > 0) {
                hidden_posture -= 1;
                ScoreBoardBus.setPlayerScore(player.getName(), "hidden_posture", hidden_posture);
            } else if (posture < MAX_POSTURE) {
                posture += 1;
                ScoreBoardBus.setPlayerScore(player.getName(), "posture", posture);
            }
        }

        if (tick >= TICK_LOOP)
            tick = 0;
        
        ++tick;
    }
}
