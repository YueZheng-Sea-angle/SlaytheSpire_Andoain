package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.LightUntoSufferers;
import Andoain.powers.NoLightPenaltyPower;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class LightAndShadow extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("LightAndShadow");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = 3;
    private static final int INTANGIBLE_AMOUNT = 1;
    private static final int LIGHT_AMOUNT = 3;

    // 预览控制变量
    private float previewTimer = 0.0F;
    private static final float PREVIEW_INTERVAL = 2.0F;
    private boolean isPreviewing = false;
    private boolean showingQuestion = true;

    public LightAndShadow() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("LightAndShadow"),
                COST,
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.RARE,
                CardTarget.SELF
        );
        this.exhaust = true;
        this.isEthereal = true;
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
        
        // 获得无实体和光赐于苦
        addToBot(new ApplyPowerAction(
                p, p,
                new IntangiblePlayerPower(p, INTANGIBLE_AMOUNT),
                INTANGIBLE_AMOUNT));

        addToBot(new ApplyPowerAction(
                p, p,
                new LightUntoSufferers(p, LIGHT_AMOUNT),
                LIGHT_AMOUNT));

        // 添加永久减益检查
        addToBot(new ApplyPowerAction(
                p, p,
                new NoLightPenaltyPower(p),
                1));

        // 洗入问题和答案
        addToBot(new MakeTempCardInHandAction(new Question(), 1));
        addToBot(new MakeTempCardInDrawPileAction(new Answer(), 1, true, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isEthereal = false; // 升级后移除虚无特性
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void update() {
        super.update();

        // 预览逻辑
        if (this.hb != null && this.hb.hovered) {
            if (!isPreviewing) {
                isPreviewing = true;
                previewTimer = PREVIEW_INTERVAL;
                this.cardsToPreview = getPreviewCard();
            } else if (previewTimer <= 0.0F) {
                previewTimer = PREVIEW_INTERVAL;
                showingQuestion = !showingQuestion;
                this.cardsToPreview = getPreviewCard();
            } else {
                previewTimer -= Gdx.graphics.getDeltaTime();
            }
        } else if (isPreviewing) {
            isPreviewing = false;
            this.cardsToPreview = null;
        }
    }

    private AbstractCard getPreviewCard() {
        if (AbstractDungeon.player == null) {
            // 战斗外预览时交替显示问题和答案
            return showingQuestion ? new Question() : new Answer();
        }
        return showingQuestion ? new Question() : new Answer();
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