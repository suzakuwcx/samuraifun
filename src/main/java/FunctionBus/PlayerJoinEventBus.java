package FunctionBus;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Item.Sword;
import DataBus.ConfigBus;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;
import Task.GameTask.GameTask;

public class PlayerJoinEventBus {
    public static void onBusTrigger(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerDataBus.addPlayerItemDisplay(player);
        PlayerStateMachineSchedule.init(event.getPlayer());
        PlayerUISchedule.init(player);

        /* Init player potion */
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, PotionEffect.INFINITE_DURATION, 3, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION, 3, false, false));

        /* Init player attribute */
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
        player.setWalkSpeed(0.2f);
        player.setShieldBlockingDelay(ConfigBus.getValue("deflect_tick", Integer.class));
        player.getInventory().setHeldItemSlot(0);

        /* Init gamemode */
        if (PlayerDataBus.isPlayerFirstJoin(player)) {
            PlayerBus.resetPlayerGame(player);
            PlayerDataBus.registerPlayerFirstJoin(player);
            if (GameTask.isInGame()) {
                player.setGameMode(GameMode.SPECTATOR);
            } else {
                player.setGameMode(GameMode.ADVENTURE);
                player.setRespawnLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
        }
    }

    public static void onBusComplete(PlayerJoinEvent event) {
        
    }

    public static boolean isPlayerOperator(PlayerJoinEvent event) {
        if (!event.getPlayer().isOp())
            return false;
        
        return true;
    }

    public static void onPlayerOperator(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Helloworld");
    }
}
