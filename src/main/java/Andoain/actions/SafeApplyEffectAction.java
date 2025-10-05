package Andoain.actions;

import Andoain.powers.EchoPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SafeApplyEffectAction extends AbstractGameAction {
    private final AbstractGameAction action;

    public SafeApplyEffectAction(AbstractGameAction action) {
        this.action = action;
    }

    @Override
    public void update() {
        EchoPower power = (EchoPower) AbstractDungeon.player.getPower(EchoPower.POWER_ID);

        // 设置标志行动
        AbstractGameAction setFlag = new AbstractGameAction() {
            @Override
            public void update() {
                if (power != null) {
                    power.isTransferring = true;
                }
                this.isDone = true;
            }
        };

        // 清除标志行动
        AbstractGameAction clearFlag = new AbstractGameAction() {
            @Override
            public void update() {
                if (power != null) {
                    power.isTransferring = false;
                }
                this.isDone = true;
            }
        };

        // 按顺序添加行动：setFlag -> 主行动 -> clearFlag
        AbstractDungeon.actionManager.addToTop(clearFlag);
        AbstractDungeon.actionManager.addToTop(this.action);
        AbstractDungeon.actionManager.addToTop(setFlag);

        this.isDone = true;
    }
}