package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Assert.Entity.HidariDoSwipeAnimation;
import Assert.Entity.KesagiriSwipeAnimation;
import Assert.Entity.RightKiriageSwipeAnimation;
import Assert.Item.Sword;
import FunctionBus.PlayerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.ModelTask.ItemDisplayAnimationTask;

public class NormalAttackStateTask extends BaseStateTask {
    private Player player;
    
    private int tick = 0;
    private int stage = 1;
    private boolean is_continue = false;

    public NormalAttackStateTask(Player player) {
        this.player = player;
    }

    private void check_end() {
        if (is_continue == false) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1003), 0, 3, 6);
            PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
        } else {
            is_continue = false;
        }
    }

    private void doStage1() {
        if (tick <= 20) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1004), 0, 3, 6);
            player.sendMessage("阶段 1: 准备");
        } else if (tick == 21) {
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick <= 23) {
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 1);
            PlayerBus.setPlayerInventoryList(player, new Sword(1005), 0, 3, 6);
            player.sendMessage("阶段 1: 砍");
        } else if (tick == 24) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1006), 0, 3, 6);
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
        if (tick <= 5) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1007), 0, 3, 6);
            player.sendMessage("阶段 2: 准备");
        } else if (tick == 6) {
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick < 9) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 1);
            PlayerBus.setPlayerInventoryList(player, new Sword(1008), 0, 3, 6);
            player.sendMessage("阶段 2: 砍");
        } else if (tick == 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
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

    private void doStage3() {
        if (tick <= 5) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1010), 0, 3, 6);
            player.sendMessage("阶段 3: 准备");
        } else if (tick == 6) {
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick < 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1011), 0, 3, 6);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 1);
            player.sendMessage("阶段 3: 砍");
        } else if (tick == 9) {
            player.sendMessage("阶段 3: 伤害判定");
        } else if (tick < 14) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1012), 0, 3, 6);
            player.sendMessage("阶段 3: 结束动作");
        } else if (tick == 14) {
            player.sendMessage("阶段 3: 结束");
            check_end();
            
            ++stage;
            tick = 0;
        }
    }

    private void doStage4() {
        if (tick <= 5) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1007), 0, 3, 6);
            player.sendMessage("阶段 4: 准备");
        } else if (tick == 6) {
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick < 9) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1008), 0, 3, 6);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 1);
            player.sendMessage("阶段 4: 砍");
        } else if (tick == 9) {
            player.sendMessage("阶段 4: 伤害判定");
        } else if (tick < 29) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
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
                PlayerStateMachineSchedule.setStateTask(player, new BattleStateTask(player));
                break;
        }
    }
}
