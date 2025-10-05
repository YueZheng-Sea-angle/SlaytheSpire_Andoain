package Andoain.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;

import java.util.ArrayList;
import java.util.List;

public class WhatISeeAction extends AbstractGameAction {
    private int scryAmount;
    private List<AbstractCard> scryCards = new ArrayList<>();
    private List<AbstractCard> attackCards = new ArrayList<>();
    private boolean hasSelected = false;

    public WhatISeeAction(int scryAmount) {
        this.scryAmount = scryAmount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // 第一步：执行预见
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }

            performScry();
            this.tickDuration();
        } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && !hasSelected) {
            // 第三步：玩家选择了要打出的攻击牌
            hasSelected = true;
            AbstractCard selectedCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            // 从当前区域移除卡片（可能是抽牌堆或弃牌堆）
            removeCardFromGroup(selectedCard, AbstractDungeon.player.drawPile);
            removeCardFromGroup(selectedCard, AbstractDungeon.player.discardPile);
            removeCardFromGroup(selectedCard, AbstractDungeon.player.hand);

            // 将卡片添加到limbo区域
            AbstractDungeon.getCurrRoom().souls.remove(selectedCard);
            AbstractDungeon.player.limbo.addToTop(selectedCard);
            selectedCard.current_y = -200.0F * Settings.scale;
            selectedCard.target_x = Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
            selectedCard.target_y = Settings.HEIGHT / 2.0F;
            selectedCard.targetAngle = 0.0F;
            selectedCard.lighten(false);
            selectedCard.drawScale = 0.12F;
            selectedCard.targetDrawScale = 0.75F;

            // 设置为免费并消耗
            selectedCard.freeToPlayOnce = true;
            selectedCard.exhaustOnUseOnce = true;

            // 应用卡牌能力
            selectedCard.applyPowers();

            // 创建一个目标（如果没有目标，随机选择一个怪物）
            AbstractCreature target = null;
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            }

            // 添加动作队列：打牌 -> 移出limbo -> 等待
            addToTop(new NewQueueCardAction(selectedCard, target, false, true));
            addToTop(new UnlimboAction(selectedCard));

            if (!Settings.FAST_MODE) {
                addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            } else {
                addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
            }

            this.isDone = true;
        } else if (this.isDone) {
            // 第二步：在预见完成后，筛选攻击牌并让玩家选择
            if (!hasSelected && !attackCards.isEmpty()) {
                AbstractDungeon.gridSelectScreen.open((CardGroup) attackCards, 1, "选择一张攻击牌打出并消耗", false);
                this.isDone = false; // 保持动作活动直到选择完成
            }
        }

        this.tickDuration();
    }

    private void removeCardFromGroup(AbstractCard card, CardGroup group) {
        if (group.contains(card)) {
            group.removeCard(card);
        }
    }

    private void performScry() {
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        int cardsToScry = Math.min(scryAmount, AbstractDungeon.player.drawPile.size());

        for (int i = 0; i < cardsToScry; i++) {
            AbstractCard card = AbstractDungeon.player.drawPile.getNCardFromTop(i);
            scryCards.add(card);
            tmpGroup.addToTop(card);

            // 添加到攻击牌列表（如果是攻击牌）
            if (card.type == AbstractCard.CardType.ATTACK) {
                attackCards.add(card);
            }
        }

        // 显示预见结果
        for (AbstractCard card : tmpGroup.group) {
            if (AbstractDungeon.player.hand.size() < 10) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            } else {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(card, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, false));
            }
        }
    }
}