package Andoain.actions;

import Andoain.cards.SeekingTruth;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SeekingTruthDamageDownAction extends AbstractGameAction {
    private AbstractCard card;
    private int amount;

    public SeekingTruthDamageDownAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
    }

    public void update() {
        if (card != null) {
            card.baseDamage -= amount;
            if (card.baseDamage < 0) card.baseDamage = 0;
            card.applyPowers();
        }
        // 减少所有区域中同名卡牌的伤害
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof SeekingTruth) {
                c.baseDamage -= amount;
                if (c.baseDamage < 0) c.baseDamage = 0;
                c.applyPowers();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof SeekingTruth) {
                c.baseDamage -= amount;
                if (c.baseDamage < 0) c.baseDamage = 0;
                c.applyPowers();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof SeekingTruth) {
                c.baseDamage -= amount;
                if (c.baseDamage < 0) c.baseDamage = 0;
                c.applyPowers();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c instanceof SeekingTruth) {
                c.baseDamage -= amount;
                if (c.baseDamage < 0) c.baseDamage = 0;
                c.applyPowers();
            }
        }

        this.isDone = true;
    }
}