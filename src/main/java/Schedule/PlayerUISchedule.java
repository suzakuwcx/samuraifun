package Schedule;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scoreboard.Team;

import Assert.Config.State;
import Assert.Font.FontDatabase;
import DataBus.ConfigBus;
import DataBus.PlayerDataBus;
import FunctionBus.ScoreBoardBus;
import Task.AttackTask.RingShowTask;
import Task.AttackTask.SubTitleShowTask;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

public class PlayerUISchedule implements Runnable {
    private static Map<UUID, BossBar> playerbar;
    private static BossBar hinibar;

    static {
        playerbar = new HashMap<>();
        hinibar = BossBar.bossBar(Component.text(
            String.format("%c 攻击 %c 振刀 %c 防御 %c 重击 %c 收/拔刀 %c 垫步 %c + %c 肩撞",
                FontDatabase.HINI_LEFT_CLICK,
                FontDatabase.HINI_RIGHT_CLICK,
                FontDatabase.HINI_LONG_RIGHT_CLICK,
                FontDatabase.HINI_LONG_F_CLICK,
                FontDatabase.HINI_Q_CLICK,
                FontDatabase.HINI_SHIFT_CLICK,
                FontDatabase.HINI_LONG_RIGHT_CLICK, FontDatabase.HINI_SHIFT_CLICK
            )
        ), 1f, Color.PURPLE, Overlay.PROGRESS);
    }

    private void updateActionBar(Player player, State state) {
        Team team = ScoreBoardBus.getPlayerTeam(player);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ConfigBus.getValue("max_posture", Integer.class) - state.posture; ++i)
            builder.append(FontDatabase.POSTURE_EMPTY);

        for (int i = 0; i < state.posture; ++i)
            builder.append(FontDatabase.POSTURE_FULL);

        builder.append(FontDatabase.getCooldownFont(FontDatabase.SWORD_COOLDOWN_BASE, state.sword_cooldown / 20));
        
        switch (state.role) {
            case SAMURAI:
                builder.append(FontDatabase.getCooldownFont(FontDatabase.SAMURAI_SKILL_COOLDOWN_BASE, state.skill_cooldown / 20));
                break;
            case RONIN:
                builder.append(FontDatabase.getCooldownFont(FontDatabase.RONIN_SKILL_COOLDOWN_BASE, state.skill_cooldown / 20));
                break;
            case SHINBI:
                builder.append(FontDatabase.getCooldownFont(FontDatabase.SHINBI_SKILL_COOLDOWN_BASE, state.skill_cooldown / 20));
                break;
            case SOHEI:
                builder.append(FontDatabase.getCooldownFont(FontDatabase.SOHEI_SKILL_COOLDOWN_BASE, state.skill_cooldown / 20));
                break;
            default:
                builder.append(FontDatabase.getCooldownFont(FontDatabase.SAMURAI_SKILL_COOLDOWN_BASE, state.skill_cooldown / 20));
                break;
        }

        builder.append(FontDatabase.getCooldownFont(FontDatabase.BOW_COOLDOWN_BASE, state.bow_cooldown / 20));

        for (int i = 0; i < state.health; ++i) {
            if (team == null || team.getName().equals("red_team")) {
                builder.append(FontDatabase.HEART_FULL);
            } else {
                builder.append(FontDatabase.HEART_FULL_BLUE);
            }
        }

        for (int i = 0; i < ConfigBus.getValue("max_health", Integer.class) - state.health; ++i) {
            if (team == null || team.getName().equals("red_team")) {
                builder.append(FontDatabase.HEART_EMPTY);
            } else {
                builder.append(FontDatabase.HEART_EMPTY_BLUE);
            }
        }

        player.sendActionBar(Component.text(builder.toString()));
    }


    public static void setPlayerMainRing(Player player, String text) {
        State state = PlayerStateMachineSchedule.getPlayerState(player);
        state.status_ring = text;
        
        if (RingShowTask.isPlayerShowingSideRing(player))
            return;
        
        TextDisplay display = PlayerDataBus.getPlayerRingDisplay(player);
        display.text(Component.text(text));
    }

    public static void setPlayerMainRing(Player player, char text) {
        setPlayerMainRing(player, String.valueOf(text));
    }

    public static void setPlayerSideRing(Player player, String text, int tick) {
        RingShowTask.execute(player, text, tick);
    }

    public static void setPlayerSideRing(Player player, char text, int tick) {
        setPlayerSideRing(player, String.valueOf(text), tick);
    }

    public static void setPlayerMainSubtitle(Player player, String text) {
        SubTitleShowTask.setMainSubtitle(player, text);
    }

    public static void setPlayerMainSubtitle(Player player, char text) {
        setPlayerMainSubtitle(player, String.valueOf(text));
    }

    public static void setPlayerSideSubtitle(Player player, String text, int tick) {
        SubTitleShowTask.execute(player, text, tick);
    }

    public static void setPlayerSideSubtitle(Player player, char text, int tick) {
        setPlayerSideSubtitle(player, String.valueOf(text), tick);
    }

    private void updateRing(Player player, State state) {
        TextDisplay display;
        Team team = ScoreBoardBus.getPlayerTeam(player);

        display = PlayerDataBus.getPlayerHealthDisplay(player);
        if (display != null) {
            if (team == null || team.getName().equals("red_team"))
                display.text(Component.text(FontDatabase.getRingFont(FontDatabase.HEALTH_RING_BASE, state.health)));
            else
                display.text(Component.text(FontDatabase.getRingFont(FontDatabase.HEALTH_RING_BASE_BLUE, state.health)));
        }
        
        display = PlayerDataBus.getPlayerPostureDisplay(player);
        if (display != null)
            display.text(Component.text(FontDatabase.getRingFont(FontDatabase.POSTURE_RING_BASE, state.posture)));
    }

    private void updateTitle(Player player, State state) {
        Component title = Component.text(state.main_title);
        Component subtitle = Component.text(state.sub_title);

        player.showTitle(Title.title(title, subtitle, Times.times(Duration.ofSeconds(0), Duration.ofSeconds(40), Duration.ofSeconds(10))));
    }

    @Override
    public void run() {
        State state;
        for (Player player : Bukkit.getOnlinePlayers()) {
            state = PlayerStateMachineSchedule.getPlayerState(player);
            updateActionBar(player, state);
            updateRing(player, state);
            updateTitle(player, state);
        }
    }

    public static void init(Player player) {
        BossBar bar = BossBar.bossBar(Component.text(""), 1f, Color.PURPLE, Overlay.PROGRESS);
        playerbar.put(player.getUniqueId(), bar);
        refreshPlayerFirstBossbar(player);
    }

    public static BossBar getPlayerFirstBossbar(Player player) {
        return playerbar.get(player.getUniqueId());
    }

    public static void refreshPlayerFirstBossbar(Player player) {
        BossBar bar = playerbar.get(player.getUniqueId());
        player.showBossBar(bar);
        player.showBossBar(hinibar);
    }
}
