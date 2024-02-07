package Task.GunTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
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

    private boolean is_mode_recover = false; /* Is current mode recover or recoil */
    private float max_recoil = 5f; /* maximun recoil distance */
    private float recoil_on_recover_begin; /* The recoil on recover mode begin */
    private float current_recoil = 0f; /* The value of accumulated recoil */
    private int tick_count = 6; /* ticks number that will be taken to finish */
    private int current_tick = 0; /* ticks that already process the recoil */
    /* minimum number of ticks tolerated without shooting, if exceed this value, treat player stop shooting */
    private int shoot_period = 2;
    private int current_shoot_period; /* tick that player not shooting  */

    static {
        task_mapper = new HashMap<>();
    }

    public PlayerPerspectiveRecoilTask(Player player, float max_recoil, int tick_count, int shoot_period) {
        this.player = player;
        this.max_recoil = max_recoil;
        this.tick_count = tick_count;
        this.shoot_period = shoot_period;
        this.current_shoot_period = shoot_period;
    }

    private void recoil_core()
    {
        if (current_recoil >= max_recoil)
            return;

        ((CraftPlayer) player).getHandle().connection.send(new ClientboundPlayerPositionPacket(0, 0, 0, 0, -max_recoil / tick_count, RELATIVE_FLAGS, 0));
        current_recoil += max_recoil / tick_count;
    }

    private void recover_core()
    {
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundPlayerPositionPacket(0, 0, 0, 0, recoil_on_recover_begin / tick_count, RELATIVE_FLAGS, 0));
        current_recoil -= recoil_on_recover_begin / tick_count;
    }

    public static void do_recoil(Player player, float max_recoil, int tick_count, int shoot_period)
    {
        PlayerPerspectiveRecoilTask task;
        task = task_mapper.get(player);
        if (task != null) {
            task.current_shoot_period = task.shoot_period;
            if (task.is_mode_recover == true) {
                /* If is on recovering status, stop recovering and continue add recoil */ 
                task.is_mode_recover = false;
                task.current_tick = 0;
            }
            return;
        }
        task = new PlayerPerspectiveRecoilTask(player, max_recoil, tick_count, shoot_period);
        task_mapper.put(player, task);
        task.task_id = Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task).getTaskId();
    }

    private boolean is_shooting() {
        if (is_mode_recover == true)
            return false;
        if (current_shoot_period == 0)
            return false;
        return true;
    }

    private void on_shooting() {
        if (current_tick != tick_count) {
            recoil_core();
            ++current_tick;
        }

        --current_shoot_period;
    }

    private boolean is_recovering() {
        if (is_mode_recover == false)
            return false;
        if (current_tick == tick_count)
            return false;
        return true;
    }

    private void on_recovering() {
        recover_core();
        ++current_tick;
    }

    private boolean is_begin_stoping_shooting() {
        if (is_mode_recover == true)
            return false;
        if (current_shoot_period != 0)
            return false;
        return true;
    }

    private void on_begin_stop_shooting() {
        is_mode_recover = true;
        current_tick = 0;
        recoil_on_recover_begin = current_recoil;
    }

    private boolean is_finishing_recover() {
        if (is_mode_recover == false)
            return false;
        if (current_tick != tick_count)
            return false;
        return true;
    }

    private void on_finishing_recover() {
        task_mapper.remove(player);
    }

    @Override
    public void run() {
        if (is_finishing_recover()) {
            on_finishing_recover();
            return;
        }

        if (is_shooting()) {
            on_shooting();
        } else if (is_recovering()) {
            on_recovering();
        } else if (is_begin_stoping_shooting()) {
            on_begin_stop_shooting();
        }

        this.task_id = Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1).getTaskId();
    }
    
}
