package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.badlogic.gdx.Gdx;

public class adlm3 extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("adlm3");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private float previewTimer = 0.0F;
    private static final float PREVIEW_INTERVAL = 3.0F;
    private boolean isPreviewing = false;
    private int previewIndex = 0;
    private AbstractCard[] previewCards;

    public adlm3() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("adlm3"),
                0,
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColor.COLORLESS,
                CardRarity.SPECIAL,
                CardTarget.SELF
        );
        this.exhaust = true;
        initializePreviewCards();
    }

    private void initializePreviewCards() {
        previewCards = new AbstractCard[]{
                new adlm4(),
                new Pathfinders(),
                new FindPath(),
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 将告别放入牌堆顶
        addToBot(new MakeTempCardInDrawPileAction(new adlm4(), 1, true, true));

        if (isGazing()) {
            // 注视状态:将0费寻路者和寻路洗入手牌
            AbstractCard pathfinder = new Pathfinders();
            pathfinder.freeToPlayOnce = true;
            pathfinder.setCostForTurn(0);

            AbstractCard findPath = new FindPath();
            findPath.freeToPlayOnce = true;
            findPath.setCostForTurn(0);

            addToBot(new MakeTempCardInHandAction(pathfinder, 1));
            addToBot(new MakeTempCardInHandAction(findPath, 1));
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