package Andoain.powers;

import Andoain.helpers.ModHelper;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;

public class LightUntoSufferers extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("LightUntoSufferers");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    // 基础数值
    private static final float BASE_BUFFER_CHANCE = 0.3f; // 30%概率
    private static final float BASE_HEAL_RATIO = 0.5f;    // 50%回血
    // 千层蛋糕增强后的数值
    private static final float ENHANCED_BUFFER_CHANCE = 0.7f; // 70%概率
    private static final float ENHANCED_HEAL_RATIO = 0.7f;    // 70%回血

    public static int totalDamageTakenInCombat = 0;

    public LightUntoSufferers(AbstractCreature owner, int stacks) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = stacks;
        this.type = PowerType.BUFF;

        // 加载图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/guangci84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/guangci32.png"),
                0, 0, 32, 32);

        updateDescription();
        if (owner.isPlayer && hasTheWorld()) {
            addToBot(new DrawCardAction(1));
            BaseMod.logger.info("LightWorld.");
        }
    }
    // 添加检查方法
    private boolean hasTheWorld() {
        return owner != null && owner.isPlayer &&
                ((AbstractPlayer)owner).hasPower(TheWorldPower.POWER_ID);
    }
    @Override
    public void updateDescription() {
        float bufferChance = hasNiceCake() ? ENHANCED_BUFFER_CHANCE : BASE_BUFFER_CHANCE;
        float healRatio = hasNiceCake() ? ENHANCED_HEAL_RATIO : BASE_HEAL_RATIO;

        this.description = String.format(
                powerStrings.DESCRIPTIONS[0],
                (int)(bufferChance * 100),
                (int)(healRatio * 100));

        if (hasNiceCake()) {
            this.description += " NL #y千层蛋糕: 效果增强。";
        }
    }

    // 检查玩家是否有千层蛋糕能力
    private boolean hasNiceCake() {
        return owner != null && owner.isPlayer &&
                ((AbstractPlayer)owner).hasPower(NiceCakePower.POWER_ID);
    }

    @Override
    public void atStartOfTurn() {
        boolean shouldSlowDecay = owner.hasPower(FirnLightModifierPower.POWER_ID);
        float bufferChance = hasNiceCake() ? ENHANCED_BUFFER_CHANCE : BASE_BUFFER_CHANCE;

        if (AbstractDungeon.monsterRng.randomBoolean(bufferChance)) {
            flash();
            addToBot(new ApplyPowerAction(
                    owner, owner,
                    new BufferPower(owner, 1), 1));
        }

        if (shouldSlowDecay) {
            FirnLightModifierPower firnPower =
                    (FirnLightModifierPower)owner.getPower(FirnLightModifierPower.POWER_ID);
            if (firnPower != null) {
                if (firnPower.turnCounter % 2 != 0) {
                    addToTop(new ReducePowerAction(owner, owner, ID, 1));
                }
            }
        } else {
            addToTop(new ReducePowerAction(owner, owner, ID, 1));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            flash();
            totalDamageTakenInCombat += damageAmount;
            float healRatio = hasNiceCake() ? ENHANCED_HEAL_RATIO : BASE_HEAL_RATIO;
            addToBot(new HealAction(
                    owner, owner,
                    (int)(damageAmount * healRatio)));
        }
        return damageAmount;
    }

    public static void resetTotalDamageTaken() {
        totalDamageTakenInCombat = 0;
    }
}