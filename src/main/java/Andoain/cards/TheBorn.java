package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TheBorn extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("TheBorn");
    private static final CardStrings CARD_STRINGS;
    static {
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    private static final int COST = 3;
    private static final int BASE_DAMAGE = 14;

    public TheBorn() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("TheBorn"),
                COST,
                CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK,
                CardColorEnum.Andoain_Blue,
                CardRarity.RARE,
                CardTarget.ALL_ENEMY
        );
        this.baseDamage = BASE_DAMAGE;
        this.isMultiDamage = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(8); // 升级后增加基础伤害
        }
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;

        // 计算额外伤害(光赐于苦状态下受伤害量总和的两倍)
        if (AbstractDungeon.player != null &&
                AbstractDungeon.player.hasPower(LightUntoSufferers.POWER_ID)) {
            this.baseDamage += LightUntoSufferers.totalDamageTakenInCombat * 2;
        }

        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
        updateDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;

        // 计算额外伤害(光赐于苦状态下受伤害量总和的两倍)
        if (AbstractDungeon.player != null &&
                AbstractDungeon.player.hasPower(LightUntoSufferers.POWER_ID)) {
            this.baseDamage += LightUntoSufferers.totalDamageTakenInCombat * 2;
        }

        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放Angel.ogg音乐
        playAngelBGM();
        
        // 播放技能动画
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 3.0f : 4.0f;
        addToBot(new WaitAction(delay));
        
        // 计算最终伤害(基础伤害+额外伤害)
        calculateCardDamage(null); // 传入null表示对所有敌人计算伤害

        addToBot(new DamageAllEnemiesAction(
                p,
                this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        ));
    }
    
    private void playAngelBGM() {
        try {
            // 检查是否已经在播放自定义音乐，如果是则不重复播放
            if (Andoain.helpers.ModHelper.isCustomMusicPlaying()) {
                System.out.println("自定义音乐已在播放，跳过Angel.ogg播放");
                return;
            }
            
            // 检查是否需要特殊BGM切换处理（包括boss战斗、第一阶段boss战斗和心灵绽放事件）
            if (Andoain.helpers.ModHelper.needsSpecialBGMSwitch()) {
                // Boss战或特殊场景时，立即停止Boss音乐和层数音乐
                CardCrawlGame.music.silenceTempBgmInstantly();
                CardCrawlGame.music.silenceBGMInstantly();
                System.out.println("检测到特殊战斗场景，立即停止当前音乐并切换为Angel.ogg");
            } else if (Andoain.helpers.ModHelper.isFightingLagavulin()) {
                // 乐加维林战斗时，需要同时淡出临时音乐（ELITE）和层数音乐
                CardCrawlGame.music.fadeOutTempBGM();
                CardCrawlGame.music.fadeOutBGM();
                System.out.println("检测到乐加维林战斗，淡出ELITE音乐和层数音乐并切换为Angel.ogg");
            } else {
                // 普通战斗时，淡出层数音乐
                CardCrawlGame.music.fadeOutBGM();
                System.out.println("普通战斗，淡出当前音乐并切换为Angel.ogg");
            }
            // 播放自定义音乐
            CardCrawlGame.music.playTempBgmInstantly("Angel.ogg", true);
        } catch (Exception e) {
            System.out.println("播放Angel.ogg时出现异常: " + e.getMessage());
        }
    }

    private void updateDescription() {
        int extraDamage = 0;
        if (AbstractDungeon.player != null) {
            extraDamage = LightUntoSufferers.totalDamageTakenInCombat * 2;
        }

        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        if (extraDamage > 0) {
            if (CARD_STRINGS.EXTENDED_DESCRIPTION != null &&
                    CARD_STRINGS.EXTENDED_DESCRIPTION.length > 0) {
                this.rawDescription += " NL " + String.format(
                        CARD_STRINGS.EXTENDED_DESCRIPTION[0],
                        extraDamage
                );
            } else {
                this.rawDescription += " NL （额外伤害: " + extraDamage + "）";
            }
        }

        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.initializeDescription();
    }
}