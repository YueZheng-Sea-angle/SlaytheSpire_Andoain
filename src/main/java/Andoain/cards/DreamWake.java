package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.DeepDreamPower;
import Andoain.powers.DreamPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class DreamWake extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("DreamWake");
    private static final CardStrings CARD_STRINGS;
    static {
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    private static final int COST = 2;
    private static final int BASE_DAMAGE = 12;

    public DreamWake() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("DreamWake"),
                COST,
                CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK,
                CardColorEnum.Andoain_Blue,
                CardRarity.RARE,
                CardTarget.ENEMY
        );
        this.baseDamage = BASE_DAMAGE;
        this.hasRicochet = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
        }
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        boolean inDeepDream = AbstractDungeon.player != null &&
                AbstractDungeon.player.hasPower(DeepDreamPower.POWER_ID);

        // 只在迷梦状态下计算额外伤害
        if (inDeepDream && AbstractDungeon.player.hasPower(DreamPower.POWER_ID)) {
            AbstractPower dreamPower = AbstractDungeon.player.getPower(DreamPower.POWER_ID);
            if (dreamPower != null) {
                this.baseDamage += ((DreamPower)dreamPower).totalDreamGainedThisCombat;
            }
        }

        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
        updateDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;
        boolean inDeepDream = AbstractDungeon.player != null &&
                AbstractDungeon.player.hasPower(DeepDreamPower.POWER_ID);

        // 只在迷梦状态下计算额外伤害
        if (inDeepDream && AbstractDungeon.player.hasPower(DreamPower.POWER_ID)) {
            AbstractPower dreamPower = AbstractDungeon.player.getPower(DreamPower.POWER_ID);
            if (dreamPower != null) {
                this.baseDamage += ((DreamPower)dreamPower).totalDreamGainedThisCombat;
            }
        }

        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放Guiding.ogg音乐
        playGuidingBGM();
        
        // 播放技能动画
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 3.0f : 4.0f;
        addToBot(new WaitAction(delay));

        // 只在迷梦状态下应用额外伤害
        int finalDamage = this.damage; // 已经通过applyPowers/calculateCardDamage计算过加成的基础伤害
        if (p.hasPower(DeepDreamPower.POWER_ID) && p.hasPower(DreamPower.POWER_ID)) {

        }

        addToBot(new DamageAction(
                m,
                new DamageInfo(p, finalDamage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        ));

        handleRicochet(p, m);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(DeepDreamPower.POWER_ID)) {
            return true;
        }
        return super.canUse(p, m);
    }

    private void updateDescription() {
        int extraDamage = 0;
        boolean inDeepDream = AbstractDungeon.player != null &&
                AbstractDungeon.player.hasPower(DeepDreamPower.POWER_ID);

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(DreamPower.POWER_ID)) {
            AbstractPower dreamPower = AbstractDungeon.player.getPower(DreamPower.POWER_ID);
            if (dreamPower != null) {
                extraDamage = ((DreamPower) dreamPower).totalDreamGainedThisCombat;
            }
        }

        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        if (extraDamage > 0) {
            if (CARD_STRINGS.EXTENDED_DESCRIPTION != null && CARD_STRINGS.EXTENDED_DESCRIPTION.length > 0) {
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