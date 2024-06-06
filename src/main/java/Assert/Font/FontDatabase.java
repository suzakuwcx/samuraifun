package Assert.Font;

import Assert.Config.Role;

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

    public static final char STATUS_RING_DEFENSE = 0xea2e;
    public static final char STATUS_RING_ATTACK = 0xea28;
    public static final char STATUS_RING_STUN = 0xea33;
    public static final char STATUS_RING_CHARGING_0 = 0xea29;
    public static final char STATUS_RING_CHARGING_1 = 0xea2a;
    public static final char STATUS_RING_CHARGING_2 = 0xea2b;
    public static final char STATUS_RING_CHARGING_3 = 0xea2c;
    public static final char STATUS_RING_CHARGED_WARNING = 0xea2d;
    public static final char STATUS_RING_THRUST_ATTACK_WARNING = 0xea35;

    public static final char HINI_LEFT_CLICK = 0xea0e;
    public static final char HINI_RIGHT_CLICK = 0xea0f;
    public static final char HINI_LONG_RIGHT_CLICK = 0xea10;
    public static final char HINI_LONG_F_CLICK = 0xea11;
    public static final char HINI_Q_CLICK = 0xea12;
    public static final char HINI_SHIFT_CLICK = 0xea13;

    public static final char STATUS_RING_POSTURE_CRASH = 0xea32;
    public static final char STATUS_RING_DASH_IFRAM = 0xea34;
    public static final char STATUS_RING_STOP_DEFENSE = 0xea37;
    public static final char STATUS_RING_SUCCESS_DEFENSE = 0xea2f;
    public static final char STATUS_RING_SUCCESS_DEFLECT = 0xea31;
    public static final char STATUS_RING_CAN_DEFLECT = 0xea30;

    public static final char STATUS_SUBTITLE_DEFENSE = 0xe980;
    public static final char STATUS_SUBTITLE_SUCCESS_DEFENSE = 0xe981;
    public static final char STATUS_SUBTITLE_CAN_DEFLECT = 0xe982;
    public static final char STATUS_SUBTITLE_SUCCESS_DEFLECT = 0xe983;
    public static final char STATUS_SUBTITLE_POSTURE_CRASH = 0xe984;
    public static final char STATUS_SUBTITLE_STUN = 0xe985;
    public static final char STATUS_SUBTITLE_THRUST_ATTACK_WARNING = 0xe986;
    public static final char STATUS_SUBTITLE_DASH = 0xe986;
    public static final char STATUS_SUBTITLE_NO_POSTURE = 0xe988;
    public static final char STATUS_SUBTITLE_IN_CD = 0xe989;

    public static final char ANIMATION_DEAD = 0xea36;
    public static final char STATUS_MAINTITLE_KILL = 0xe98a;

    public static char getRingFont(Role role) {
        switch(role) {
            case RONIN:
                return STATUS_RING_EMPTY_RONIN;
            case SHINBI:
                return STATUS_RING_EMPTY_SHINBI;
            case SOHEI:
                return STATUS_RING_EMPTY_SOHEI;
            case COMMON:
            case SAMURAI:
            default:
                return STATUS_RING_EMPTY_SANURAI;
        }
    } 

    public static char getRingFont(char base, int value) {
        return (char) (base + value);
    }

    public static char getCooldownFont(char base, int remain) {
        return (char) (base + 11 - remain);
    }

    public static char getChargingAttackFont(int value) {
        if (value <= 0)
            return ' ';
        else
            return (char) (CHARGED_ATTACK_CHARGING_BASE + value);
    }
}
