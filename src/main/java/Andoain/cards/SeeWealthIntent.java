package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import Andoain.powers.SeeWealthIntentPower;

public class SeeWealthIntent extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("SeeWealthIntent");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int BASE_COST = 3;
    private static final int UPGRADED_COST = 2;
    private static final int GOLD_GAIN = 30;
    private static final int MAX_PLAYS = 4;

    public SeeWealthIntent() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("SeeWealthIntent"),
                BASE_COST,
                CARD_STRINGS.DESCRIPTION,
                CardType.POWER,
                CardColorEnum.Andoain_Blue,
                CardRarity.UNCOMMON, // 罕见稀有度
                CardTarget.SELF
        );
        this.baseMagicNumber = 0; // 记录打出次数
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 增加打出次数
        this.magicNumber++;

        // 应用能力
        addToBot(new ApplyPowerAction(
                p, p,
                new SeeWealthIntentPower(p, GOLD_GAIN, this),
                0));

        // 检查是否达到最大打出次数
        if (this.magicNumber >= MAX_PLAYS) {
            addToBot(new ExhaustSpecificCardAction(this, p.hand));
            addToBot(new ExhaustSpecificCardAction(this, p.drawPile));
            addToBot(new ExhaustSpecificCardAction(this, p.discardPile));
            addToBot(new ExhaustSpecificCardAction(this, p.exhaustPile));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        // 更新描述以显示当前打出次数
        this.rawDescription = (upgraded ? CARD_STRINGS.UPGRADE_DESCRIPTION : CARD_STRINGS.DESCRIPTION)
                + "当前已打出：" + this.magicNumber;
        initializeDescription();
    }
}