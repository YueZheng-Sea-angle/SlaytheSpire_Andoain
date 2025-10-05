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

public class NoAmmoGainPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("NoAmmoGain");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    // 弹药系统引用
    private AmmunitionPower ammoPower;

    public NoAmmoGainPower(AbstractCreature owner, int turns) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = turns;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        // 加载图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/Ammunitionban84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/Ammunitionban32.png"),
                0, 0, 32, 32);

        // 获取当前弹药能力引用
        if (owner.hasPower(AmmunitionPower.POWER_ID)) {
            this.ammoPower = (AmmunitionPower)owner.getPower(AmmunitionPower.POWER_ID);
        }

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = amount == 1 ?
                powerStrings.DESCRIPTIONS[0] :
                String.format(powerStrings.DESCRIPTIONS[1], amount);
    }

    @Override
    public void atEndOfRound() {
        if (this.amount <= 1) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            addToTop(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    // 核心拦截逻辑--------------------------------------------------
    @Override
    public void onInitialApplication() {
        if (ammoPower != null) {
            ammoPower.canGain = false; // 立即禁用弹药获取
        }
    }

    @Override
    public void onRemove() {
        if (ammoPower != null) {
            ammoPower.canGain = true; // 恢复弹药获取
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        // 叠加时保持封锁状态
        if (ammoPower != null) {
            ammoPower.canGain = false;
        }
    }

}