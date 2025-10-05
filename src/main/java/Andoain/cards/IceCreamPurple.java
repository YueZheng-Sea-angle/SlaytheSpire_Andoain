package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import Andoain.patches.CardColorEnum;
import Andoain.powers.LightUntoSufferers;
import Andoain.powers.PurpleCreamPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class IceCreamPurple extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("IceCreamPurple");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("IceCreamPurple");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.POWER;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    public IceCreamPurple() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.isInnate = true; // 升级后变为固有
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 应用香芋冰淇淋能力
        addToBot(new ApplyPowerAction(
                p, p,
                new PurpleCreamPower(p),
                0
        ));
    }

    @Override
    public AbstractCard makeCopy() {
        return new IceCreamPurple();
    }

    // 检查是否存在友方冰淇淋机
    public static boolean hasFriendlyIceCreamMachine() {
        return AbstractDungeon.getMonsters().monsters.stream()
                .filter(mon -> mon instanceof IceCreamMachine && !mon.isDeadOrEscaped())
                .anyMatch(machine -> ((IceCreamMachine) machine).isAlly());
    }
    public static boolean hasEnemyIceCreamMachine() {
        return AbstractDungeon.getMonsters().monsters.stream()
                .filter(mon -> mon instanceof IceCreamMachine && !mon.isDeadOrEscaped())
                .anyMatch(machine -> !((IceCreamMachine) machine).isAlly());
    }
    @Override
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("andoainmod:迷梦");
    }
}