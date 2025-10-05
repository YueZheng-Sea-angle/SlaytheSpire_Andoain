package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AngelPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("Angel");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final boolean isUpgraded;

    public AngelPower(AbstractCreature owner, boolean isUpgraded) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.isUpgraded = isUpgraded;
        this.type = PowerType.BUFF;

        // 使用光赐于苦资源作为美术资源
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/angel84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/angel32.png"), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        int strengthAmount = isUpgraded ? 2 : 1;
        this.description = String.format(powerStrings.DESCRIPTIONS[0], strengthAmount);
    }

    @Override
    public void atStartOfTurn() {
        // 每回合开始时获得2层梦境
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new DreamPower(owner, 2), 2));
    }

    public void onEnterDeepDream() {
        // 加入迷梦时获得力量
        flash();
        int strengthAmount = isUpgraded ? 2 : 1;
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthAmount), strengthAmount));
    }

    public void onExitDeepDream() {
        // 离开迷梦时获得力量
        flash();
        int strengthAmount = isUpgraded ? 2 : 1;
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthAmount), strengthAmount));
    }
}