package Andoain.actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SafeApplyPowerAction extends ApplyPowerAction {
    public SafeApplyPowerAction(AbstractCreature target, AbstractCreature source,
                                AbstractPower powerToApply, int stackAmount) {
        super(target, source, createSafePower(target, powerToApply, stackAmount), stackAmount);
    }

    private static AbstractPower createSafePower(AbstractCreature target,
                                                 AbstractPower powerToApply,
                                                 int stackAmount) {
        if (target == null || target.isDeadOrEscaped()) {
            return new AbstractPower() {
                @Override public void updateDescription() {}
                 {}
            };
        }
        return powerToApply;
    }
}