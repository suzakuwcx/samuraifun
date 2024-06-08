package Assert.Config;

import org.bukkit.entity.Player;

import DataBus.ConfigBus;
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
    public String status_ring = "";
    public String main_title = "";
    public String sub_title = "";

    public int health = 7;
    public int posture = 7;
    public Role role = Role.SAMURAI;
    public int sword_cooldown = ConfigBus.getValue("sword_cooldown", Integer.class);
    public int bow_cooldown = ConfigBus.getValue("skill_cooldown", Integer.class);
    public int skill_cooldown = ConfigBus.getValue("skill_cooldown", Integer.class);
    public int current_sword_frame = 1001;
    public int dash_cooldown = 4 * 20;
    public boolean is_invincible_frame = false;

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
