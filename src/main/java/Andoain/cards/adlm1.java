package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.badlogic.gdx.Gdx;

public class adlm1 extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("adlm1");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private float previewTimer = 0.0F;
    private static final float PREVIEW_INTERVAL = 3.5F;
    private boolean isPreviewing = false;
    private int previewIndex = 0;
    private AbstractCard[] previewCards;

    public adlm1() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("adlm1"),
                1,
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.RARE,
                CardTarget.SELF
        );
        this.exhaust = true;
        initializePreviewCards();
    }

    private void initializePreviewCards() {
        previewCards = new AbstractCard[]{
                new adlm2(),
                new adlm3(),
                new adlm4(),
                new Lemuen()
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查是否需要转阶段
        if (p instanceof Andoain.character.andoain && !((Andoain.character.andoain)p).isSecondStage()) {
            Andoain.character.andoain.onRevive();
            float delay = Settings.FAST_MODE ? 0.4f : 0.8f;
            addToBot(new WaitAction(delay));
        }
        
        // 将相知放入抽牌堆
        addToBot(new MakeTempCardInDrawPileAction(new adlm2(), 1, true, true));

        if (isGazing()) {
            // 注视状态:获得[E][E]并抽2张牌
            addToBot(new GainEnergyAction(2));
            addToBot(new DrawCardAction(2));
        } else {
            // 非注视状态:将蕾缪安的注视洗入手牌
            addToBot(new MakeTempCardInHandAction(new Lemuen(), 1));
        }
    }

    private boolean isGazing() {
        return AbstractDungeon.player.hand.group.stream()
                .anyMatch(c -> c.cardID.equals(Lemuen.ID) || c.cardID.equals(Sleep.ID));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.cost = 0;
            this.costForTurn = 0;
            this.isCostModified = true;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void update() {
        super.update();
        updatePreview();
    }

    private void updatePreview() {
        if (hb.hovered) {
            if (!isPreviewing) {
                isPreviewing = true;
                previewTimer = PREVIEW_INTERVAL;
                previewIndex = 0;
                cardsToPreview = previewCards[previewIndex];
            } else if (previewTimer <= 0.0F) {
                previewTimer = PREVIEW_INTERVAL;
                previewIndex = (previewIndex + 1) % previewCards.length;
                cardsToPreview = previewCards[previewIndex];
            } else {
                previewTimer -= Gdx.graphics.getDeltaTime();
            }
        } else if (isPreviewing) {
            isPreviewing = false;
            cardsToPreview = null;
        }
    }
}