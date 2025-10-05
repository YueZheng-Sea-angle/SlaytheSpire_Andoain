package Andoain.cards;

import Andoain.actions.ChooseSquadCardAction;
import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.LightUntoSufferers;
import Andoain.powers.SquadCooldownPower;
import Andoain.powers.SquadGenerationPower;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;

public class OnceLivedForever extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("OnceLivedForever");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final ArrayList<AbstractCard> SQUAD_CARDS = new ArrayList<AbstractCard>() {{
        add(new Lemuen_E());
        add(new Mostima_E());
        add(new Fiammetta_E());
        add(new MyLateran_E());
    }};

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("OnceLivedForever");
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.POWER;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    // 预览控制变量
    private float previewTimer = 0.0F;
    private static final float PREVIEW_INTERVAL = 2.0F;
    private boolean isPreviewing = false;

    public OnceLivedForever() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
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
        // 检查是否需要转阶段
        if (p instanceof Andoain.character.andoain && !((Andoain.character.andoain)p).isSecondStage()) {
            Andoain.character.andoain.onRevive();
            float delay = Settings.FAST_MODE ? 0.4f : 0.8f;
            addToBot(new WaitAction(delay));
        }
        
        // 播放Angel.ogg音乐
        playAngelBGM();

        // 添加光赐于苦效果
        addToBot(new ApplyPowerAction(
                p, p,
                new LightUntoSufferers(p, this.magicNumber),
                this.magicNumber
        ));

        // 检查现有能力
        AbstractPower existing = p.getPower(SquadGenerationPower.POWER_ID);

        // 升级卡牌逻辑：总是移除现有能力并添加升级版
        if (this.upgraded) {
            if (existing != null) {
                addToBot(new RemoveSpecificPowerAction(p, p, existing));
            }
            addToBot(new ApplyPowerAction(p, p, new SquadGenerationPower(p, true)));


        }
        // 未升级卡牌逻辑：仅当没有升级版时添加
        else {
            // 如果已存在升级版，则不添加新能力
            if (existing instanceof SquadGenerationPower && ((SquadGenerationPower) existing).upgraded) {
                return;
            }

            // 如果已存在未升级版，移除后再添加（确保只保留一个）
            if (existing != null) {
                addToBot(new RemoveSpecificPowerAction(p, p, existing));
            }

            addToBot(new ApplyPowerAction(p, p, new SquadGenerationPower(p, false)));

        }
    }

    public static AbstractCard generateSquadCard(boolean upgraded) {
        if (AbstractDungeon.player == null) {
            return getPreviewCard();
        }

        ArrayList<AbstractCard> availableCards = new ArrayList<>();
        for (AbstractCard c : SQUAD_CARDS) {
            if (!AbstractDungeon.player.hasPower(SquadCooldownPower.POWER_ID + "_" + c.cardID)) {
                availableCards.add(c);
            }
        }

        AbstractCard selected = availableCards.isEmpty() ?
                SQUAD_CARDS.get(AbstractDungeon.cardRandomRng.random(SQUAD_CARDS.size() - 1)) :
                availableCards.get(AbstractDungeon.cardRandomRng.random(availableCards.size() - 1));

        // 使用makeCopy()而不是makeStatEquivalentCopy()
        AbstractCard modifiedCard = selected.makeCopy();

        // 强制修改属性
        modifiedCard.cost = 0;
        modifiedCard.costForTurn = 0;
        modifiedCard.exhaust = true;
        modifiedCard.isEthereal = true;

        // 添加描述提示
        modifiedCard.rawDescription = modifiedCard.rawDescription;
        modifiedCard.initializeDescription();

        if (!availableCards.isEmpty()) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    new SquadCooldownPower(AbstractDungeon.player, selected.cardID, 4),
                    4
            ));
        }

        return modifiedCard;
    }
    // 安全获取预览牌
    private static AbstractCard getPreviewCard() {
        AbstractCard card = SQUAD_CARDS.get(
                (int)((System.currentTimeMillis() / 2000) % SQUAD_CARDS.size())
        ).makeCopy();  // 使用makeCopy()

        // 设置预览卡牌的属性
        card.cost = 0;
        card.costForTurn = 0;
        card.exhaust = true;
        card.isEthereal = true;
        card.initializeDescription();

        return card;
    }

    @Override
    public void update() {
        super.update();

        if (this.hb != null && this.hb.hovered) {
            if (!isPreviewing) {
                isPreviewing = true;
                previewTimer = PREVIEW_INTERVAL;
                this.cardsToPreview = getPreviewCard();
            } else if (previewTimer <= 0.0F) {
                previewTimer = PREVIEW_INTERVAL;
                this.cardsToPreview = getPreviewCard();
            } else {
                previewTimer -= Gdx.graphics.getDeltaTime();
            }
        } else if (isPreviewing) {
            isPreviewing = false;
            this.cardsToPreview = null;
        }
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