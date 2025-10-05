package Andoain.cards;

import Andoain.actions.PlayTopAttackCardAction;
import Andoain.actions.WhatISeeAction;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WhatISee extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("WhatISee");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("WhatISee");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    private static final int BASE_SEEK = 3;
    private static final int BASE_SEEK_PER_STACK = 3;
    private static final int UPGRADE_SEEK_PER_STACK = 4;

    public WhatISee() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = BASE_SEEK;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 计算总预见数量
        int totalSeek = BASE_SEEK;
        int seekPerStack = this.upgraded ? UPGRADE_SEEK_PER_STACK : BASE_SEEK_PER_STACK;

        if (p.hasPower(LightUntoSufferers.POWER_ID)) {
            int stacks = p.getPower(LightUntoSufferers.POWER_ID).amount;
            totalSeek += seekPerStack * stacks;
        }

        // 执行自定义预见动作
        this.addToBot(new ScryAction(totalSeek));
        this.addToBot(new PlayTopAttackCardAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new WhatISee();
    }
}