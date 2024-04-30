package Assert.Font;

public class FontDatabase {
    public static final char HEART_FULL = 0xe900;
    public static final char HEART_EMPTY = 0xe901;
    public static final char HEART_BAN = 0xe902;

    public static final char POSTURE_FULL = 0xe903;
    public static final char POSTURE_EMPTY = 0xe904;
    public static final char POSTURE_BAN = 0xe905;

    public static final char SWORD_COOLDOWN_BASE = 0xe906;
    public static final char BOW_COOLDOWN_BASE = 0xe913;
    public static final char SAMURAI_SKILL_COOLDOWN_BASE = 0xe91f;
    public static final char RONIN_SKILL_COOLDOWN_BASE = 0xe92b;
    public static final char SHINBI_SKILL_COOLDOWN_BASE = 0xe93f;
    public static final char SOHEI_SKILL_COOLDOWN_BASE = 0xe94b;

    public static final char CHARGED_ATTACK_CHARGING_BASE = 0xe957;

    public static final char SWORD_NO_CHOSEN = 0xea00;
    public static final char SWORD_CHOSEN = 0xea01;

    public static final char SKILL_ITEM_NO_CHOSEN = 0xea02;
    public static final char SKILL_ITEM_CHOSEN = 0xea03;

    public static final char BOW_NO_CHOSEN = 0xea04;
    public static final char BOW_CHOSEN = 0xea05;

    public static final char HEALTH_RING_BASE = 0xea14;
    public static final char POSTURE_RING_BASE = 0xea1c;

    public static final char STATUS_RING_EMPTY_SANURAI = 0xea24;
    public static final char STATUS_RING_EMPTY_RONIN = 0xea25;
    public static final char STATUS_RING_EMPTY_SHINBI = 0xea26;
    public static final char STATUS_RING_EMPTY_SOHEI = 0xea27;

    public static final char HINI_LEFT_CLICK = 0xea0e;
    public static final char HINI_RIGHT_CLICK = 0xea0f;
    public static final char HINI_LONG_RIGHT_CLICK = 0xea10;
    public static final char HINI_LONG_F_CLICK = 0xea11;
    public static final char HINI_Q_CLICK = 0xea12;
    public static final char HINI_SHIFT_CLICK = 0xea13;

    public static char getRingFont(char base, int value) {
        return (char) (base + value);
    }

    public static char getCooldownFont(char base, int remain) {
        return (char) (base + 12 - remain);
    }

    public static char getChargingAttackFont(int value) {
        return (char) (CHARGED_ATTACK_CHARGING_BASE + value);
    }
}
