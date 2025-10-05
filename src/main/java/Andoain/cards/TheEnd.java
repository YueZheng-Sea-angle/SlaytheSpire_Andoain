package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

import java.util.ArrayList;
import java.util.List;

public class TheEnd extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("TheEnd");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int BASE_DAMAGE = 30;
    private static final int UPGRADED_DAMAGE = 40;
    private static final int AMMO_BONUS = 2;

    public TheEnd() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("TheEnd"),
                -1, // X费
                CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK,
                CardColorEnum.Andoain_Blue,
                CardRarity.RARE,
                CardTarget.ALL_ENEMY
        );
        this.baseDamage = BASE_DAMAGE;
        this.isMultiDamage = true;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADED_DAMAGE - BASE_DAMAGE);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放技能动画
        if (p instanceof Andoain.character.andoain) {
            Andoain.character.andoain.onSkill(1); // 播放1次技能循环
            float delay = Settings.FAST_MODE ? 3.0f : 4.0f;
            addToBot(new WaitAction(delay));
        }
        
        // 播放Guiding.ogg音乐
        playGuidingBGM();
        
        int x = this.energyOnUse;
        if (p.hasRelic("Chemical X")) {
            x += 2;
        }

        // 执行前X项效果
        if (x >= 1) {
            clearNegativeEffects(p); // 第1项:清空负面效果
        }
        if (x >= 2) {
            if (p.hasPower("AndoainMod:NoAmmoGain")) {
                addToBot(new RemoveSpecificPowerAction(p, p, "AndoainMod:NoAmmoGain"));
            }
            enhanceAmmo(p); // 第2项:弹药上限+2并补满
        }
        if (x >= 3) {
            dealDamageMultipleTimes(p, x - 2); // 第3项及以后:多次伤害
        }
        if (!freeToPlay()) {
            p.energy.use(this.energyOnUse);
        }
    }

    // 清空负面效果
    private void clearNegativeEffects(AbstractPlayer p) {
        List<AbstractPower> toRemove = new ArrayList<>();
        for (AbstractPower power : p.powers) {
            if (isNegativePower(power)) {
                toRemove.add(power);
            }
        }

        for (AbstractPower power : toRemove) {
            addToBot(new RemoveSpecificPowerAction(p, p, power.ID));
        }
    }

    // 判断是否为负面效果
    private boolean isNegativePower(AbstractPower power) {
        return power.type == AbstractPower.PowerType.DEBUFF;
    }

    // 增强弹药
    private void enhanceAmmo(AbstractPlayer p) {
        if (p.hasPower(AmmunitionPower.POWER_ID)) {
            AmmunitionPower ammo = (AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID);
            ammo.setMax(ammo.currentMax + AMMO_BONUS);
            ammo.replenish(ammo.currentMax);
        } else {
            // 如果没有弹药能力则添加新的
            addToBot(new ApplyPowerAction(
                    p, p,
                    new AmmunitionPower(p, AMMO_BONUS, AMMO_BONUS),
                    AMMO_BONUS));
        }
    }

    // 多次造成伤害
    private void dealDamageMultipleTimes(AbstractPlayer p, int times) {
        for (int i = 0; i < times; i++) {
            addToBot(new DamageAllEnemiesAction(
                    p,
                    this.multiDamage,
                    this.damageTypeForTurn,
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheEnd();
    }
    
    private void playGuidingBGM() {
        try {
            // 检查是否已经在播放自定义音乐，如果是则不重复播放
            if (Andoain.helpers.ModHelper.isCustomMusicPlaying()) {
                System.out.println("自定义音乐已在播放，跳过Guiding.ogg播放");
                return;
            }
            
            // 检查是否需要特殊BGM切换处理（包括boss战斗、第一阶段boss战斗和心灵绽放事件）
            if (Andoain.helpers.ModHelper.needsSpecialBGMSwitch()) {
                // Boss战或特殊场景时，立即停止Boss音乐和层数音乐
                CardCrawlGame.music.silenceTempBgmInstantly();
                CardCrawlGame.music.silenceBGMInstantly();
                // System.out.println("检测到特殊战斗场景，立即停止当前音乐并切换为Guiding.ogg");
            } else if (Andoain.helpers.ModHelper.isFightingLagavulin()) {
                // 乐加维林战斗时，需要同时淡出临时音乐（ELITE）和层数音乐
                CardCrawlGame.music.fadeOutTempBGM();
                CardCrawlGame.music.fadeOutBGM();
                // System.out.println("检测到乐加维林战斗，淡出ELITE音乐和层数音乐并切换为Guiding.ogg");
            } else {
                // 普通战斗时，淡出层数音乐
                CardCrawlGame.music.fadeOutBGM();
                // System.out.println("普通战斗，淡出当前音乐并切换为Guiding.ogg");
            }
            // 播放自定义音乐
            CardCrawlGame.music.playTempBgmInstantly("Guiding.ogg", true);
        } catch (Exception e) {
            System.out.println("播放Guiding.ogg时出现异常: " + e.getMessage());
        }
    }
}