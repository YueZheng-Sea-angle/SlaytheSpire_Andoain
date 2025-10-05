package Andoain.cards;

import Andoain.actions.ConsumeCursesInHandAction;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;

public class Question extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Question");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Question() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("Question"),
                0,
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColor.COLORLESS,
                CardRarity.SPECIAL,
                CardTarget.NONE
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 将玩家选择的任意张手牌替换为疑虑
        addToBot(new TransformCardsAction(p));

        // 消耗手牌中所有诅咒牌
        addToBot(new ConsumeCursesInHandAction(p));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    private static class TransformCardsAction extends com.megacrit.cardcrawl.actions.utility.WaitAction {
        private final AbstractPlayer player;

        public TransformCardsAction(AbstractPlayer player) {
            super(0.1f);
            this.player = player;
        }

        @Override
        public void update() {
            super.update();
            if (this.isDone) {
                // 遍历手牌，将非诅咒牌替换为疑虑
                for (AbstractCard c : new ArrayList<>(player.hand.group)) {
                    if (c.color != CardColor.CURSE) {
                        player.hand.addToTop(new Doubt()); // 添加疑虑牌
                        player.hand.removeCard(c); // 移除原牌
                        AbstractDungeon.effectList.add(new PurgeCardEffect(c)); // 播放净化效果
                    }
                }
            }
        }
    }
}