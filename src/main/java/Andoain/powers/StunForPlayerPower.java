package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class StunForPlayerPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("StunForPlayer");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public StunForPlayerPower(AbstractCreature owner, int maxCards) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = maxCards; // 最大可出牌数
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        // 使用光赐于苦美术资源代替
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/yunyun84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/yunyun32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = powerStrings.DESCRIPTIONS[0];
        } else {
            description = amount + powerStrings.DESCRIPTIONS[1];
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (amount > 0) {
            amount--;
            updateDescription();
        }
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        // 当剩余次数为0时，禁止打出更多牌
        return amount > 0;
    }
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }
}