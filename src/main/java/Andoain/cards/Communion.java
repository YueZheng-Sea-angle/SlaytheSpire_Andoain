package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.DreamPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.List;
import java.util.stream.Collectors;

public class Communion extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Communion");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Communion");
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int BASE_DREAM = 4;
    private static final int UPGRADE_DREAM = 5;
    private static final int PLAYER_WEAK = 99;
    private static final int ENEMY_WEAK = 99;
    private static final int UPGRADE_PLAYER_WEAK = 1;

    public Communion() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = BASE_DREAM;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true; // 消耗属性
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DREAM - BASE_DREAM); // 梦境层数增加
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 0.5f : 1.0f;
        addToBot(new WaitAction(delay));

        // 1. 获得梦境层数
        addToBot(new ApplyPowerAction(
                p, p,
                new DreamPower(p, this.magicNumber),
                this.magicNumber
        ));

        // 2. 自身获得虚弱
        int playerWeakAmount = upgraded ? UPGRADE_PLAYER_WEAK : PLAYER_WEAK;
        addToBot(new ApplyPowerAction(
                p, p,
                new WeakPower(p, playerWeakAmount, false),
                playerWeakAmount
        ));

        // 3. 目标敌人获得虚弱
        if (m != null && !m.isDeadOrEscaped()) {
            addToBot(new ApplyPowerAction(
                    m, p,
                    new WeakPower(m, ENEMY_WEAK, false),
                    ENEMY_WEAK
            ));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        // 更新描述文本
        this.rawDescription = upgraded ?
                CARD_STRINGS.UPGRADE_DESCRIPTION :
                CARD_STRINGS.DESCRIPTION;

        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = upgraded ?
                CARD_STRINGS.UPGRADE_DESCRIPTION :
                CARD_STRINGS.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Communion();
    }
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("andoainmod:迷梦");
    }
}