package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Config.State;
import Assert.Entity.HidariDoSwipeAnimation;
import Assert.Entity.KesagiriSwipeAnimation;
import Assert.Entity.RightKiriageSwipeAnimation;
import Assert.Item.Sword;
import FunctionBus.EntityBus;
import FunctionBus.PlayerBus;
import FunctionBus.ServerBus;
import Schedule.PlayerStateMachineSchedule;
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

    private void doStage1() {
        if (tick == 1) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1f, 0.8f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1004), 0, 3, 6);
            state.current_sword_frame = 1004;
        } else if (tick <= 20) {
            player.sendMessage("阶段 1: 准备");
        } else if (tick == 21) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 1.2f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick <= 23) {
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 1);
            PlayerBus.setPlayerInventoryList(player, new Sword(1005), 0, 3, 6);
            state.current_sword_frame = 1005;
            player.sendMessage("阶段 1: 砍");
        } else if (tick == 24) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1006), 0, 3, 6);
            state.current_sword_frame = 1006;
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
            player.sendMessage("阶段 1: 伤害判定");
        } else if (tick < 29) {
            player.sendMessage("阶段 1: 结束动作");
        } else if (tick == 29) {
            player.sendMessage("阶段 1: 结束");
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
        } else if (tick <= 5) {
            player.sendMessage("阶段 2: 准备");
        } else if (tick == 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 1.2f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick < 9) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 1);
            PlayerBus.setPlayerInventoryList(player, new Sword(1008), 0, 3, 6);
            state.current_sword_frame = 1008;
            player.sendMessage("阶段 2: 砍");
        } else if (tick == 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
            state.current_sword_frame = 1009;
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
            player.sendMessage("阶段 2: 伤害判定");
        } else if (tick < 14) {
            player.sendMessage("阶段 2: 结束动作");
        } else if (tick == 14) {
            player.sendMessage("阶段 2: 结束");
            check_end();
            
            ++stage;
            tick = 0;
        }
    }

    @Override
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity e = event.getEntity();
        /* Check if this damage event is trigged by slash attack or normal attack, if it is not slash attack, trigger it by left click */
        if (!StateEventBus.isPlayerSlash(player)) {
            is_continue = true;
        } else {
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 0.8f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_HIT, 1f, 0.5f);

            if (EntityBus.getTargetDistance(event.getDamager(), e) < 1)
                e.setVelocity(EntityBus.getTargetDirection(event.getDamager(), e).multiply(0.6));
            else
                e.setVelocity(EntityBus.getTargetDirection(event.getDamager(), e).multiply(0.3));
        }
    }

    private void doStage3() {
        if (tick == 1) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1010), 0, 3, 6);
            state.current_sword_frame = 1010;
        } else if (tick <= 5) {
            player.sendMessage("阶段 3: 准备");
        } else if (tick == 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 1.2f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick < 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1011), 0, 3, 6);
            state.current_sword_frame = 1011;
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 1);
            player.sendMessage("阶段 3: 砍");
        } else if (tick == 9) {
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
            player.sendMessage("阶段 3: 伤害判定");
        } else if (tick < 14) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1012), 0, 3, 6);
            state.current_sword_frame = 1012;
            player.sendMessage("阶段 3: 结束动作");
        } else if (tick == 14) {
            player.sendMessage("阶段 3: 结束");
            check_end();
            
            ++stage;
            tick = 0;
        }
    }

    private void doStage4() {
        if (tick == 1) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1007), 0, 3, 6);
            state.current_sword_frame = 1007;
        } else if (tick <= 5) {
            player.sendMessage("阶段 4: 准备");
        } else if (tick == 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 1.2f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick < 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1008), 0, 3, 6);
            state.current_sword_frame = 1008;
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 1);
            player.sendMessage("阶段 4: 砍");
        } else if (tick == 9) {
            StateEventBus.playerSectorSlash(player, 4 * Math.PI / 3);
            player.sendMessage("阶段 4: 伤害判定");
        } else if (tick < 29) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
            state.current_sword_frame = 1009;
            player.sendMessage("阶段 4: 结束动作");
        } else if (tick == 29) {
            check_end();
            
            ++stage;
            tick = 0;
        }
    }


    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (StateEventBus.isPlayerAttack(event)) {
            is_continue = true;
        } else if (StateEventBus.isPlayerDefense(event)) {
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
