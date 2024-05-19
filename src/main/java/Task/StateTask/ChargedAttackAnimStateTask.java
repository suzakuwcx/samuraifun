package Task.StateTask;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import Assert.Config.State;
import Assert.Entity.HidariDoSwipeAnimation;
import Assert.Entity.KesagiriSwipeAnimation;
import Assert.Item.Sword;
import FunctionBus.EntityBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.ModelTask.ItemDisplayAnimationTask;

public class ChargedAttackAnimStateTask extends BaseStateTask {
    private Player player;
    private int level;
    private int tick;
    private State state;

    public ChargedAttackAnimStateTask(Player player, int level) {
        this.player = player;
        this.level = level;

        this.state = PlayerStateMachineSchedule.getPlayerState(player);
    }

    private void finish() {
        PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
        PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
    }

    private void level_0() {
        finish();
    }

    private void level_1() {
        if (tick == 0) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 1);
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1017), 0, 3, 6);
            state.current_sword_frame = 1017;
        } else if (tick == 1) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1018), 0, 3, 6);
            state.current_sword_frame = 1018;
        } else if (tick == 2) {
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
        } else if (tick == 6) {            
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
            state.current_sword_frame = 1009;
        } else if (tick == 7) {
            finish();
        }
    }

    private void level_2() {
        if (tick == 0) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 1);
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1017), 0, 3, 6);
            state.current_sword_frame = 1017;
        } else if (tick == 1) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1018), 0, 3, 6);
            state.current_sword_frame = 1018;
        } else if (tick == 2) {
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
        } else if (tick == 5) {
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
            state.current_sword_frame = 1009;
        } else if (tick == 6) {
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation(), 2), 4);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation(), 2), 3);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation(), 2), 2);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation(), 2), 1);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1019), 0, 3, 6);
            state.current_sword_frame = 1019;
        } else if (tick == 7) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1005), 0, 3, 6);
            state.current_sword_frame = 1005;
        } else if (tick == 8) {
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
            PlayerBus.setPlayerInventoryList(player, new Sword(1006), 0, 3, 6);
            state.current_sword_frame = 1006;
        } else if (tick == 12) {
            finish();
        }
    }

    private void onDamageTarget(EntityDamageByEntityEvent event) {
        Player target =  (Player) event.getEntity();
        BaseStateTask task = PlayerStateMachineSchedule.getStateTask(target);

        event.setCancelled(false);
        event.setDamage(0);
        PlayerStateMachineSchedule.damageHealth(target, 1);
        ServerBus.playServerSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 0.8f);
        ServerBus.playServerSound(target.getLocation(), Sound.ITEM_TRIDENT_HIT, 1f, 0.5f);

        if (task instanceof ChargedAttackAnimStateTask)
            return;
        
        if (task instanceof ChargingAttackStateTask)
            return;

        if (EntityBus.getTargetDistance(event.getDamager(), target) < 1)
            target.setVelocity(EntityBus.getTargetDirection(event.getDamager(), target).multiply(0.6));
        else
            target.setVelocity(EntityBus.getTargetDirection(event.getDamager(), target).multiply(0.3));
            
        if (task instanceof PlayerPostureCrashTask)
            return;
            
        PlayerBus.setPlayerInventoryList(target, new Sword(1023), 0, 3, 6);
        PlayerStateMachineSchedule.setStateTask(target, new PlayerStunTask(target));
    }

    private void onPlayerDefense(EntityDamageByEntityEvent event) {
        Player p = (Player) event.getEntity();

        PlayerStateMachineSchedule.damagePosture(p, 1);
        ServerBus.playServerSound(event.getEntity().getLocation(), Sound.ITEM_SHIELD_BLOCK, 0.5f, 0.8f);
        ServerBus.playServerSound(event.getEntity().getLocation(), Sound.BLOCK_BELL_USE, 1f, 2f);
        ServerBus.playServerSound(event.getEntity().getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.2f, 2f);

        PlayerBus.disableShield(p, 8);
        return;
    }

    private void onPlayerDeflect(EntityDamageByEntityEvent event) {
        ServerBus.playServerSound(event.getEntity().getLocation(), Sound.BLOCK_BELL_USE, 1f, 2f);
        ServerBus.playServerSound(event.getEntity().getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5f, 0.5f);
        ServerBus.playServerSound(event.getEntity().getLocation(), Sound.BLOCK_BELL_USE, 0.5f, 0.1f);
        ServerBus.playServerSound(event.getEntity().getLocation(), Sound.ITEM_TRIDENT_RETURN, 1f, 0.5f);
    }

    @Override
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Player p = (Player) event.getEntity();

        if (!StateEventBus.isPlayerSlash(player)) {
            
        } else if (StateEventBus.isPlayerDefense(event) || StateEventBus.isPlayerFakeDeflect(event)) { /* Defense handler */
            onPlayerDefense(event);
        } else if (StateEventBus.isPlayerDeflect(event)) { /* Deflect handler */
            onPlayerDeflect(event);
        } else {
            onDamageTarget(event);
        }
    }

    private void level_3() {
        if (tick == 0) {
            Player target;
            Vector direction = player.getEyeLocation().getDirection().setY(0).normalize();
            RayTraceResult result = ServerBus.rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 8, 0.3, EntityType.PLAYER, player.getUniqueId());
            if (result != null) {
                target = (Player) result.getHitEntity();
                double distance = EntityBus.getTargetDistance(player, target) - 2;
                if (distance < 0)
                    distance = 0;
                player.setVelocity(direction.multiply(new Vector(ServerBus.getDistanceVelocity(distance), 0, ServerBus.getDistanceVelocity(distance))));
            } else {
                player.setVelocity(direction.multiply(new Vector(ServerBus.getDistanceVelocity(3), 0, ServerBus.getDistanceVelocity(3))));
            }
        } else if (tick == 3) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 1);
        } else if (tick == 5) {
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1017), 0, 3, 6);
            state.current_sword_frame = 1017;
        } else if (tick == 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1018), 0, 3, 6);
            state.current_sword_frame = 1018;
        } else if (tick == 10) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
            state.current_sword_frame = 1009;
        } else if (tick == 11) {
            finish();
        }
    }

    @Override
    public void run() {
        switch (level) {
            case 0:
                level_0();
                break;
            case 1:
                level_1();
                break;
            case 2:
                level_2();
                break;
            case 3:
                level_3();
                break;
            default:
                break;
        }

        ++tick;
    }
}
