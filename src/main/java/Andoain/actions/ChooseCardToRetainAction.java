package Andoain.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.RetainCardPower;

import java.util.List;

public class ChooseCardToRetainAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Andoain:RetainOption");
    private final int retainAmount;

    public ChooseCardToRetainAction(int retainAmount) {
        this.retainAmount = retainAmount;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }

            // 正确的参数结构（基于StS 2.3版本API）
            AbstractDungeon.gridSelectScreen.open(
                    AbstractDungeon.player.hand,      // 卡牌来源
                    retainAmount,                     // 选择数量
                    uiStrings.TEXT[0] + retainAmount + uiStrings.TEXT[1], // 提示文本
                    false,                            // 不显示升级按钮
                    false,                            // 不转换卡牌
                    true,
                    true// 允许取消
            );
        }
        tickDuration();
    }

    private void applyRetain(List<AbstractCard> selectedCards) {
        selectedCards.forEach(card -> {
            addToTop(new ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    new RetainCardPower(AbstractDungeon.player, 1),
                    1
            ));
        });
    }
}