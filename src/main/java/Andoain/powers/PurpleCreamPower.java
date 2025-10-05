package Andoain.powers;

import Andoain.cards.IceCreamPurple;
import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PurpleCreamPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("PurpleCream");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public PurpleCreamPower(AbstractCreature owner) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // 使用光赐于苦美术资源代替
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/xiangyubingqilin84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/xiangyubingqilin32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        if (IceCreamPurple.hasFriendlyIceCreamMachine()) {
            // 有友方冰淇淋机时获得能量
            addToBot(new GainEnergyAction(1));
            flash();
        } else if(IceCreamPurple.hasEnemyIceCreamMachine()){
            // 否则获得1层光赐于苦
            addToBot(new ApplyPowerAction(
                    owner, owner,
                    new LightUntoSufferers(owner, 1),
                    1
            ));
            addToBot(new ApplyPowerAction(
                    owner, owner,
                    new DreamPower(owner, 2),
                    2
            ));
        }
    }
}