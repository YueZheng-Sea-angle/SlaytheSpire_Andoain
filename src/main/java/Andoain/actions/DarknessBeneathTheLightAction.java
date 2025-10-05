package Andoain.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DarknessBeneathTheLightAction extends AbstractGameAction {
    private final boolean upgraded;

    public DarknessBeneathTheLightAction(AbstractPlayer player,boolean upgraded) {
        this.source = player;
        this.upgraded = upgraded;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractPlayer p = (AbstractPlayer) source;

            // 遍历手牌中的所有卡牌
            for (AbstractCard card : p.hand.group) {
                // 只处理攻击牌和技能牌
                if (card.type == AbstractCard.CardType.ATTACK || card.type == AbstractCard.CardType.SKILL) {
                    // 设置为状态牌
                    card.type = AbstractCard.CardType.STATUS;

                    // 升级卡牌：整个战斗耗能为0
                    if (upgraded) {
                        card.cost = 0;
                        card.costForTurn = 0;
                        card.isCostModified = true;
                    }
                    // 未升级卡牌：仅本回合耗能为0
                    else {
                        card.freeToPlayOnce = true;
                    }
                }
            }
        }
        tickDuration();
        this.isDone = true;
    }
}