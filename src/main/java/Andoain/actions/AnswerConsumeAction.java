package Andoain.actions;

import Andoain.powers.AmmunitionPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.ArrayList;

public class AnswerConsumeAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster monster;
    private final ArrayList<AbstractCard> cardsToConsume = new ArrayList<>();
    private int totalCardsConsumed;
    private boolean initiated = false;
    private boolean cardsExhausted = false;

    public AnswerConsumeAction(AbstractPlayer player, AbstractMonster monster) {
        this.player = player;
        this.monster = monster;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (!initiated) {
            // 收集所有需要消耗的牌
            collectCardsToConsume(player.hand);
            collectCardsToConsume(player.drawPile);
            collectCardsToConsume(player.discardPile);

            totalCardsConsumed = cardsToConsume.size();

            if (!cardsToConsume.isEmpty()) {
                // 移动到limbo区域
                moveCardsToLimbo();
                // 开始消耗过程
                initiateCardConsumption();
            } else {
                // 如果没有卡牌消耗，直接处理弹药和伤害
                cardsExhausted = true;
            }

            initiated = true;
            return;
        }

        // 检查消耗是否完成
        if (!cardsExhausted && player.limbo.isEmpty()) {
            cardsExhausted = true;
        }

        // 消耗完成后处理弹药和伤害
        if (cardsExhausted) {
            handleAmmoAndDamage();
            this.isDone = true;
            return;
        }

        // 等待消耗完成
        this.tickDuration();
    }

    private void collectCardsToConsume(CardGroup group) {
        // 创建副本避免并发修改
        ArrayList<AbstractCard> cardsCopy = new ArrayList<>(group.group);
        for (AbstractCard c : cardsCopy) {
            if (c.type == AbstractCard.CardType.STATUS || c.color == AbstractCard.CardColor.CURSE) {
                cardsToConsume.add(c);
            }
        }
    }

    private void moveCardsToLimbo() {
        for (AbstractCard c : cardsToConsume) {
            // 从原位置移除
            if (player.hand.contains(c)) {
                player.hand.removeCard(c);
            } else if (player.drawPile.contains(c)) {
                player.drawPile.removeCard(c);
            } else if (player.discardPile.contains(c)) {
                player.discardPile.removeCard(c);
            }

            // 添加到limbo区域
            player.limbo.addToTop(c);
            c.stopGlowing();
            c.targetDrawScale = 0.75F;
            c.current_x = c.target_x = Settings.WIDTH / 2.0F;
            c.current_y = c.target_y = Settings.HEIGHT / 2.0F;
        }
    }

    private void initiateCardConsumption() {
        // 添加消耗动作
        for (AbstractCard c : cardsToConsume) {
            addToTop(new ExhaustSpecificCardAction(c, player.limbo));
        }

        // 设置更长的持续时间等待消耗完成
        this.duration = Settings.ACTION_DUR_XLONG;
    }

    private void handleAmmoAndDamage() {
        // 获取弹药数量
        AbstractPower ammoPower = player.getPower(AmmunitionPower.POWER_ID);
        int ammoSpent = ammoPower != null ? ((AmmunitionPower)ammoPower).getammo() : 0;

        // 消耗所有弹药
        if (ammoPower != null) {
            ((AmmunitionPower)ammoPower).spend(ammoPower.amount, true);
        }

        // 计算并造成伤害
        if (ammoSpent > 0 && monster != null && !monster.isDeadOrEscaped()) {
            int totalDamage = totalCardsConsumed * 4 * ammoSpent;

            // 确保伤害至少为1
            if (totalDamage < 1) totalDamage = 1;

            // 添加伤害动作
            addToTop(new DamageAction(
                    monster,
                    new DamageInfo(player, totalDamage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.FIRE
            ));
        }
    }
}