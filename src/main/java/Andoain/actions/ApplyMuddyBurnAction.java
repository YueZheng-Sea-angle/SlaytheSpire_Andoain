package Andoain.actions;

import Andoain.powers.MuddyBurnImmunityPower;
import Andoain.powers.MuddyBurnPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ApplyMuddyBurnAction extends AbstractGameAction {
    public ApplyMuddyBurnAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
    }

    @Override
    public void update() {
        // 如果目标有浊燃损伤免疫，则不应用
        if (target.hasPower(MuddyBurnImmunityPower.POWER_ID)) {
            this.isDone = true;
            return;
        }

        // 应用浊燃损伤
        AbstractDungeon.actionManager.addToTop(
                new ApplyPowerAction(target, source, new MuddyBurnPower(target, amount), amount)
        );
        this.isDone = true;
    }
}