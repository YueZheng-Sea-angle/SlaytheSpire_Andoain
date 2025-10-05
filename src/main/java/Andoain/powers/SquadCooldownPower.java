package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SquadCooldownPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("SquadCooldown");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private final String cardId;

    public SquadCooldownPower(AbstractCreature owner, String cardId, int turns) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID + "_" + cardId;
        this.owner = owner;
        this.cardId = cardId;
        this.amount = turns;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        // 加载图标资源
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/xiaoduilengque84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/xiaoduilengque32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        // 从卡牌ID中提取简名
        String cardName = cardId;
        if (cardId.contains(":")) {
            cardName = cardId.split(":")[1];
        }

        this.description = String.format(
                powerStrings.DESCRIPTIONS[0],
                cardName,
                this.amount
        );
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount <= 1) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            addToTop(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void onInitialApplication() {
        flashWithoutSound();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        flash();
    }
}