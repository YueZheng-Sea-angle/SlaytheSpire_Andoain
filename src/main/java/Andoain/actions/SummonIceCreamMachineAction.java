package Andoain.actions;

import Andoain.modcore.AndoainMod;
import Andoain.monster.IceCreamMachine;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SummonIceCreamMachineAction extends AbstractGameAction {
    private final float targetX;
    private final float targetY;
    private final boolean isFriendlyMinion;
    private final boolean isDead;
    private final boolean isAlly;
    private final boolean grantIntangible;

    public SummonIceCreamMachineAction(float targetX, float targetY, boolean isAlly, boolean isFriendlyMinion, boolean isDead) {
        this(targetX, targetY, isAlly, isFriendlyMinion, isDead, false);
    }

    public SummonIceCreamMachineAction(float targetX, float targetY, boolean isAlly, boolean isFriendlyMinion, boolean isDead, boolean grantIntangible) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST; // 使用更短的持续时间
        this.isAlly = isAlly;
        this.isFriendlyMinion = isFriendlyMinion;
        this.isDead = isDead;
        this.grantIntangible = grantIntangible;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // 直接创建并放置召唤物
            IceCreamMachine machine = new IceCreamMachine(0, 0);
            machine.setAlly(this.isAlly);
            machine.drawX = this.targetX;
            machine.drawY = this.targetY;
            machine.setRotation(0);

            // 添加到场景
            AbstractDungeon.getCurrRoom().monsters.addMonster(machine);
            machine.init();
            machine.showHealthBar();
            machine.applyPowers();
            machine.createIntent();

            // 如果需要添加无实体
            if (this.grantIntangible && !machine.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToTop(
                        new ApplyPowerAction(
                                machine,
                                machine,
                                new IntangiblePlayerPower(machine, 1),
                                1
                        )
                );
            }

            // 触发遗物效果
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onSpawnMonster(machine);
            }

            // 显示教程提示（如果需要）
            if (!AndoainMod.iceCreamTutorialShown && !this.isDead) {
                AbstractDungeon.actionManager.addToTop(
                        new ShowIceCreamTutorialAction(machine, 2)
                );
                AbstractDungeon.actionManager.addToTop(
                        new ShowIceCreamTutorialAction(machine, 1)
                );
                AbstractDungeon.actionManager.addToTop(
                        new ShowIceCreamTutorialAction(machine, 0)
                );
            }
        }

        tickDuration();
    }
}