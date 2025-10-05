package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DarknessBeneathTheLightPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("DarknessBeneathTheLight");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DarknessBeneathTheLightPower(AbstractCreature owner, int maxPlays) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = maxPlays;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        // 测试阶段统一使用光赐于苦美术资源代替
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/whiteb84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/whiteb32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 0) {
            description = powerStrings.DESCRIPTIONS[0];
        } else {
            description = powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[2];
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