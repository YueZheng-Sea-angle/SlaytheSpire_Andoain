package Andoain.actions;


import Andoain.cards.Question;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import java.util.ArrayList;

public class SelectCardsToDoubtAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final boolean canCancel;
    private ArrayList<AbstractCard> selectedCards = new ArrayList<>();

    public SelectCardsToDoubtAction(AbstractPlayer player, boolean canCancel) {
        this.player = player;
        this.canCancel = canCancel;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // 创建选择界面
            AbstractDungeon.handCardSelectScreen.open(
                    "选择要变为疑虑的牌",
                    Integer.MAX_VALUE,
                    canCancel,
                    true,
                    false,
                    false,
                    true
            );

            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            // 获取选择的卡牌
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (card.color != AbstractCard.CardColor.CURSE) { // 只处理非诅咒牌
                    selectedCards.add(card);
                    addToBot(new MakeTempCardInHandAction(new com.megacrit.cardcrawl.cards.curses.Doubt(), 1));

                    // 播放正确的动画效果
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
                }
            }

            // 移除选择的原卡牌
            for (AbstractCard card : selectedCards) {
                player.hand.removeCard(card);
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();

            ArrayList<AbstractCard> curses = new ArrayList<>();

            for (AbstractCard c : player.hand.group) {
                if (c.color == AbstractCard.CardColor.CURSE || c.type == AbstractCard.CardType.STATUS) {
                    curses.add(c);
                }
            }

        }

        this.tickDuration();
    }
}