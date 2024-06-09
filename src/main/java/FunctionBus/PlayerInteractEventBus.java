package FunctionBus;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import Assert.Config.Role;
import Assert.Item.BattleFlag;
import Assert.Item.Buddha;
import Assert.Item.ReviveKey;
import Assert.Item.SmokingDarts;
import Assert.Item.Sword;
import Assert.Item.Taijutsu;
import Assert.Item.Gun.Matchlock;
import Assert.Item.Gun.Rifle;
import DataBus.PlayerDataBus;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;
import Task.MonitorTask;
import Task.AttackTask.BattleFlagTask;
import Task.AttackTask.SmokingDartsTask;
import Task.GunTask.RecoilTask;
import Task.GunTask.RifleTask;

public class PlayerInteractEventBus {
    public static boolean isTriggeredByDropItemEvent(PlayerInteractEvent event) {
        if (!PlayerDataBus.upPlayerDropItem(event.getPlayer()))
            return false;

        return true;
    }

    public static void onTriggeredByDropItemEvent(PlayerInteractEvent event) {
        
    }

    public static boolean isTargetBlockInteractAble(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (action == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (!player.isSneaking() && !event.getClickedBlock().getType().isInteractable())
                return false;
        }

        return true;
    }

    public static void onTargetBlockInteractAble(PlayerInteractEvent event) {
        return;
    }

    public static boolean isPlayerBeginUsingRifle(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;
        if (!Rifle._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;
        return true;
    }

    public static void onPlayerBeginUsingRifle(PlayerInteractEvent event) {
        RifleTask.shoot(event.getPlayer());
    }

    public static boolean isPlayerSlash(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK)
            return false;
        if (!Sword._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;
        return true;
    }

    public static void onPlayerSlash(PlayerInteractEvent event) {
        PlayerSlashBus.onPlayerSlash(event.getPlayer());
    }

    @Deprecated
    public static boolean isPlayerBeginChargedBlow(PlayerInteractEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        Action action = event.getAction();

        if (event.getHand() != EquipmentSlot.HAND)
            return false;

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (!Taijutsu._instanceof(inventory.getItemInMainHand()))
            return false;

        if (!Sword._instanceof(inventory.getItemInOffHand()))
            return false;

        if (event.getPlayer().getCooldown(inventory.getItemInMainHand().getType()) > 0)
            return false;
        
        return true;
    }

    @Deprecated
    public static void onPlayerBeginChargedBlow(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    public static boolean isPlayerUsingBattleFlag(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (event.getHand() != EquipmentSlot.HAND)
            return false;

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (!BattleFlag._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;

        return true;
    }


    public static void onPlayerUsingBattleFlag(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Vector direction = player.getEyeLocation().getDirection().setY(0).normalize();
        direction.multiply(ServerBus.getDistanceVelocity(1));
        direction.setY(1);

        player.setVelocity(direction);

        MonitorTask.execute("onPlayerUsingBattleFlag")
        .setMaxTick(120)
        .setConditionFunction((args, tick) -> {
            if (tick == 4)
                player.setVelocity(new Vector(0, -1, 0));

            if (Math.abs(event.getPlayer().getVelocity().getY() + 0.0784) > 0.00001)
                return false;
            else if (tick < 2)
                return false;
            return true;
        }, player)
        .setTargetFunction((args, tick) -> {
            Player p = (Player) args[0];
            BattleFlagTask task = new BattleFlagTask(p.getLocation(), 400);
            Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
        }, player)
        .execute();
    }

    
    public static boolean isPlayerUsingSmokingDartsEntity(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (event.getHand() != EquipmentSlot.HAND)
            return false;

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (!SmokingDarts._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;

        return true;
    }


    public static void onPlayerUsingSmokingDartsEntity(PlayerInteractEvent event) {
        SmokingDartsTask task = new SmokingDartsTask(event.getPlayer().getEyeLocation(), 400);
        Bukkit.getScheduler().runTask(ServerBus.getPlugin(), task);
    }

    public static boolean isPlayerUsingMatchlock(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (event.getHand() != EquipmentSlot.HAND)
            return false;

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (!Matchlock._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;

        return true;
    }

    
    public static void onPlayerUsingMatchlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location location = player.getEyeLocation();
        RayTraceResult result = ServerBus.rayTraceEntities(location, location.getDirection(), 35);
        RecoilTask.execute(event.getPlayer(), 0, -16f, 4);
        DelayTask.execute((args) -> {
            RecoilTask.execute((Player) args[0], 0, 12f, 10);
        }, 4, event.getPlayer());

        if (result == null)
            return;

        Vector velocity = location.toVector().clone().subtract(result.getHitPosition()).normalize().multiply(0.5);
        result.getHitEntity().setVelocity(velocity);
    }

    public static boolean isPlayerUsingReviveKey(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (event.getHand() != EquipmentSlot.HAND)
            return false;

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (!ReviveKey._instanceof(event.getPlayer().getInventory().getItemInMainHand()))
            return false;

        return true;
    }

    public static void onPlayerUsingReviveKey(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Team team = ScoreBoardBus.getPlayerTeam(player);
        if (team == null)
            return;

        for (Entity entity : player.getNearbyEntities(5, 3, 5)) {
            if (entity.getType() == EntityType.ITEM_DISPLAY) {
                ItemDisplay display = (ItemDisplay) entity;

                if (!Buddha._instanceof(((ItemDisplay) entity).getItemStack()))
                    continue;

                Color color = display.getGlowColorOverride();
                if (color == null)
                    return;
                    
                if ((color.equals(Color.fromRGB(0xB22222)) && team.getName().equals("red_team")) ||
                (color.equals(Color.AQUA) && team.getName().equals("blue_team")))
                {
                    PlayerBus.setPlayerInventoryList(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(1)), 0, 3, 6);
                    Role.refreshPlayerArmor(player, PlayerStateMachineSchedule.getPlayerRole(player));
                    player.getInventory().setHeldItemSlot(0);
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    ServerBus.playerShowServerEntity(player, PlayerDataBus.getPlayerRingDisplay(player));
                    ServerBus.playerShowServerEntity(player, PlayerDataBus.getPlayerHealthDisplay(player));
                    ServerBus.playerShowServerEntity(player, PlayerDataBus.getPlayerPostureDisplay(player));
                }
            }
        }
    }
}
