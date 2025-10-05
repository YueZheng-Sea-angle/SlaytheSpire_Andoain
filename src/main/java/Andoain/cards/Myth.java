package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.powers.DreamPower;
import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Myth extends CustomCard {
    public static final String ID = ModHelper.makePath("Myth");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = ModHelper.getCardImagePath("Myth");

    public Myth() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 0, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.exhaust = true;
    }
    @Override
    public void upgrade(){
        upgradeName();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(
                p, p,
                new DreamPower(p, 2),
                2
        ));
        // 获得1层虚弱
        addToBot(new ApplyPowerAction(p, p, new WeakPower(p, 1, false), 1));

        // 添加疑虑牌
        addToBot(new MakeTempCardInHandAction(new Doubt(), 1));

        // 全体晕眩
        AbstractDungeon.getMonsters().monsters.forEach(mon -> {
            if (!mon.isDeadOrEscaped()) {
                addToBot(new StunMonsterAction(mon, p, 1));
            }
        });
    }
    @Override
    public void onChoseThisOption() {
        AbstractCard newCard = this.makeStatEquivalentCopy();
        newCard.purgeOnUse = true;
        addToBot(new MakeTempCardInHandAction(newCard));
    }
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("andoainmod:迷梦");
    }
}