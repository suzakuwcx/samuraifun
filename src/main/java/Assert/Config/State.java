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
    public int sword_cooldown = 12;
    public int bow_cooldown = 12;
    public int skill_cooldown = 12;

    public State(Player player) {
        state = new NormalStateTask(player);
    }

    public void refresh() {
        left_click = false;
        right_click = false;
        normal_attack = false;
        space = false;
    }
}
