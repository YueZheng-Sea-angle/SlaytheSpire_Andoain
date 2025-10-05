package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FirnLightModifierPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("FirnLightModifier");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    int turnCounter = 0;

    public FirnLightModifierPower(AbstractCreature owner) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = -1;
        this.isTurnBased = false;
        this.turnCounter = 0;


        // 使用光赐于苦的美术资源
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/woyuanxiangqian84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/woyuanxiangqian32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (owner.hasPower(LightUntoSufferers.POWER_ID)) {
        turnCounter++;
    }
    else{
        this.turnCounter = 0;
        }
    }

    @Override
    public void updateDescription() {
        this.description =
                powerStrings.DESCRIPTIONS[0];
        ;
    }
}