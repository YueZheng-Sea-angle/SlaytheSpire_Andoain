package Andoain.powers;

import Andoain.actions.ChooseSquadCardAction;
import Andoain.cards.OnceLivedForever;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class SquadGenerationPower extends AbstractPower {
    public static final String POWER_ID = "SquadGeneration";
    public final boolean upgraded; // 改为public以便访问

    public SquadGenerationPower(AbstractCreature owner, boolean upgraded) {
        this.ID = POWER_ID;
        this.name = "也曾永远";
        this.owner = owner;
        this.upgraded = upgraded;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        updateDescription();
        loadRegion("time");
    }

    @Override
    public void updateDescription() {
        this.description = upgraded ?
                "每回合开始时，选择获得一张0费，虚无，消耗的小队牌。" :
                "每回合开始时，随机获得一张0费，虚无，消耗的小队牌。";
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ChooseSquadCardAction());
        } else {
            AbstractCard card = OnceLivedForever.generateSquadCard(false);
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, true));
        }
    }

    // 禁止能力叠加
    @Override
    public void stackPower(int stackAmount) {}
}