package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.*;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.List;

public class Patia extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Patia");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public boolean isPathfinders = true;
    public Patia() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("Patia"),
                0, // 0费
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.UNCOMMON,
                CardTarget.SELF
        );
        this.exhaust = true; // 消耗属性
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后可以保留在手牌中
            this.selfRetain = true;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 步骤1: 消耗手牌中的所有状态牌
        consumeStatusCards(p);

        // 步骤2: 为不足1的数值进行补充
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                // 1. 补充光赐于苦层数
                provideLightUntoSufferers(p);

                // 2. 补充弹药数
                provideAmmunition(p);

                // 3. 补充能量值
                provideEnergy(p);

                // 4. 补充手牌数
                provideCardDraw(p);

                // 5. 补充药水数
                providePotions(p);

                if (p.hasPower(LightUntoSufferers.POWER_ID) &&
                        p.hasPower(GuidingAheadPower.POWER_ID)) {
                    p.gainEnergy(1);
                    p.getPower(GuidingAheadPower.POWER_ID).flash();
                }
                this.isDone = true;
            }
        });
    }

    /**
     * 消耗手牌中的所有状态牌
     */
    private void consumeStatusCards(AbstractPlayer p) {
        List<AbstractCard> statusCards = new ArrayList<>();

        // 收集所有状态牌
        for (AbstractCard card : p.hand.group) {
            if (isStatusCard(card)) {
                statusCards.add(card);
            }
        }

        // 消耗所有状态牌
        for (AbstractCard card : statusCards) {
            // 使用DiscardToHandAction确保卡牌正确进入消耗牌堆
            addToBot(new DiscardToHandAction(card));
            addToBot(new ExhaustSpecificCardAction(card, p.hand));
        }
    }

    /**
     * 判断卡牌是否是状态牌
     */
    private boolean isStatusCard(AbstractCard card) {
        // 基础游戏的状态牌类型
        if (card.type == AbstractCard.CardType.STATUS) return true;

        // 特殊状态卡
        return card instanceof Burn
                || card instanceof Dazed
                || card instanceof Slimed
                || card instanceof VoidCard
                || card instanceof Wound;
    }

    /**
     * 补充光赐于苦层数
     */
    private void provideLightUntoSufferers(AbstractPlayer p) {
        LightUntoSufferers power = (LightUntoSufferers) p.getPower(LightUntoSufferers.POWER_ID);

        if (power == null) {
            // 如果玩家没有光赐于苦，添加1层
            addToBot(new ApplyPowerAction(
                    p, p,
                    new LightUntoSufferers(p, 1),
                    1
            ));
        } else if (power.amount < 1) {
            // 如果层数小于1，增加到1层
            addToBot(new ReducePowerAction(p, p, power, power.amount));
            addToBot(new ApplyPowerAction(
                    p, p,
                    new LightUntoSufferers(p, 1),
                    1
            ));
        }
    }

    /**
     * 补充弹药数
     */
    private void provideAmmunition(AbstractPlayer p) {
        AmmunitionPower power = (AmmunitionPower) p.getPower(AmmunitionPower.POWER_ID);

        if (power == null) {
            // 如果玩家没有弹药能力，添加弹药能力并设置弹药量为1
            addToBot(new ApplyPowerAction(
                    p, p,
                    new AmmunitionPower(p, 1),
                    1
            ));
        } else if (power.amount < 1) {
            // 如果弹药量小于1，补充到1
            power.amount = 1;
            power.updateDescription();
            p.hand.refreshHandLayout(); // 刷新手牌显示
        }
    }

    /**
     * 补充能量值
     */
    private void provideEnergy(AbstractPlayer p) {
        int currentEnergy = EnergyPanel.totalCount;
        if (currentEnergy < 1) {
            // 添加足够使能量达到1的能量
            addToBot(new GainEnergyAction(1));
        }
    }

    /**
     * 补充手牌数
     */
    private void provideCardDraw(AbstractPlayer p) {
        if (p.hand.size() < 1) {
            int cardsNeeded = 1 - p.hand.size();
            addToBot(new DrawCardAction(cardsNeeded));
        }
    }

    /**
     * 补充药水数
     */
    private void providePotions(AbstractPlayer p) {
        if (!p.hasAnyPotions()) {
            this.addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
        }
    }
}