package Andoain.actions;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;

public class ShuffleOtherChoicesAction extends com.megacrit.cardcrawl.actions.AbstractGameAction {
    private final ArrayList<AbstractCard> choices;

    public ShuffleOtherChoicesAction(ArrayList<AbstractCard> allChoices) {
        this.choices = allChoices;
    }

    @Override
    public void update() {
        // 获取实际选择的卡片（直接从RewardScreen获取）
        AbstractCard selected = AbstractDungeon.cardRewardScreen.discoveryCard;

        // 设置选择的卡片为0费（需检查是否存在于手牌中）
        if (selected != null) {
            // 遍历手牌查找实际选择的卡片（解决对象引用问题）
            for (AbstractCard handCard : AbstractDungeon.player.hand.group) {
                if (handCard.cardID.equals(selected.cardID) && handCard.upgraded == selected.upgraded) {
                    handCard.freeToPlayOnce = true;
                    handCard.isCostModifiedForTurn = true;
                    break;
                }
            }

            // 添加视觉反馈
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(selected.makeStatEquivalentCopy()));
        }

        // 洗入未选择的卡片
        for (AbstractCard card : choices) {
            // 跳过实际选择的卡片
            if (selected != null &&
                    card.cardID.equals(selected.cardID) &&
                    card.upgraded == selected.upgraded) {
                continue;
            }

            AbstractCard copy = card.makeStatEquivalentCopy();
            if (card.upgraded) copy.upgrade();
            addToTop(new MakeTempCardInDrawPileAction(copy, 1, true, true, false));
        }

        this.isDone = true;
    }
}