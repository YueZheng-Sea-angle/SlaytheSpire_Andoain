package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class MyLateran_E extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("MyLateran_E");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 0;
    private static final int UPGRADED_COST = 0;
    private static final int LIGHT_STACKS = 2;

    public MyLateran_E() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("MyLateran"),
                COST,
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColor.COLORLESS,
                CardRarity.SPECIAL,
                CardTarget.ALL_ENEMY
        );
        this.exhaust = true; // 消耗效果
        this.isEthereal = true;
        this.baseMagicNumber = LIGHT_STACKS;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST); // 升级后费用减少1
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得2层光赐于苦
        addToBot(new ApplyPowerAction(
                p, p,
                new LightUntoSufferers(p, magicNumber),
                magicNumber));

        // 令所有敌方单位的易伤、缓慢层数变为三倍
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                if (monster.hasPower(VulnerablePower.POWER_ID)) {
                    int vulnerableAmt = monster.getPower(VulnerablePower.POWER_ID).amount;
                    monster.getPower(VulnerablePower.POWER_ID).amount = vulnerableAmt * 3;
                    monster.getPower(VulnerablePower.POWER_ID).updateDescription();
                }
                if (monster.hasPower(SlowPower.POWER_ID)) {
                    int slowAmt = monster.getPower(SlowPower.POWER_ID).amount;
                    monster.getPower(SlowPower.POWER_ID).amount = slowAmt * 3;
                    monster.getPower(SlowPower.POWER_ID).updateDescription();
                }
            }
        }

        // 检查手牌中是否有状态牌
        boolean hasStatusCard = false;
        for (AbstractCard card : p.hand.group) {
            if (card.type == CardType.STATUS) {
                hasStatusCard = true;
                addToBot(new com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction(card, p.hand));
            }
        }

        // 如果有状态牌则获得1能量
        if (hasStatusCard) {
            addToBot(new GainEnergyAction(1));
        }

        // 添加等待动作保持动画流畅
        float delay = Settings.FAST_MODE ? 0.4f : 0.8f;
        addToBot(new WaitAction(delay));
    }
}