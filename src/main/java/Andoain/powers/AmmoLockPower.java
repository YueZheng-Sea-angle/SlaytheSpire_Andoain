package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AmmoLockPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("AmmoLock");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    // 弹药系统引用
    private AmmunitionPower ammoPower;

    public AmmoLockPower(AbstractCreature owner) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF; // 明确标记为DEBUFF
        this.isTurnBased = false;
        this.canGoNegative = false;

        // 加载图标（可以使用不同的图标来区分）
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
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void onInitialApplication() {
        // 永久禁用弹药获取
        if (ammoPower != null) {
            ammoPower.canGain = false;
        }
    }

    @Override
    public void onRemove() {
        // 移除时恢复弹药获取能力
        if (ammoPower != null) {
            ammoPower.canGain = true;
        }
    }
}