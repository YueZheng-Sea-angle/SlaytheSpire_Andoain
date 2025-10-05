package Andoain.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.ArrayList;

public class ConsumeCursesInHandAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public ConsumeCursesInHandAction(AbstractPlayer player) {
        this.player = player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // 获取手牌中所有诅咒牌
            ArrayList<AbstractCard> curses = new ArrayList<>();
            for (AbstractCard c : player.hand.group) {
                if (c.color == AbstractCard.CardColor.CURSE) {
                    curses.add(c);
                }
            }

            // 消耗所有诅咒牌
            for (AbstractCard curse : curses) {
                addToTop(new ExhaustSpecificCardAction(curse, player.hand));
            }

            this.isDone = true;
        }
        this.tickDuration();
    }
}