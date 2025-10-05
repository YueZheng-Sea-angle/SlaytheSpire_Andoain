package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.LTSongPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LTSong extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("LTSong");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = 1;

    public LTSong() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("LTSong"),
                COST,
                CARD_STRINGS.DESCRIPTION,
                CardType.POWER,
                CardColorEnum.Andoain_Blue,
                CardRarity.UNCOMMON,
                CardTarget.SELF
        );
        this.cardsToPreview = new Prayer();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.cardsToPreview.upgrade();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查是否需要转阶段
        if (p instanceof Andoain.character.andoain && !((Andoain.character.andoain)p).isSecondStage()) {
            Andoain.character.andoain.onRevive();
            float delay = Settings.FAST_MODE ? 0.4f : 0.8f;
            addToBot(new WaitAction(delay));
        }
        
        addToBot(new ApplyPowerAction(
                p, p,
                new LTSongPower(p, this.upgraded),
                1
        ));
    }
}