package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.actions.utility.WaitAction;

import java.util.List;
import java.util.stream.Collectors;

public class Protection extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Protection");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Protection");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    private static final int BLOCK = 8;
    private static final int INTANGIBLE_AMOUNT = 1;
    private static final int UPGRADE_BLOCK = 3;
    private static final int UPGRADE_INTANGIBLE = 1;

    public Protection() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = INTANGIBLE_AMOUNT;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            upgradeMagicNumber(UPGRADE_INTANGIBLE);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 0.5f : 1.0f;
        addToBot(new WaitAction(delay));

        // 1. 获得格挡
        addToBot(new GainBlockAction(p, p, this.block));

        // 2. 获取所有友方冰淇淋机
        List<IceCreamMachine> friendlyMachines = AbstractDungeon.getMonsters().monsters.stream()
                .filter(mon -> mon instanceof IceCreamMachine && !mon.isDeadOrEscaped())
                .map(mon -> (IceCreamMachine) mon)
                .filter(IceCreamMachine::isAlly)
                .collect(Collectors.toList());

        // 3. 为每个友方冰淇淋机添加无实体效果
        for (IceCreamMachine machine : friendlyMachines) {
            addToBot(new ApplyPowerAction(
                    machine, p,
                    new IntangiblePlayerPower(machine, this.magicNumber),
                    this.magicNumber
            ));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Protection();
    }
}