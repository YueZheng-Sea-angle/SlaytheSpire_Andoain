package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

public class adlm4 extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("adlm4");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int BASE_DAMAGE = 45;
    private static final int UPGRADED_DAMAGE = 60;

    private float previewTimer = 0.0F;
    private static final float PREVIEW_INTERVAL = 3.0F;
    private boolean isPreviewing = false;
    private int previewIndex = 0;
    private AbstractCard[] previewCards;

    public adlm4() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("adlm4"),
                0,
                CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK,
                CardColor.COLORLESS,
                CardRarity.SPECIAL,
                CardTarget.ENEMY
        );
        this.baseDamage = BASE_DAMAGE;
        this.exhaust = true;
        initializePreviewCards();
    }

    private void initializePreviewCards() {
        previewCards = new AbstractCard[]{
                new Lemuen()
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 消耗所有区域的蕾缪安的注视并造成伤害
        int totalCards = consumeLemuenCards();

        if (totalCards > 0) {
            // 造成伤害
            addToBot(new DamageAction(
                    m,
                    new DamageInfo(p, this.damage * totalCards, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    private int consumeLemuenCards() {
        int count = 0;

        // 消耗手牌中的Lemuen卡
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.cardID.equals(Lemuen.ID)) {
                addToBot(new VFXAction(new PurgeCardEffect(c)));
                addToBot(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
                count++;
            }
        }

        // 消耗抽牌堆中的Lemuen卡
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.cardID.equals(Lemuen.ID)) {
                addToBot(new VFXAction(new PurgeCardEffect(c)));
                addToBot(new ExhaustSpecificCardAction(c, AbstractDungeon.player.drawPile));
                count++;
            }
        }

        // 消耗弃牌堆中的Lemuen卡
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c.cardID.equals(Lemuen.ID)) {
                addToBot(new VFXAction(new PurgeCardEffect(c)));
                addToBot(new ExhaustSpecificCardAction(c, AbstractDungeon.player.discardPile));
                count++;
            }
        }

        return count;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADED_DAMAGE - BASE_DAMAGE);
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