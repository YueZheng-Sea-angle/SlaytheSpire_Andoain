package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class MuddyBurnPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("MuddyBurn");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final int THRESHOLD = 2;
    private static final int LIFE_LOSS = 10;
    private static final int IMMUNITY_AMOUNT = 2;
    private static final int VULNERABLE_AMOUNT = 2;

    public MuddyBurnPower(AbstractCreature owner, int amount) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;

        // 使用光赐于苦美术资源代替
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/zhuoransunshang84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/zhuoransunshang32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(
                powerStrings.DESCRIPTIONS[0],
                THRESHOLD,
                LIFE_LOSS,
                VULNERABLE_AMOUNT,
                IMMUNITY_AMOUNT
        );
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);

        // 检查是否达到阈值
        if (this.amount >= THRESHOLD) {
            flash();

            // 触发效果
            addToTop(new DamageAction(
                    owner,
                    new DamageInfo(owner, LIFE_LOSS, DamageInfo.DamageType.HP_LOSS),
                    AbstractGameAction.AttackEffect.FIRE
            ));

            addToTop(new ApplyPowerAction(
                    owner, owner,
                    new VulnerablePower(owner, VULNERABLE_AMOUNT, false),
                    VULNERABLE_AMOUNT
            ));

            addToTop(new ApplyPowerAction(
                    owner, owner,
                    new MuddyBurnImmunityPower(owner, IMMUNITY_AMOUNT),
                    IMMUNITY_AMOUNT
            ));

            // 移除自身
            addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }
}