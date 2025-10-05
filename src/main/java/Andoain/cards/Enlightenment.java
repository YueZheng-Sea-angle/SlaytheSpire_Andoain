package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.List;

public class Enlightenment extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Enlightenment");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Enlightenment");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int DAMAGE = 15;
    private static final int DEBUFF_REDUCTION = 1;

    public Enlightenment() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.hasRicochet = true; // 启用弹射功能
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放Guiding.ogg音乐
        playGuidingBGM();
        
        // 检查是否有弹药，有弹药才播放攻击动画
        boolean hasAmmo = p.hasPower(AmmunitionPower.POWER_ID) &&
                ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).amount > 0;
        
        if (hasAmmo) {
            andoain.onAttack();
            float delay = Settings.FAST_MODE ? 0.7f : 1.4f;
            addToBot(new WaitAction(delay));
        }

        // 1. 对主目标造成伤害
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        ));

        // 2. 清除目标的力量加成
        if (m.hasPower(StrengthPower.POWER_ID)) {
            addToBot(new RemoveSpecificPowerAction(
                    m, p, StrengthPower.POWER_ID
            ));
        }

        // 3. 清除自身的异常状态
        clearPlayerDebuffs(p);

        // 4. 处理弹射效果
        handleRicochet(p, m);
    }

    // 清除玩家自身的异常状态
    private void clearPlayerDebuffs(AbstractPlayer p) {
        List<AbstractPower> debuffs = new ArrayList<>();

        // 收集所有异常状态
        for (AbstractPower power : p.powers) {
            if (power.type == PowerType.DEBUFF) {
                debuffs.add(power);
            }
        }

        // 根据升级状态清除异常状态
        for (AbstractPower debuff : debuffs) {
            if (this.upgraded) {
                // 升级后：完全清除异常状态
                addToBot(new RemoveSpecificPowerAction(p, p, debuff.ID));
            } else {
                // 基础：减少1层异常状态
                if (debuff.amount > 0) {
                    addToBot(new ReducePowerAction(p, p, debuff.ID, DEBUFF_REDUCTION));
                }
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Enlightenment();
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
                System.out.println("检测到特殊战斗场景，立即停止当前音乐并切换为Guiding.ogg");
            } else if (Andoain.helpers.ModHelper.isFightingLagavulin()) {
                // 乐加维林战斗时，需要同时淡出临时音乐（ELITE）和层数音乐
                CardCrawlGame.music.fadeOutTempBGM();
                CardCrawlGame.music.fadeOutBGM();
                System.out.println("检测到乐加维林战斗，淡出ELITE音乐和层数音乐并切换为Guiding.ogg");
            } else {
                // 普通战斗时，淡出层数音乐
                CardCrawlGame.music.fadeOutBGM();
                System.out.println("普通战斗，淡出当前音乐并切换为Guiding.ogg");
            }
            // 播放自定义音乐
            CardCrawlGame.music.playTempBgmInstantly("Guiding.ogg", true);
        } catch (Exception e) {
            System.out.println("播放Guiding.ogg时出现异常: " + e.getMessage());
        }
    }
}