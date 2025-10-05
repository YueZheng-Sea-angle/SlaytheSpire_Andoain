package Andoain.powers;

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
import com.megacrit.cardcrawl.powers.StrengthPower;

public class FallenHeavenPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("FallenHeaven");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    // 弹药系统引用
    private AmmunitionPower ammoPower;

    public FallenHeavenPower(AbstractCreature owner) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF; // 设置为BUFF类型以防止被清除
        this.isTurnBased = false; // 不是回合制效果
        this.canGoNegative = false;

        // 加载图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/duotian84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/duotian32.png"),
                0, 0, 32, 32);

        // 获取当前弹药能力引用
        if (owner.hasPower(AmmunitionPower.POWER_ID)) {
            this.ammoPower = (AmmunitionPower)owner.getPower(AmmunitionPower.POWER_ID);
        }

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }



    @Override
    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            // 每回合开始时获得1能量和1梦境
            flash();
            addToBot(new GainEnergyAction(1));
            addToBot(new ApplyPowerAction(
                    owner, owner,
                    new DreamPower(owner, 1),
                    1
            ));
        }
    }

    @Override
    public void onRemove() {
    }
}