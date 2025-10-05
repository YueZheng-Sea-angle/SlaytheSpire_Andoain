package Andoain.powers;

import Andoain.cards.AbstractAndoainCard;
import Andoain.cards.UnInvited;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Andoain.cards.UnInvited.LIGHT_GAIN;
import static Andoain.cards.UnInvited.UPGRADE_LIGHT_GAIN;

public class FreeNextCardPower extends AbstractPower {
    public static final String POWER_ID = "AndoainMod:FreeNextCardPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);


    public FreeNextCardPower(AbstractCreature owner, int amount) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        updateDescription();
        this.loadRegion("swivel");
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount > 0) {
            this.flash();
            card.freeToPlayOnce = true;
            action.exhaustCard = true;

            // 添加DEBUG日志
            System.out.println("[DEBUG] Processing free card: " + card.cardID);
            System.out.println("[DEBUG] Is team card: " + UnInvited.isTeamCard(card));


            this.amount--;
            if (this.amount <= 0) {
                addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
            }
            card.freeToPlayOnce = false;
        }
    }

    /************************ 新增方法 ************************/
    // 检查是否是小队牌
    private boolean isTeamCard(AbstractCard card) {
        // 优先调用UnInvited的静态检查方法
        return UnInvited.isTeamCard(card);
    }
}