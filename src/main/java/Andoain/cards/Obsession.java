package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.powers.SelectiveRetainPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Obsession extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Obsession");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = ModHelper.getCardImagePath("Obsession");

    // 卡牌基础属性
    private static final int COST = 0;
    private static final int STRENGTH = 1;
    private static final int ARTIFACT = 1;
    private static final int HEAL_AMOUNT = 5;
    private static final int RETAIN_AMOUNT = 1;

    public Obsession() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.NONE);
        this.exhaust = true;
        this.magicNumber = this.baseMagicNumber = STRENGTH;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 基础效果
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, ARTIFACT), ARTIFACT));
        addToBot(new HealAction(p, p, HEAL_AMOUNT));

        // 添加保留能力
        addToBot(new ApplyPowerAction(p, p,
                new RetainCardPower(p, 1), 1));
    }

    @Override
    public void onChoseThisOption() {
        // 被选择时生成到手牌
        AbstractCard newCard = this.makeCopy();
        newCard.purgeOnUse = true; // 使用后消失
        addToBot(new MakeTempCardInHandAction(newCard));
    }
}