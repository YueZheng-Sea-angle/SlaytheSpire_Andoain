package Andoain.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RetainCardPower extends AbstractPower {
    public static final String POWER_ID = "Andoain:SelectiveRetain";

    public RetainCardPower(AbstractCreature owner, int amount) {
        this.name = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        updateDescription();
        loadRegion("retain");
    }

    @Override
    public void updateDescription() {
        this.description = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).DESCRIPTIONS[0];
    }


    public void onRetained() {
        flash();
        this.addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
    }
}