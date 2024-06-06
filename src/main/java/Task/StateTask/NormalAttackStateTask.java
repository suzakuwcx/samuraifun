package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import Assert.Config.State;
import Assert.Entity.HidariDoSwipeAnimation;
import Assert.Entity.KesagiriSwipeAnimation;
import Assert.Entity.RightKiriageSwipeAnimation;
import Assert.Font.FontDatabase;
import Assert.Item.Sword;
import DataBus.PlayerDataBus;
import FunctionBus.EntityBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
import Schedule.PlayerUISchedule;
import Task.ModelTask.ItemDisplayAnimationTask;

public class NormalAttackStateTask extends BaseStateTask {
    private Player player;
    private State state;
    
    private int tick = 0;
    private int stage = 1;
    private boolean is_continue = false;

    public NormalAttackStateTask(Player player) {
        this.player = player;
        this.state = PlayerStateMachineSchedule.getPlayerState(player);

        PlayerStateMachineSchedule.resetSwordCooldown(player);
        
        PlayerUISchedule.setPlayerMainRing(player, FontDatabase.STATUS_RING_ATTACK);
    }

    private void check_end() {
        if (is_continue == false) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
            state.current_sword_frame = 1003;
            PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
        } else {
            is_continue = false;
        }
    }

    private void knockback(Player damager, Player target) {
        knockback(damager, target, 1);
    }

    private void knockback(Player damager, Player target, double percentage) {
        double distance = EntityBus.getTargetDistance(damager, target);

        if (distance < 1.5)
            target.setVelocity(EntityBus.getTargetDirection(damager, target).setY(0).normalize().multiply(1.7));
        else
            target.setVelocity(EntityBus.getTargetDirection(damager, target).setY(0).normalize().multiply(0.8));
    }

    private void doStage1() {
        if (tick == 1) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1f, 0.8f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1004), 0, 3, 6);
            state.current_sword_frame = 1004;
        } else if (tick == 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 1.2f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            player.setCooldown(Material.SHIELD, 6);
            PlayerStateMachineSchedule.resetSwordCooldown(player);
        } else if (tick == 8) {
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 1);
            PlayerBus.setPlayerInventoryList(player, new Sword(1005), 0, 3, 6);
            state.current_sword_frame = 1005;
        } else if (tick == 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1006), 0, 3, 6);
            state.current_sword_frame = 1006;
            StateEventBus.playerSectorSlash(player, Math.PI / 2);
        } else if (tick == 14) {
            check_end();
            
            ++stage;
            tick = 0;
        }
    }

    private void doStage2() {
        if (tick == 1) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1f, 0.8f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1007), 0, 3, 6);
            state.current_sword_frame = 1007;
        } else if (tick == 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 1.2f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            player.setCooldown(Material.SHIELD, 6);
            PlayerStateMachineSchedule.resetSwordCooldown(player);
        } else if (tick == 8) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 1);
            PlayerBus.setPlayerInventoryList(player, new Sword(1008), 0, 3, 6);
            state.current_sword_frame = 1008;
        } else if (tick == 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
            state.current_sword_frame = 1009;
            StateEventBus.playerSectorSlash(player, Math.PI / 2);
        } else if (tick == 14) {
            check_end();
            
            ++stage;
            tick = 0;
        }
    }

    private void doStage3() {
        if (tick == 1) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1010), 0, 3, 6);
            state.current_sword_frame = 1010;
        } else if (tick == 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 1.2f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            player.setCooldown(Material.SHIELD, 6);
            PlayerStateMachineSchedule.resetSwordCooldown(player);
        } else if (tick == 8) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1011), 0, 3, 6);
            state.current_sword_frame = 1011;
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 1);
        } else if (tick == 9) {
            StateEventBus.playerSectorSlash(player, Math.PI / 2);
        } else if (tick == 10) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1012), 0, 3, 6);
            state.current_sword_frame = 1012;
        } else if (tick == 14) {
            check_end();
            
            ++stage;
            tick = 0;
        }
    }

    private void doStage4() {
        if (tick == 1) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1007), 0, 3, 6);
            state.current_sword_frame = 1007;
        } else if (tick == 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 1.2f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            player.setCooldown(Material.SHIELD, 6);
            PlayerStateMachineSchedule.resetSwordCooldown(player);
        } else if (tick == 8) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1008), 0, 3, 6);
            state.current_sword_frame = 1008;
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 1);
        } else if (tick == 9) {
            StateEventBus.playerSectorSlash(player, Math.PI / 2);
        } else if (tick == 10) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
            state.current_sword_frame = 1009;
        } else if (tick == 18) {
            check_end();
            
            ++stage;
            tick = 0;
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
            knockback(damager, target, 2);
        } else {
            knockback(damager, target);
        }
        
        if (task instanceof PlayerPostureCrashTask)
            return;

        if (PlayerStateMachineSchedule.isPlayerNoPosture(target)) {
            PlayerStateMachineSchedule.setStateTask(target, new PlayerPostureCrashTask(target));
        } else {
            PlayerBus.setPlayerInventoryList(target, new Sword(1023), 0, 3, 6);
            PlayerStateMachineSchedule.setStateTask(target, new PlayerStunTask(target));
        }
    }

    private void onPlayerDefense(EntityDamageByEntityEvent event) {
        Player target =  (Player) event.getEntity();

        PlayerStateMachineSchedule.damagePosture(target, 1);
        knockback((Player) event.getDamager(), target, 0.5);

        PlayerUISchedule.setPlayerSideRing(target, FontDatabase.STATUS_RING_SUCCESS_DEFENSE, 4);
        PlayerUISchedule.setPlayerSideSubtitle(target, FontDatabase.STATUS_SUBTITLE_SUCCESS_DEFENSE, 4);
        ServerBus.playServerSound(target.getLocation(), Sound.ITEM_SHIELD_BLOCK, 0.5f, 0.8f);
        ServerBus.playServerSound(target.getLocation(), Sound.BLOCK_BELL_USE, 1f, 2f);
        ServerBus.playServerSound(target.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.2f, 2f);
        
        Vector direction = EntityBus.getTargetDirection(target, event.getDamager());
        direction.multiply(0.5);
        ServerBus.spawnServerParticle(Particle.LAVA, target.getLocation().add(direction).add(0, 1.1, 0), 2, 0, 0., 0, 1000);
    }

    private void onPlayerDeflect(EntityDamageByEntityEvent event) {
        Player damager = (Player) event.getDamager();
        Player target = (Player) event.getEntity();
        Vector direction = EntityBus.getTargetDirection(target, event.getDamager());
        double distance = 0;

        PlayerUISchedule.setPlayerSideRing(target, FontDatabase.STATUS_RING_SUCCESS_DEFLECT, 4);
        PlayerUISchedule.setPlayerSideSubtitle(target, FontDatabase.STATUS_SUBTITLE_SUCCESS_DEFLECT, 4);
        ServerBus.playServerSound(target.getLocation(), Sound.BLOCK_BELL_USE, 1f, 2f);
        ServerBus.playServerSound(target.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5f, 0.5f);
        ServerBus.playServerSound(target.getLocation(), Sound.BLOCK_BELL_USE, 0.5f, 0.1f);
        ServerBus.playServerSound(target.getLocation(), Sound.ITEM_TRIDENT_RETURN, 1f, 0.5f);

        ServerBus.spawnServerParticle(Particle.LAVA, target.getLocation().add(direction.clone().multiply(0.5)).add(0, 1.1, 0), 5, 0, 0., 0, 1000);

        direction.setY(0).normalize();
        distance = 3.0 - EntityBus.getTargetDistance(damager, target);
        if (distance < 0)
            distance = 0.6;

        PlayerStateMachineSchedule.damagePosture(damager, 1, false);
        damager.setVelocity(direction.multiply(distance));
        PlayerStateMachineSchedule.setStateTask(damager, new PlayerStunTask(damager));
    }

    @Override
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Player target = (Player) event.getEntity();
        /* Check if this damage event is trigged by slash attack or normal attack, if it is not slash attack, trigger it by left click */
        if (!StateEventBus.isPlayerSlash(player)) {
            is_continue = true;
            return;
        }
        
        if (stage > 1) {
            PlayerStateMachineSchedule.recoverPosture(player, 1);
        }

        PlayerStateMachineSchedule.resetSwordCooldown(target);

        if (StateEventBus.isPlayerDefense(event) || StateEventBus.isPlayerFakeDeflect(event)) { /* Defense handler */
            onPlayerDefense(event);
        } else if (StateEventBus.isPlayerDeflect(event)) { /* Deflect handler */
            onPlayerDeflect(event);
        } else {
            onDamageTarget(event);
        }
    }


    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (StateEventBus.isPlayerAttack(event)) {
            is_continue = true;
        } else if (StateEventBus.isPlayerDefense(event)) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
            PlayerStateMachineSchedule.setStateTask(event.getPlayer(), new DefenseStateTask(event.getPlayer()));
        }
    }


    @Override
    public void run() {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 4, 4));

        ++tick;

        switch (stage) {
            case 1:
                doStage1();
                break;
            case 2:
                doStage2();
                break;
            case 3:
                doStage3();
                break;
            case 4:
                doStage4();
                break;
            default:
                PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
                state.current_sword_frame = 1003;
                PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
                break;
        }
    }
}
