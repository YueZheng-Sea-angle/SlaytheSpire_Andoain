package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.FirnLightModifierPower;
import Andoain.powers.LightUntoSufferers;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Firn extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Firn");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    private static final int BASE_AMMO = 4;

    public Firn() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("Firn"),
                COST,
                CARD_STRINGS.DESCRIPTION,
                CardType.POWER,
                CardColorEnum.Andoain_Blue,
                CardRarity.RARE,
                CardTarget.SELF
        );
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查是否需要转阶段
        if (p instanceof Andoain.character.andoain && !((Andoain.character.andoain)p).isSecondStage()) {
            Andoain.character.andoain.onRevive();
            float delay = Settings.FAST_MODE ? 0.4f : 0.8f;
            addToBot(new WaitAction(delay));
        }
        
        // 播放Angel.ogg音乐
        playAngelBGM();
        
        // 处理弹药能力
        handleAmmunitionPower(p);

        // 修改光赐于苦的层数减少速度
        modifyLightUntoSufferersDecay(p);
    }

    private void handleAmmunitionPower(AbstractPlayer p) {
        AbstractPower ammoPower = p.getPower(AmmunitionPower.POWER_ID);
        if (ammoPower != null) {
            // 已有弹药能力，增加上限
            ((AmmunitionPower)ammoPower).setMax(((AmmunitionPower)ammoPower).currentMax + 1);
        } else {
            // 没有弹药能力，创建新的
            addToBot(new ApplyPowerAction(
                    p, p,
                    new AmmunitionPower(p, BASE_AMMO),
                    1));
        }
    }

    private void modifyLightUntoSufferersDecay(AbstractPlayer p) {
        // 直接添加修饰能力，不检查现有光赐于苦
        addToBot(new ApplyPowerAction(
                p, p,
                new FirnLightModifierPower(p),
                1));
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
}