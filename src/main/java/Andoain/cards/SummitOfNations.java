package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.SummitOfNationsPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SummitOfNations extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("SummitOfNations");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = 2;

    public SummitOfNations() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("SummitOfNations"),
                COST,
                CARD_STRINGS.DESCRIPTION,
                CardType.POWER,
                CardColorEnum.Andoain_Blue,
                CardRarity.UNCOMMON,
                CardTarget.SELF
        );
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查是否已有相同类型的能力
        SummitOfNationsPower existingPower = null;
        for (AbstractPower power : p.powers) {
            if (power instanceof SummitOfNationsPower) {
                SummitOfNationsPower snPower = (SummitOfNationsPower) power;
                if (snPower.isUpgraded == this.upgraded) {
                    existingPower = snPower;
                    break;
                }
            }
        }

        if (existingPower != null) {
            // 如果已有相同类型的能力，增加层数
            existingPower.stackPower(1);
        } else {
            // 否则添加新能力
            addToBot(new ApplyPowerAction(
                    p, p,
                    new SummitOfNationsPower(p, 1, this.upgraded),
                    1
            ));
        }
    }
}