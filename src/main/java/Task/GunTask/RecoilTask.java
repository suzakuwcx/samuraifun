package Task.GunTask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import FunctionBus.ServerBus;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.entity.RelativeMovement;

public class RecoilTask implements Runnable {
    private static Set<RelativeMovement> RELATIVE_FLAGS = new HashSet<>(Arrays.asList(
        RelativeMovement.X,
        RelativeMovement.Y,
        RelativeMovement.Z,
        RelativeMovement.X_ROT,

        RelativeMovement.Y_ROT));

    private Player player;
    private float yaw;
    private float pitch;
    private int duration;

    private RecoilTask(Player player, float yaw, float pitch, int duration) {
        this.player = player;
        this.duration = duration;
        this.yaw = yaw / duration;
        this.pitch = pitch / duration;
    }

    public static void execute(Player player, float yaw, float pitch, int duration) {
        Bukkit.getScheduler().runTask(ServerBus.getPlugin(), new RecoilTask(player, yaw, pitch, duration));
    }

    @Override
    public void run() {
        if (duration <= 0)
            return;

        ((CraftPlayer) player).getHandle().connection.send(new ClientboundPlayerPositionPacket(0, 0, 0, yaw, pitch, RELATIVE_FLAGS, 0));

        --duration;
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
    }
}
