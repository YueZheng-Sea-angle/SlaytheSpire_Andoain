package Andoain.powers;

import Andoain.helpers.ModHelper;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ThriftyHabitsPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("ThriftyHabits");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private final int chance;
    private boolean ricochetFailed;

    public ThriftyHabitsPower(AbstractCreature owner, int chance) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.chance = chance;
        this.ricochetFailed = false;

// 加载图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/Ammunitionplus84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/Ammunitionplus32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }



    public void onRicochetFail() {
        if (!ricochetFailed) {
            ricochetFailed = true;
            flash();
        }
    }

    public int onSpendAmmo() {
        if(ricochetFailed){
            BaseMod.logger.info("back.");
            flash();
            ricochetFailed = false;
            return 1;
        }
        if ((AbstractDungeon.cardRandomRng.random(0, 99) >= chance)) {
            BaseMod.logger.info("lucky.");
            if(!ricochetFailed){
            flash();
            return 1;
        }
        }
        BaseMod.logger.info("unlucky.");
        return 0;
    }
}