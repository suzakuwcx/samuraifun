package Assert.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.bukkit.entity.Player;

public class PlayerMessageLogHandler extends Handler{
    private Player player;
    private static Map<Player, PlayerMessageLogHandler> map = new HashMap<>();

    private PlayerMessageLogHandler(Player player) {
        this.player = player;
    }

    public static boolean hasHandler(Player player) {
        return map.containsKey(player);
    }

    public static PlayerMessageLogHandler getHandler(Player player) {
        PlayerMessageLogHandler handler = map.get(player);
        if (handler == null) {
            handler = new PlayerMessageLogHandler(player);
            map.put(player, handler);
        }
        return handler;
    }

    @Override
    public void publish(LogRecord record) {
        player.sendMessage(record.getMessage());
    }

    @Override
    public void close() throws SecurityException {
        return;
    }

    @Override
    public void flush() {
        return;
    }
    
}
