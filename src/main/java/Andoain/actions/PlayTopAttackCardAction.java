package Andoain.actions;

import Andoain.monster.IceCreamMachine;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class PlayTopAttackCardAction extends AbstractGameAction {

    public PlayTopAttackCardAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.drawPile.isEmpty()) {
            this.isDone = true;
            return;
        }

        AbstractCard topCard = AbstractDungeon.player.drawPile.getTopCard();

        // 检查牌堆顶是否为攻击牌
        if (topCard.type == AbstractCard.CardType.ATTACK) {
            // 获取随机敌人目标（排除友方冰淇淋机）
            AbstractMonster target = getRandomEnemyTarget();

            // 打出牌堆顶牌并消耗
            addToTop(new PlayTopCardAction(target, true));
        }

        this.isDone = true;
    }

    private AbstractMonster getRandomEnemyTarget() {
        List<AbstractMonster> enemies = new ArrayList<>();

        // 收集所有非友方冰淇淋机的敌人
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && !(m instanceof IceCreamMachine && ((IceCreamMachine)m).isAlly())) {
                enemies.add(m);
            }
        }

        // 如果有可用的敌人，随机选择一个
        if (!enemies.isEmpty()) {
            return enemies.get(AbstractDungeon.cardRandomRng.random(enemies.size() - 1));
        }

        return null; // 没有可用的目标
    }
}