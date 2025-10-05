package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.ThriftyHabitsPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ThriftyHabits extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("ThriftyHabits");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMG_PATH = ModHelper.getCardImagePath("ThriftyHabits");
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION;

    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.POWER;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    private static final int COST = 1;
    private static final int PROBABILITY = 50;

    public ThriftyHabits() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = PROBABILITY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ThriftyHabitsPower(p, magicNumber), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}