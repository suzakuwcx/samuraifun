package FunctionBus;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Item.Sword;
import ConfigBus.ConfigBus;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;

public class PlayerJoinEventBus {
    public static void onBusTrigger(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerDataBus.addPlayerItemDisplay(player);
        PlayerStateMachineSchedule.init(event.getPlayer());
        PlayerUISchedule.init(player);

        player.setShieldBlockingDelay(ConfigBus.getValue("deflect_tick", Integer.class));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 40, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, PotionEffect.INFINITE_DURATION, 3, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION, 3, false, false));
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
        player.setWalkSpeed(0.2f);
        player.getInventory().setHeldItemSlot(0);

        PlayerBus.setPlayerInventoryList(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(1)), 0, 3, 6);
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
