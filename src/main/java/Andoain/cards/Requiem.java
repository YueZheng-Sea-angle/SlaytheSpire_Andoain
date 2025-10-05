package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.RequiemPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Requiem extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Requiem");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = 2;
    private static final int BLOCK_PER_CARD = 4;
    private static final int DREAM_PER_CARD = 1;
    private static final int UPGRADE_BLOCK = 1;

    public Requiem() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("Requiem"),
                COST,
                CARD_STRINGS.DESCRIPTION,
                CardType.POWER,
                CardColorEnum.Andoain_Blue,
                CardRarity.UNCOMMON,
                CardTarget.SELF
        );
        this.magicNumber = this.baseMagicNumber = BLOCK_PER_CARD;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_BLOCK);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(
                p, p,
                new RequiemPower(p, this.magicNumber, 1, this.upgraded),
                1
        ));
    }
}