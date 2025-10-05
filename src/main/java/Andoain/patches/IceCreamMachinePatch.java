package Andoain.patches;

import Andoain.monster.IceCreamMachine;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class IceCreamMachinePatch {

    // 更精确的清除条件检查
    private static boolean shouldClearMachines() {
        // 1. 确保当前房间可以正常结束战斗
        if (AbstractDungeon.getCurrRoom().cannotLose) {
            return false;
        }

        // 2. 检查是否所有非冰淇淋机敌人都已死亡
        boolean allNonMachineEnemiesDead = true;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!(m instanceof IceCreamMachine) && !m.isDeadOrEscaped()) {
                allNonMachineEnemiesDead = false;
                break;
            }
        }

        return allNonMachineEnemiesDead;
    }

    // 安全清除所有冰淇淋机
    public static void clearAllMachinesIfNeeded() {
        if (shouldClearMachines()) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m instanceof IceCreamMachine && !m.isDeadOrEscaped()) {
                    m.die(); // 静默清除，不触发动画
                }
            }
        }
    }

    // 更精确的死亡事件监听
    @SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
    public static class DiePatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster _inst, boolean triggerRelics) {
            // 确保死亡的不是冰淇淋机
            if (!(_inst instanceof IceCreamMachine)) {
                clearAllMachinesIfNeeded();
            }
        }
    }

    // 更精确的逃跑事件监听
    @SpirePatch(clz = AbstractMonster.class, method = "escape")
    public static class EscapePatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster _inst) {
            // 确保逃跑的不是冰淇淋机
            if (!(_inst instanceof IceCreamMachine)) {
                clearAllMachinesIfNeeded();
            }
        }
    }
}