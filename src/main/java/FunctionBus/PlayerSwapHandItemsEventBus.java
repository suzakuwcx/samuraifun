package FunctionBus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import Assert.Config.PlayerConfig;
import Assert.Item.Sword;
import Task.SwipeTask.YokogiriTask;

public class PlayerSwapHandItemsEventBus {
    public static void onBusTrigger(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    public static boolean isPlayerSwitchBodyMode(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!Sword._instanceof(player.getInventory().getItemInMainHand()))
            return false;

        return true;
    }

    public static void onPlayerSwitchBodyMode(PlayerSwapHandItemsEvent event) {
        event.setCancelled(false);
    }

    public static boolean isPlayerSwitchSwordMode(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!Sword._instanceof(player.getInventory().getItemInOffHand()))
            return false;

        return true;
    }

    public static void onPlayerSwitchSwordMode(PlayerSwapHandItemsEvent event) {
        event.setCancelled(false);
        event.setOffHandItem(new ItemStack(Material.AIR));
        YokogiriTask.execute(event.getPlayer(), PlayerConfig.BASIC_ATTACK_RANGE);
    }

    public static void onBusComplete(PlayerSwapHandItemsEvent event) {
        
    }
}
