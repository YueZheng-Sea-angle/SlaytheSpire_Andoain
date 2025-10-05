package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.DreamPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class TidalMemory extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("TidalMemory");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("TidalMemory");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    // 使用自定义字段代替defaultBaseSecondMagicNumber
    private int statGain;
    private int doubtCards;

    public TidalMemory() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 3; // 梦境层数
        this.statGain = 4; // 力量和敏捷增益
        this.doubtCards = 4; // 疑虑卡数量
        this.exhaust = true;
        this.cardsToPreview = new Doubt();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onSkill(1);
        addToBot(new WaitAction(Settings.FAST_MODE ? 0.5f : 1.0f));

        addToBot(new ApplyPowerAction(p, p, new DreamPower(p, this.magicNumber)));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.statGain)));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.statGain)));

        // 使用原版方法添加疑虑卡
        AbstractCard doubt = new Doubt();
        for (int i = 0; i < this.doubtCards; i++) {
            addToBot(new MakeTempCardInDiscardAction(doubt.makeCopy(), 1));
        }

        addToBot(new PressEndTurnButtonAction());
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        // 动态更新描述
        this.rawDescription = (upgraded ? CARD_STRINGS.UPGRADE_DESCRIPTION : CARD_STRINGS.DESCRIPTION);
        initializeDescription();
    }
    @Override
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("andoainmod:迷梦");
    }
    @Override
    public AbstractCard makeCopy() {
        return new TidalMemory();
    }
}