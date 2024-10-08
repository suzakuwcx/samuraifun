package Task.StateTask;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import Assert.Config.State;
import Assert.Entity.HidariDoSwipeAnimation;
import Assert.Entity.KesagiriSwipeAnimation;
import Assert.Font.FontDatabase;
import Assert.Item.Sword;
import DataBus.PlayerDataBus;
import FunctionBus.EntityBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;
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
        PlayerUISchedule.setPlayerMainRing(player, FontDatabase.STATUS_RING_CHARGED_WARNING);
    }

    private void finish() {
        StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(3)));
        PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
    }

    private void knockback(Player damager, Player target) {
        knockback(damager, target, 1);
    }

    private void knockback(Player damager, Player target, double percentage) {
        double distance = EntityBus.getTargetDistance(damager, target);

        if (distance < 1.5)
            target.setVelocity(EntityBus.getTargetDirection(damager, target).setY(0).normalize().multiply(2.3).multiply(percentage));
        else
            target.setVelocity(EntityBus.getTargetDirection(damager, target).setY(0).normalize().multiply(1.4).multiply(percentage));
    }

    private void level_0() {
        finish();
    }

    private void level_1() {
        if (tick == 0) {
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(17)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(17);
        } else if (tick == 6) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 1);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(18)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(18);
        } else if (tick == 7) {
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
        } else if (tick == 8) {            
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(9)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(9);
        } else if (tick == 12) {
            finish();
        }
    }

    private void level_2() {
        if (tick == 0) {
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(17)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(17);
        } else if (tick == 4) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 1);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(18)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(18);
        } else if (tick == 5) {
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
        } else if (tick == 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(9)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(9);
        } else if (tick == 9) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(19)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(19);
        } else if (tick == 10) {
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(5)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(5);
        } else if (tick == 11) {
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation(), 2), 4);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation(), 2), 3);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation(), 2), 2);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation(), 2), 1);
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(6)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(6);
        } else if (tick == 15) {
            finish();
        }
    }

    private void onDamageTarget(EntityDamageByEntityEvent event) {
        Player damager = (Player) event.getDamager();
        Player target = (Player) event.getEntity();
        BaseStateTask task = PlayerStateMachineSchedule.getStateTask(target);

        target.sendHurtAnimation(0);
        PlayerStateMachineSchedule.damageHealth(target, 1);
        if (PlayerStateMachineSchedule.isPlayerNoHealth(target)) {
            PlayerDataBus.downPlayerDeadByPlugin(target, player);
            target.setHealth(0);
        }

        ServerBus.playServerSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 0.8f);
        ServerBus.playServerSound(target.getLocation(), Sound.ITEM_TRIDENT_HIT, 1f, 0.5f);

        if (task instanceof ChargedAttackAnimStateTask)
            return;
        
        if (task instanceof ChargingAttackStateTask)
            return;

        if (PlayerStateMachineSchedule.isPlayerNoPosture(target)) {
            knockback(damager, target, 1.6);
        } else if (level == 2) {
            knockback(damager, target, 0.3);
        } else {
            knockback(damager, target);
        }
        
        if (task instanceof PlayerPostureCrashTask)
            return;

        if (PlayerStateMachineSchedule.isPlayerNoPosture(target)) {
            PlayerStateMachineSchedule.setStateTask(target, new PlayerPostureCrashTask(target));
        } else {
            StateEventBus.replacePlayerSwordSlot(target, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(23)));
            PlayerStateMachineSchedule.setStateTask(target, new PlayerStunTask(target));
        }
    }

    private void onPlayerDefense(EntityDamageByEntityEvent event) {
        Player damager = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        PlayerStateMachineSchedule.damagePosture(target, 1);

        PlayerUISchedule.setPlayerSideSubtitle(target, FontDatabase.STATUS_SUBTITLE_POSTURE_CRASH, 4);

        PlayerBus.disableShield(target, 15);
        knockback(damager, target, 0.5);

        ServerBus.playServerSound(target.getLocation(), Sound.ITEM_SHIELD_BLOCK, 0.5f, 0.8f);
        ServerBus.playServerSound(target.getLocation(), Sound.BLOCK_BELL_USE, 1f, 2f);
        ServerBus.playServerSound(target.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.2f, 2f);

        Vector direction = EntityBus.getTargetDirection(target, damager);
        direction.multiply(0.5);
        ServerBus.spawnServerParticle(Particle.LAVA, target.getLocation().add(direction).add(0, 1.1, 0), 2, 0, 0., 0, 1000);
    }

    private void onPlayerDeflect(EntityDamageByEntityEvent event) {
        Player damager = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        knockback(damager, target, 0.5);

        ServerBus.playServerSound(target.getLocation(), Sound.BLOCK_BELL_USE, 1f, 2f);
        ServerBus.playServerSound(target.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5f, 0.5f);
        ServerBus.playServerSound(target.getLocation(), Sound.BLOCK_BELL_USE, 0.5f, 0.1f);
        ServerBus.playServerSound(target.getLocation(), Sound.ITEM_TRIDENT_RETURN, 1f, 0.5f);

        Vector direction = EntityBus.getTargetDirection(target, damager);
        direction.multiply(0.5);
        ServerBus.spawnServerParticle(Particle.LAVA, target.getLocation().add(direction).add(0, 1.1, 0), 10, 0, 0., 0, 1000);
    }

    @Override
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Player target = (Player) event.getEntity();

        if (!StateEventBus.isPlayerSlash(player))
            return;
        
        PlayerStateMachineSchedule.resetSwordCooldown(target);

        if (StateEventBus.isPlayerDefense(event) || StateEventBus.isPlayerFakeDeflect(event)) { /* Defense handler */
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
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 5), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 5), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 5), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 5), 1);
        } else if (tick == 5) {
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(17)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(17);
        } else if (tick == 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(18)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(18);
        } else if (tick == 7) {
            StateEventBus.replacePlayerSwordSlot(player, new Sword(PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(9)));
            state.current_sword_frame = PlayerStateMachineSchedule.getPlayerRole(player).getSwordModelData(9);
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
