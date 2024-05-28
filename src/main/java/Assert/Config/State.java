package Assert.Config;

import org.bukkit.entity.Player;

import Task.StateTask.BaseStateTask;
import Task.StateTask.NormalStateTask;

public class State {
    public boolean left_click = false;
    public boolean right_click = false;
    public boolean fclick = false;

    public boolean normal_attack = false;
    public boolean space = false;
    public boolean shift = false;
    
    public BaseStateTask state;

    public int health = 7;
    public int posture = 7;
    public Role role = Role.SAMURAI;
    public int sword_cooldown = PlayerConfig.SWORD_COOLDOWN;
    public int bow_cooldown = PlayerConfig.BOW_COOLDOWN;
    public int skill_cooldown = PlayerConfig.SKILL_COOLDOWN;
    public int current_sword_frame = 1001;
    public int dash_cooldown = 4 * 20;

    public int charging = 0;

    public State(Player player) {
        state = new NormalStateTask(player, role);
    }

    public void refresh() {
        left_click = false;
        right_click = false;
        normal_attack = false;
        space = false;
    }
}
