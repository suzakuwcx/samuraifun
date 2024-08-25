package Task.GunTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import FunctionBus.ServerBus;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.entity.RelativeMovement;

public class PlayerPerspectiveRecoilTask implements Runnable {
    private static Map<Player, PlayerPerspectiveRecoilTask> task_mapper;
    private static Set<RelativeMovement> RELATIVE_FLAGS = new HashSet<>(Arrays.asList(
        RelativeMovement.X,
        RelativeMovement.Y,
        RelativeMovement.Z,
        RelativeMovement.X_ROT,
        RelativeMovement.Y_ROT));
    
    private Player player;
    private int task_id = 0;

    private float[] vertical_recoil_table = {5f / 6, 5f / 6, 5f / 6, 5f / 6, 5f / 6, 5f / 6};
    private float[] vertical_burst_recoil_table = {0f, 0f, 0f, 0f, 0.3f, 0.3f, 0f, 0f, -0.3f, -0.3f};
    
    private float[] level_recoil_table = {0.2f, -0.2f, -0.2f, 0.2f, 0.3f, 0.3f, -0.3f, -0.3f, -0.3f, -0.3f, 0.3f, 0.3f};
    private float[] level_burst_recoil_table = {0f, 0f, 0f, 0f, 1f, 1f, 0f, 0f, -1f, -1f};

    private boolean is_mode_recover = false;

    private int vertical_max_recoil_tick = vertical_recoil_table.length;
    private int vertical_max_recoil_burst_tick = vertical_burst_recoil_table.length;
    private int vertical_recoil_tick = 0;
    private int vertical_recoil_burst_tick = 0;
    private float vertical_current_recoil = 0;
    private float recover_begin_vertical_recoil = 0;
    
    private int level_max_recoil_tick = level_recoil_table.length;
    private int level_max_recoil_burst_tick = level_burst_recoil_table.length;
    private int level_recoil_tick = 0;
    private int level_recoil_burst_tick = 0;
    private float level_current_recoil = 0;
    private float recover_begin_level_recoil = 0;

    private int recover_max_tick = 6;
    private int recover_tick = 0;

    private int shoot_period = 2;
    private int current_shoot_period; /* tick that player not shooting  */

    static {
        task_mapper = new HashMap<>();
    }

    public PlayerPerspectiveRecoilTask(Player player) {
        this.player = player;
    }

    private float get_vertical_recoil(int index, int burst_index)
    {
        if (index == vertical_max_recoil_tick)
            return get_vertical_recoil_burst(burst_index);
        return vertical_recoil_table[index];
    }

    private float get_level_recoil(int index, int burst_index)
    {
        if (index == level_max_recoil_tick)
            return get_level_recoil_burst(burst_index);
        return level_recoil_table[index];
    }

    private float get_vertical_recoil_burst(int index)
    {
        return vertical_burst_recoil_table[index];
    }

    private float get_level_recoil_burst(int index)
    {
        return level_burst_recoil_table[index];
    }

    private void recoil_core(float level, float vertical)
    {
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundPlayerPositionPacket(0, 0, 0, -level, -vertical, RELATIVE_FLAGS, 0));
    }

    private void recover_core(float level, float vertical)
    {
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundPlayerPositionPacket(0, 0, 0, level, vertical, RELATIVE_FLAGS, 0));
    }

    public static void do_recoil(Player player)
    {
        PlayerPerspectiveRecoilTask task;
        task = task_mapper.get(player);
        if (task != null) {
            task.is_mode_recover = false;
            task.current_shoot_period = task.shoot_period;
            return;
        }
        task = new PlayerPerspectiveRecoilTask(player);
        task_mapper.put(player, task);
        task.task_id = Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task).getTaskId();
    }

    private boolean is_shooting() 
    {
        if (is_mode_recover == true)
            return false;
        if (current_shoot_period < 0)
            return false;
        return true;
    }

    private void on_shooting()
    {
        if (vertical_recoil_tick < vertical_max_recoil_tick)
            ++vertical_recoil_tick;
        else {
            ++vertical_recoil_burst_tick;
            if (vertical_recoil_burst_tick == vertical_max_recoil_burst_tick)
                vertical_recoil_burst_tick = 0;
        }

        if (level_recoil_tick < level_max_recoil_tick)
            ++level_recoil_tick;
        else {
            ++level_recoil_burst_tick;
            if (level_recoil_burst_tick == level_max_recoil_burst_tick)
                level_recoil_burst_tick = 0;
        }

        float vertical = get_vertical_recoil(vertical_recoil_tick, vertical_recoil_burst_tick);
        float level = get_level_recoil(level_recoil_tick, level_recoil_burst_tick);
        vertical_current_recoil += vertical;
        level_current_recoil += level;
        
        recoil_core(level, vertical);

        current_shoot_period -= 1;
    }

    private boolean is_begin_stoping_shooting()
    {
        if (is_mode_recover == true)
            return false;
        if (current_shoot_period >= 0)
            return false;
        return true;
    }

    private void on_begin_stop_shooting()
    {
        is_mode_recover = true;
        recover_tick = recover_max_tick;
        recover_begin_vertical_recoil = vertical_current_recoil;
        recover_begin_level_recoil = level_current_recoil;
        vertical_recoil_burst_tick = 0;
        level_recoil_burst_tick = 0;
    }

    private boolean is_recovering()
    {
        if (is_mode_recover == false)
            return false;

        return true;
    }

    private void on_recovering()
    {
        if (level_recoil_tick > 0)
            --level_recoil_tick;
        if (vertical_recoil_tick > 0)
            --vertical_recoil_tick;

        float level = recover_begin_level_recoil / recover_max_tick;
        float vertical = recover_begin_vertical_recoil / recover_max_tick;
        recover_core(level, vertical);
        level_current_recoil -= level;
        vertical_current_recoil -= vertical;
        --recover_tick;
    }

    private boolean is_player_exist()
    {
        if (player.isOnline())
            return true;
        return false;
    }

    private boolean is_finishing_shooting() {
        if (is_mode_recover == false)
            return false;

        if (recover_tick != 0)
            return false;

        return true;
    }

    @Override
    public void run() {
        if (!is_player_exist() || is_finishing_shooting()) {
            task_mapper.remove(player);
            return;
        }

        if (is_shooting())
            on_shooting();
        else if (is_begin_stoping_shooting())
            on_begin_stop_shooting();
        else if (is_recovering())
            on_recovering();

        this.task_id = Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1).getTaskId();
    }
    
}
