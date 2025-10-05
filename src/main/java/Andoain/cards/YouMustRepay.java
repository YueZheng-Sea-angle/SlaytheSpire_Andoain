package Andoain.cards;

import Andoain.helpers.ModHelper;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class YouMustRepay extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("YouMustRepay");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("YouMustRepay");
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final int HP_LOSS = 4;

    public YouMustRepay() {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION,
                CardType.STATUS, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.NONE);
        this.baseMagicNumber = HP_LOSS;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
            // 先播放卡牌飞到中央的动画
            AbstractDungeon.actionManager.addToTop(new VFXAction(
                    new CardPoofEffect(this.current_x, this.current_y), 0.1f));

            // 然后播放一个小爆炸效果
            AbstractDungeon.actionManager.addToTop(new VFXAction(
                    new ExplosionSmallEffect(this.current_x, this.current_y), 0.1f));

            // 最后造成伤害
            AbstractDungeon.actionManager.addToTop(
                    new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber,
                            AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public void upgrade() {
        // 状态牌通常不可升级
    }
}