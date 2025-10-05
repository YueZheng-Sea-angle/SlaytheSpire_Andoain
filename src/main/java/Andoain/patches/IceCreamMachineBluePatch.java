package Andoain.patches;

import Andoain.monster.IceCreamMachineBlue;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class IceCreamMachineBluePatch {
    // 存储冰淇淋机实例
    public static IceCreamMachineBlue iceCreamMachineBlue = null;

    // 渲染补丁
    @SpirePatch(clz = AbstractRoom.class, method = "render")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void render(AbstractRoom __instance, SpriteBatch sb) {
            if (iceCreamMachineBlue != null && !iceCreamMachineBlue.isDeadOrEscaped()) {
                iceCreamMachineBlue.render(sb);
            }
        }
    }

    // 更新补丁
    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class UpdatePatch {
        @SpirePostfixPatch
        public static void update(AbstractRoom __instance) {
            if (iceCreamMachineBlue != null && !iceCreamMachineBlue.isDeadOrEscaped()) {
                iceCreamMachineBlue.update();
            }
        }
    }

    // 回合开始处理
    @SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class TurnStartPatch {
        @SpirePostfixPatch
        public static void onTurnStart(GameActionManager __instance) {
            if (iceCreamMachineBlue != null && !iceCreamMachineBlue.isDeadOrEscaped()) {
                iceCreamMachineBlue.takeTurn();
            }
        }
    }

    // 伤害传递补丁
    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class DamagePatch {
        @SpirePostfixPatch
        public static void onDamage(AbstractPlayer __instance, DamageInfo info) {
            if (iceCreamMachineBlue != null && !iceCreamMachineBlue.isDeadOrEscaped()) {
                int damageToTake = (int)(info.output * 0.5f);
                if (damageToTake > 0) {
                    iceCreamMachineBlue.damage(new DamageInfo(info.owner, damageToTake, info.type));
                }
            }
        }
    }
}