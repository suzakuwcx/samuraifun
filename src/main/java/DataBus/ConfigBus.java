package DataBus;

import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigBus {
    private static FileConfiguration conf;
    private static Map<String, Object> default_mapper; 

    static {
        default_mapper = Map.ofEntries(
            Map.entry("basic_attack_range", 4.1),
            Map.entry("deflect_tick", 4),
            Map.entry("max_health", 7),
            Map.entry("max_posture", 7),
            Map.entry("sword_cooldown", 12 * 20 - 1),
            Map.entry("skill_cooldown", 12 * 20 - 1),
            Map.entry("bow_cooldown", 12 * 20 - 1),
            Map.entry("game_time", 30 * 60 * 20)
        );
    }

    public static void onEnable(FileConfiguration configuration) {
        conf = configuration;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T getValue(String path, Class<T> clazz) {
        if (!conf.contains(path))
            setValue(path, (T) default_mapper.get(path), clazz);

        return (T) conf.get(path);
    }

    public static <T> void setValue(String path, T value, Class<T> clazz) {
        conf.set(path, value);
    }

    public static Set<String> getKeys() {
        return conf.getKeys(false);
    }
}
