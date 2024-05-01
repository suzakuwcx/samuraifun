package Task.StateTask;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Assert.Config.State;
import Assert.Entity.HidariDoSwipeAnimation;
import Assert.Entity.KesagiriSwipeAnimation;
import Assert.Item.Sword;
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
        if (tick < 1) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 1);
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1017), 0, 3, 6);
            state.current_sword_frame = 1017;
        } else if (tick < 2) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1018), 0, 3, 6);
            state.current_sword_frame = 1018;
        } else if (tick < 6) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
            state.current_sword_frame = 1009;
        } else {
            finish();
        }
    }

    private void level_2() {
        if (tick < 1) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 1);
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1017), 0, 3, 6);
            state.current_sword_frame = 1017;
        } else if (tick < 2) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1018), 0, 3, 6);
            state.current_sword_frame = 1018;
        } else if (tick < 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
            state.current_sword_frame = 1009;
        } else if (tick < 7) {
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
        } else if (tick < 8) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1005), 0, 3, 6);
            state.current_sword_frame = 1005;
        } else if (tick < 12) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1006), 0, 3, 6);
            state.current_sword_frame = 1006;
        } else {
            finish();
        }
    }

    private void level_3() {
        if (tick == 1) {
            Vector vec = player.getEyeLocation().getDirection().setY(0).normalize();
            player.setVelocity(vec.multiply(new Vector(1.5, 0, 1.5)));
        } else if (tick == 5) {
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 4);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 3);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 2);
            ItemDisplayAnimationTask.execute(new HidariDoSwipeAnimation(player.getEyeLocation(), 2), 1);
        } else if (tick < 6) {
            ServerBus.playServerSound(player.getLocation(), Sound.UI_TOAST_OUT, 3f, 0.1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1017), 0, 3, 6);
            state.current_sword_frame = 1017;
        } else if (tick < 7) {
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, 1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.1f, 0.5f);
            ServerBus.playServerSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.5f);
            PlayerBus.setPlayerInventoryList(player, new Sword(1018), 0, 3, 6);
            state.current_sword_frame = 1018;
        } else if (tick < 11) {
            PlayerBus.setPlayerInventoryList(player, new Sword(1009), 0, 3, 6);
            state.current_sword_frame = 1009;
        } else {
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
