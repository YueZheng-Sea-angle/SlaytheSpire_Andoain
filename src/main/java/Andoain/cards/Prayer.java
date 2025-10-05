package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.Collections;

public class Prayer extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Prayer");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private enum RewardType {
        ENERGY, AMMO, DRAW
    }

    public Prayer() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("Prayer"),
                0,
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.COMMON,
                CardTarget.SELF
        );
        this.baseBlock = 2;
        this.baseMagicNumber = 1;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeMagicNumber(1); //
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 消耗所有状态牌和诅咒牌
        consumeStatusAndCurseCards(p);

        // 获得格挡
        addToBot(new GainBlockAction(p, p, this.block));

        // 随机奖励
        if (upgraded) {
            giveRandomRewards(p, 2); // 升级后2个奖励
        } else {
            giveRandomRewards(p, 1); // 基础1个奖励
        }
    }

    private void consumeStatusAndCurseCards(AbstractPlayer p) {
        ArrayList<AbstractCard> cardsToExhaust = new ArrayList<>();

        for (AbstractCard c : p.hand.group) {
            if (c.type == CardType.STATUS || c.color == CardColor.CURSE) {
                cardsToExhaust.add(c);
            }
        }

        for (AbstractCard c : cardsToExhaust) {
            addToBot(new ExhaustSpecificCardAction(c, p.hand));
        }
    }

    private void giveRandomRewards(AbstractPlayer p, int count) {
        ArrayList<RewardType> rewards = new ArrayList<>();
        Collections.addAll(rewards, RewardType.values());
        Collections.shuffle(rewards, AbstractDungeon.cardRandomRng.random);

        for (int i = 0; i < Math.min(count, rewards.size()); i++) {
            switch (rewards.get(i)) {
                case ENERGY:
                    addToBot(new GainEnergyAction(1));
                    break;
                case AMMO:
                    if (p.hasPower(AmmunitionPower.POWER_ID)) {
                        ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).replenish(1);
                    } else {
                        addToBot(new ApplyPowerAction(p, p, new AmmunitionPower(p, 3,1)));
                    }
                    break;
                case DRAW:
                    addToBot(new DrawCardAction(p, 1));
                    break;
            }
        }
    }
}