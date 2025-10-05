package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShadowAndAsh extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("ShadowAndAsh");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("ShadowAndAsh");
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.POWER;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    public ShadowAndAsh() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.isEthereal = true; // 初始为虚无
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.isEthereal = false; // 取消虚无
            this.selfRetain = true;  // 获得保留
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!p.hasPower(LightUntoSufferers.POWER_ID)) {
            this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查是否需要转阶段
        if (p instanceof Andoain.character.andoain && !((Andoain.character.andoain)p).isSecondStage()) {
            Andoain.character.andoain.onRevive();
            float delay = Settings.FAST_MODE ? 0.4f : 0.8f;
            addToBot(new WaitAction(delay));
        }
        
        // 播放Guiding.ogg音乐
        playGuidingBGM();
        
        // 应用持续效果
        if(!p.hasPower(ShadowAndAshPower.POWER_ID)){
        addToBot(new ApplyPowerAction(p, p, new ShadowAndAshPower(p)));}

        // 处理弹药逻辑
        AmmunitionPower ammo = (AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID);
        if (ammo != null) {
            ammo.setMax(0);
            ammo.amount = 0;
            ammo.updateDescription();
        } else {
            ammo = new AmmunitionPower(p, 0);
            addToBot(new ApplyPowerAction(p, p, ammo));
        }
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