package Task.StateTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import Assert.Config.PlayerConfig;
import Assert.Config.State;
import Assert.Item.Sword;
import DataBus.PlayerDataBus;
import FunctionBus.PlayerBus;
import FunctionBus.ScoreBoardBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.DelayTask;
import Task.AttackTask.DeflectTask;

public class StateEventBus {
    public static boolean isPlayerAttack(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return false;

        return true;
    }

    public static boolean isPlayerAttack(EntityDamageByEntityEvent event) {
        return true;
    }

    public static boolean isPlayerDefense(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return false;

        if (event.getPlayer().getCooldown(Material.SHIELD) != 0)
            return false;

        return true;
    }

    public static boolean isPlayerChargingAttack(PlayerSwapHandItemsEvent event) {
        return true;
    }

    public static void playerSectorSlash(Player player, double scope) {
        for (Player e: ServerBus.getNearbyEntities(player.getEyeLocation(), PlayerConfig.BASIC_ATTACK_RANGE, 2, PlayerConfig.BASIC_ATTACK_RANGE, EntityType.PLAYER, Player.class)) {
            if (e.equals(player))
                continue;

            if (PlayerStateMachineSchedule.getPlayerState(e).is_invincible_frame)
                continue;

            if (ScoreBoardBus.isPlayerSameTeam(player, e))
                continue;

            if (!PlayerBus.isEntityInFrontOfPlayer(player, e, PlayerConfig.BASIC_ATTACK_RANGE, scope))
                continue;

            PlayerDataBus.downPlayerSlash(player);
            player.attack(e);
        }
    }

    public static boolean isPlayerSlash(Player player) {
        if (!PlayerDataBus.upPlayerSlash(player))
            return false;

        return true;
    }

    public static boolean isPlayerDefense(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER)
            return false;

        if (event.getDamage(DamageModifier.BLOCKING) >= - 0.01)
            return false;

        return true;
    }

    public static boolean isPlayerFakeDeflect(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER)
            return false;

        Player player = (Player) event.getEntity();
        if (event.getDamage(DamageModifier.BLOCKING) < - 0.01)
            return false;

        if (!DeflectTask.isPlayerFakeDeflect(player))
            return false;

        return true;
    }

    public static boolean isPlayerDeflect(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER)
            return false;

        Player player = (Player) event.getEntity();
        if (event.getDamage(DamageModifier.BLOCKING) < - 0.01)
            return false;

        if (!DeflectTask.isPlayerDeflect(player))
            return false;

        return true;
    }

    public static boolean isPlayerDash(PlayerToggleSneakEvent event) {
        if (event.isSneaking())
            return false;

        if (Math.abs(event.getPlayer().getVelocity().getY() + 0.0784) > 0.00001)
            return false;

        return true;
    }

    public static void onPlayerDash(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();

        State s = PlayerStateMachineSchedule.getPlayerState(p);
        if (s.dash_cooldown != 0)
            return;

        DelayTask.execute((args) -> {
            Player player = (Player) args[0];
            Location location = player.getLocation();
            Vector direction = location.getDirection().setY(0).normalize();
            State state = PlayerStateMachineSchedule.getPlayerState(p);
            Vector vec = (location.toVector().subtract((Vector) args[1])).setY(0);

            /* Sound effect */
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1f, 0.8f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1f, 1f);

            /* If player not move, default dash backward */
            if (vec.lengthSquared() == 0)
                vec = direction.clone().multiply(-1);
            else
                vec.normalize();

            float angle = vec.angle(direction);
            /* 
             * decrease straight dash value to 2.5, increase level dash to 4, backend dash is 3.5
             * due to that PI / 2 sometime judgement faile because of the accuracy
             * use the 6/11 PI instead
             */
            double distance;
            if (angle < 6 * Math.PI / 11)
                distance = 2.5 + 1.5 * Math.sin(angle);
            else
                distance = 3.5;

            if (state.posture == 0) {
                distance /= 2;
            } else {
                state.posture = state.posture - 1;
                /*
                 * Add a 2 tick invincible frame
                 */
                state.is_invincible_frame = true;
                DelayTask.execute((args1) -> {
                    State s1 = (State) args1[0];
                    s1.is_invincible_frame = false;
                }, 2, state);
            }

            player.setVelocity(vec.clone().multiply(ServerBus.getDistanceVelocity(distance)));

            state.dash_cooldown = 5;
        }, 2, p, p.getLocation().toVector());
    }

    public static void onPlayerDefense(PlayerInteractEvent event) {
        PlayerBus.setPlayerInventoryList(event.getPlayer(), new Sword(1003), 0, 3, 6);
        PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new DefenseStateTask(event.getPlayer()));
    }
}
