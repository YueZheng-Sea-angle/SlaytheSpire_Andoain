package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import Andoain.patches.CardColorEnum;
import Andoain.powers.WhiteCreamPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class IceCreamWhite extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("IceCreamWhite");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("IceCreamWhite");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.POWER;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    private static final int HEAL_AMOUNT = 5;
    private static final int HP_LOSS = 6;
    private static final int BASE_STACKS = 3;  // 新增：基础层数
    private static final int UPGRADED_STACKS = 4; // 新增：升级后层数
    public IceCreamWhite() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = BASE_STACKS; // 设置基础层数
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1); // 升级后费用从2降为1
            upgradeMagicNumber(UPGRADED_STACKS - BASE_STACKS); // 升级层数
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 应用奶油冰淇淋能力，传递层数参数
        addToBot(new ApplyPowerAction(
                p, p,
                new WhiteCreamPower(p, magicNumber), // 添加层数参数
                magicNumber
        ));
    }

    @Override
    public AbstractCard makeCopy() {
        return new IceCreamWhite();
    }

    // 检查是否存在友方冰淇淋机
    public static boolean hasFriendlyIceCreamMachine() {
        return AbstractDungeon.getMonsters().monsters.stream()
                .filter(mon -> mon instanceof IceCreamMachine && !mon.isDeadOrEscaped())
                .anyMatch(machine -> ((IceCreamMachine) machine).isAlly());
    }

    // 获取友方冰淇淋机
    public static IceCreamMachine getFriendlyIceCreamMachine() {
        return (IceCreamMachine)AbstractDungeon.getMonsters().monsters.stream()
                .filter(mon -> mon instanceof IceCreamMachine && !mon.isDeadOrEscaped() && ((IceCreamMachine)mon).isAlly())
                .findFirst()
                .orElse(null);
    }
}