package Andoain.actions;

import Andoain.powers.ShadowAndAshPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import java.util.ArrayList;

public class ShadowAndAshConsumeAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private int totalConsumed;
    private boolean firstFrame = true;
    private int limit = 0;

    public ShadowAndAshConsumeAction(AbstractPlayer player) {
        this.player = player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (firstFrame) {
            ArrayList<AbstractCard> toExhaust = new ArrayList<>();

            // 修改参数类型为CardGroup
            processCardGroup(player.drawPile, toExhaust);
            processCardGroup(player.discardPile, toExhaust);
            processCardGroup(player.hand, toExhaust);

            this.totalConsumed = toExhaust.size();

            // 执行消耗动画
            for (AbstractCard c : toExhaust) {
                addToTop(new WaitAction(0.1F));
                addToTop(new ExhaustSpecificCardAction(c, player.limbo));
                setupCardAnimation(c);
            }

            firstFrame = false;
            return;
        }

        // 仅修改效果应用部分
        if (this.duration == Settings.ACTION_DUR_FAST) { // 使用精确时间判断
            if (totalConsumed > 0) {
                // 强制获取Power实例
                ShadowAndAshPower power = (ShadowAndAshPower) player.getPower(ShadowAndAshPower.POWER_ID);
                if (power == null) {
                    power = new ShadowAndAshPower(player);
                    player.addPower(power);
                }

                // 计算实际增益（包括治疗）
                int remaining = power.getRemainingCapacity();
                int actualGain = Math.min(totalConsumed, remaining);

                if (actualGain > 0) {
                    // 治疗逻辑（受上限限制）- 改为每张1点
                    player.heal(actualGain);
                    
                    // 应用增益（使用即时生效方式）
                    player.addPower(new StrengthPower(player, actualGain));
                    player.addPower(new DexterityPower(player, actualGain));
                    power.totalGain += actualGain;
                    power.flash(); // 添加视觉反馈
                }

                // 清理持续时间
                this.isDone = true;
            }
        }
        this.tickDuration();
    }

    // 修改为接受CardGroup参数
    private void processCardGroup(CardGroup group, ArrayList<AbstractCard> targets) {
        // 使用副本遍历避免并发修改
        for (AbstractCard c : new ArrayList<>(group.group)) {
            if (c.type == AbstractCard.CardType.STATUS ||
                    c.type == AbstractCard.CardType.CURSE) {

                targets.add(c);
                group.removeCard(c); // 使用CardGroup的官方移除方法
                player.limbo.addToTop(c); // 加入临时区域
            }
        }
    }

    private void setupCardAnimation(AbstractCard c) {
        c.targetDrawScale = 0.5F;
        c.current_x = c.target_x = Settings.WIDTH / 2.0F;
        c.current_y = c.target_y = Settings.HEIGHT / 2.0F;
        c.angle = 0.0F;
    }
}