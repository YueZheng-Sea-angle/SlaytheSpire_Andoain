package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AngelPower;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Angel extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Angel");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = 2;


    public Angel() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("Angel"),
                COST,
                CARD_STRINGS.DESCRIPTION,
                CardType.POWER,
                CardColorEnum.Andoain_Blue,
                CardRarity.RARE,
                CardTarget.SELF
        );
        this.baseMagicNumber = 1;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
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
        
        // 检查当前BGM，如果不是Angel.ogg则播放Angel.ogg
        checkAndPlayAngleBGM();
        
        addToBot(new ApplyPowerAction(
                p, p,
                new AngelPower(p, this.upgraded),
                1
        ));
    }
    
    private void checkAndPlayAngleBGM() {
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
                // System.out.println("检测到特殊战斗场景，立即停止当前音乐并切换为Angel.ogg");
            } else if (Andoain.helpers.ModHelper.isFightingLagavulin()) {
                // 乐加维林战斗时，需要同时淡出临时音乐（ELITE）和层数音乐
                CardCrawlGame.music.fadeOutTempBGM();
                CardCrawlGame.music.fadeOutBGM();
                // System.out.println("检测到乐加维林战斗，淡出ELITE音乐和层数音乐并切换为Angel.ogg");
            } else {
                // 普通战斗时，淡出层数音乐
                CardCrawlGame.music.fadeOutBGM();
                // System.out.println("普通战斗，淡出当前音乐并切换为Angel.ogg");
            }
            // 播放自定义音乐
            CardCrawlGame.music.playTempBgmInstantly("Angel.ogg", true);
        } catch (Exception e) {
            // 如果出现异常，记录日志但不影响游戏进行
            System.out.println("播放Angel.ogg时出现异常: " + e.getMessage());
        }
    }
}