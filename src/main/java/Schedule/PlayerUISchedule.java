package Schedule;

import java.time.Duration;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Assert.Config.PlayerConfig;
import Assert.Config.State;
import Assert.Font.FontDatabase;
import DataBus.PlayerDataBus;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

public class PlayerUISchedule implements Runnable {
    private static BossBar hinibar;
    private static BossBar timebar;

    static {
        timebar = BossBar.bossBar(Component.text(""), 1f, Color.RED, Overlay.PROGRESS);

        hinibar = BossBar.bossBar(Component.text(
            String.format("%c 攻击 %c 振刀 %c 防御 %c 重击 %c 收/拔刀 %c 垫步",
                FontDatabase.HINI_LEFT_CLICK,
                FontDatabase.HINI_RIGHT_CLICK,
                FontDatabase.HINI_LONG_RIGHT_CLICK,
                FontDatabase.HINI_LONG_F_CLICK,
                FontDatabase.HINI_Q_CLICK,
                FontDatabase.HINI_SHIFT_CLICK
            )
        ), 1f, Color.WHITE, Overlay.PROGRESS);
    }

    private void updateActionBar(Player player, State state) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < PlayerConfig.MAX_POSTURE - state.posture; ++i)
            builder.append(FontDatabase.POSTURE_EMPTY);

        for (int i = 0; i < state.posture; ++i)
            builder.append(FontDatabase.POSTURE_FULL);

        builder.append(FontDatabase.getCooldownFont(FontDatabase.SWORD_COOLDOWN_BASE, state.sword_cooldown));
        
        switch (state.role) {
            case SAMURAI:
                builder.append(FontDatabase.getCooldownFont(FontDatabase.SAMURAI_SKILL_COOLDOWN_BASE, state.skill_cooldown));
                break;
            case RONIN:
                builder.append(FontDatabase.getCooldownFont(FontDatabase.RONIN_SKILL_COOLDOWN_BASE, state.skill_cooldown));
                break;
            case SHINBI:
                builder.append(FontDatabase.getCooldownFont(FontDatabase.SHINBI_SKILL_COOLDOWN_BASE, state.skill_cooldown));
                break;
            case SOHEI:
                builder.append(FontDatabase.getCooldownFont(FontDatabase.SOHEI_SKILL_COOLDOWN_BASE, state.skill_cooldown));
                break;
            default:
                builder.append(FontDatabase.getCooldownFont(FontDatabase.SAMURAI_SKILL_COOLDOWN_BASE, state.skill_cooldown));
                break;
        }

        builder.append(FontDatabase.getCooldownFont(FontDatabase.BOW_COOLDOWN_BASE, state.bow_cooldown));

        for (int i = 0; i < state.health; ++i)
            builder.append(FontDatabase.HEART_FULL);

        for (int i = 0; i < PlayerConfig.MAX_HEALTH - state.health; ++i)
            builder.append(FontDatabase.HEART_EMPTY);

        player.sendActionBar(Component.text(builder.toString()));
    }

    private void updateRing(Player player, State state) {
        PlayerDataBus.getPlayerHealthDisplay(player).text(Component.text(FontDatabase.getRingFont(FontDatabase.HEALTH_RING_BASE, state.health)));
        PlayerDataBus.getPlayerPostureDisplay(player).text(Component.text(FontDatabase.getRingFont(FontDatabase.POSTURE_RING_BASE, state.posture)));
    }

    private void updateTitle(Player player, State state) {
        Component title = Component.text("");
        Component subtitle;
        if (state.charging <= 0)
            subtitle = Component.text("");
        else
            subtitle = Component.text(FontDatabase.getChargingAttackFont(state.charging));

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
        player.showBossBar(timebar);
        player.showBossBar(hinibar);
    }
}
