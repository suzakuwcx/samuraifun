package Task.StateTask;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import Assert.Entity.HidariDoSwipeAnimation;
import Assert.Entity.KesagiriSwipeAnimation;
import Assert.Entity.RightKiriageSwipeAnimation;
import FunctionBus.PlayerBus;
import Schedule.PlayerStateMachineSchedule;
import Task.ModelTask.ItemDisplayAnimationTask;

public class NormalAttackStateTask implements Runnable{
    private Player player;
    
    private int tick = 0;
    private int stage = 1;
    private boolean is_continue = false;

    private final Vector axis = new Vector(0, 1, 0).rotateAroundZ(- Math.PI / 10).normalize();

    public NormalAttackStateTask(Player player) {
        this.player = player;
    }

    private void doStage1() {
        if (tick <= 20) 
            player.sendMessage("阶段 1: 准备");
        else if (tick == 21) {
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick <= 23) {
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new KesagiriSwipeAnimation(player.getEyeLocation()), 1);
            // ItemDisplayAnimationTask.execute(new SwordSwipeAnimation(player.getEyeLocation()), 4);
            player.sendMessage("阶段 1: 砍");
        } else if (tick == 24) {
            player.sendMessage("阶段 1: 伤害判定");
        } else if (tick < 29) {
            if (PlayerStateMachineSchedule.player_leftclick_map.get(player.getUniqueId()))
                is_continue = true;
            player.sendMessage("阶段 1: 结束动作");
            
        } else if (tick == 29) {
            player.sendMessage("阶段 1: 结束");
            if (is_continue == false)
                PlayerStateMachineSchedule.player_state_map.put(player.getUniqueId(), new NormalStateTask(player));
            
            ++stage;
            tick = 0;

        }
    }

    private void doStage2() {
        if (tick <= 5) {
            player.sendMessage("阶段 2: 准备");
        } else if (tick == 6) {
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick < 9) {
            player.sendMessage("阶段 2: 砍");
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 1);
        } else if (tick == 9) {
            player.sendMessage("阶段 2: 伤害判定");
        } else if (tick < 14) {
            if (PlayerStateMachineSchedule.player_leftclick_map.get(player.getUniqueId()))
                is_continue = true;
            player.sendMessage("阶段 2: 结束动作");
        } else if (tick == 14) {
            player.sendMessage("阶段 2: 结束");
            if (is_continue == false)
                PlayerStateMachineSchedule.player_state_map.put(player.getUniqueId(), new NormalStateTask(player));
            
            ++stage;
            tick = 0;
        }
    }

    private void doStage3() {
        if (tick <= 5) {
            player.sendMessage("阶段 3: 准备");
        } else if (tick == 6) {
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick < 9) {
            player.sendMessage("阶段 3: 砍");
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new RightKiriageSwipeAnimation(player.getEyeLocation()), 1);
        } else if (tick == 9) {
            player.sendMessage("阶段 3: 伤害判定");
        } else if (tick < 14) {
            if (PlayerStateMachineSchedule.player_leftclick_map.get(player.getUniqueId()))
                is_continue = true;
            player.sendMessage("阶段 3: 结束动作");
        } else if (tick == 14) {
            player.sendMessage("阶段 3: 结束");
            if (is_continue == false)
                PlayerStateMachineSchedule.player_state_map.put(player.getUniqueId(), new NormalStateTask(player));
            
            ++stage;
            tick = 0;
        }
    }

    private void doStage4() {
        if (tick <= 5) {
            player.sendMessage("阶段 4: 准备");
        } else if (tick == 6) {
            player.setCooldown(Material.SHIELD, 6);
        } else if (tick < 9) {
            player.sendMessage("阶段 4: 砍");
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation()), 1);
        } else if (tick == 9) {
            player.sendMessage("阶段 4: 伤害判定");
        } else if (tick < 29) {
            if (PlayerStateMachineSchedule.player_leftclick_map.get(player.getUniqueId()))
                is_continue = true;
            player.sendMessage("阶段 4: 结束动作");
        } else if (tick == 29) {
            player.sendMessage("阶段 4: 结束");
            if (is_continue == false)
                PlayerStateMachineSchedule.player_state_map.put(player.getUniqueId(), new NormalStateTask(player));
            
            ++stage;
            tick = 0;
        }
    }

    private static boolean isPlayerDefense(Player player) {
        if (!PlayerStateMachineSchedule.player_defense_map.get(player.getUniqueId()))
            return false;

        return true;
    }

    private static void onPlayerDefense(Player player) {
        PlayerStateMachineSchedule.player_state_map.put(player.getUniqueId(), new DefenseStateTask(player));
    }

    @Override
    public void run() {
        PlayerBus.banPlayerLeftClickAnimation(player, 2);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 4, 4));

        ++tick;

        if (isPlayerDefense(player))
            onPlayerDefense(player);

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
                PlayerStateMachineSchedule.player_state_map.put(player.getUniqueId(), new NormalStateTask(player));
                break;
        }
    }
}
