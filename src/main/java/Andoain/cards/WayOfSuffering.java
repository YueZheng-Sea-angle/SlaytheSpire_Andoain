package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.EndlessAgony;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WayOfSuffering extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("WayOfSuffering");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int BASE_STACKS = 1; // 基础1层
    private static final int UPGRADED_STACKS = 2; // 升级后2层

    public WayOfSuffering() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("WayOfSuffering"),
                1, // 1费
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.COMMON, // 普通稀有度
                CardTarget.SELF
        );
        this.baseMagicNumber = BASE_STACKS;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }
    public void triggerWhenDrawn() {
        this.addToTop(new MakeTempCardInHandAction(this.makeStatEquivalentCopy()));
    }
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_STACKS - BASE_STACKS); // 层数提升到2
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onRevive();
        float delay = Settings.FAST_MODE ? 0.4f : 0.8f;
        addToBot(new WaitAction(delay));
        addToBot(new ApplyPowerAction(
                p, p,
                new LightUntoSufferers(p, magicNumber),
                magicNumber));
        
        // 添加抽2张牌效果
        addToBot(new DrawCardAction(p, 2));
    }
    public AbstractCard makeCopy() {
        return new WayOfSuffering();
    }

}