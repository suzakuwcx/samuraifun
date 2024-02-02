package Schedule;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PlayerRealVelocityUpdateSchedule implements Runnable {
    public static class Status {
        public Location location;
        public Vector velocity;
        public long time;
    }

    public static Map<Player, Status> player_status_mapper = new HashMap<>();

    public static void updateNewVelocity(Player player) {
        Status status = player_status_mapper.get(player);
        if (status == null) {
            status = new Status();
            status.time = System.currentTimeMillis();
            status.location = player.getLocation();
        }
        Location location = player.getLocation();
        long time = System.currentTimeMillis();
        Vector velocity = player.getVelocity();
        velocity.setX((location.getX() - status.location.getX()) * 1000 / (time - status.time) / 21.59);
        velocity.setZ((location.getZ() - status.location.getZ()) * 1000 / (time - status.time) / 21.59);

        status.location = location;
        status.time = time;
        status.velocity = velocity;
        player_status_mapper.put(player, status);
    }

    public static Vector getVelocity(Player player) {
        Status status = player_status_mapper.get(player);
        if (status == null)
            return player.getVelocity();
        
        Vector velocity = player_status_mapper.get(player).velocity;
        velocity.setY(player.getVelocity().getY());
        return velocity;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateNewVelocity(player);
        }
    }
}
