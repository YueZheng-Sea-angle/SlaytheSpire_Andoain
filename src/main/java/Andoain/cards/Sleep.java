package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EquilibriumPower;

public class Sleep extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Sleep");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int BLOCK = 9;
    private static final int LIGHT_STACKS = 1;

    public Sleep() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("Sleep"),
                1, // 1费
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.UNCOMMON, // 罕见稀有度
                CardTarget.SELF
        );
        this.baseBlock = BLOCK;
        this.baseMagicNumber = LIGHT_STACKS;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true; // 消耗属性
        this.retain = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false; // 升级后不再消耗
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(
                p, p,
                new LightUntoSufferers(p, magicNumber),
                magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new EquilibriumPower((AbstractCreature)p, 1)));
        addToBot(new PressEndTurnButtonAction());
    }

    @Override
    public void atTurnStart() {
        this.retain = true; // 每回合开始时保持保留效果
    }
}