package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.powers.AutoRefrigerationPower;
import Andoain.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AutoRefrigeration extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("AutoRefrigeration");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);


    public AutoRefrigeration() {
        super(ID, CARD_STRINGS.NAME, "AndoainResources/img/cards/AutoRefrigeration.png",
                3, CARD_STRINGS.DESCRIPTION, CardType.POWER,
                CardColorEnum.Andoain_Blue, CardRarity.RARE, CardTarget.SELF);

        if (!this.upgraded) {
            this.isEthereal = true;
        }

        // 设置静态预览卡牌为消耗版本
        this.cardsToPreview = new CandyTime_E();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.isEthereal = false;
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
        
        addToBot(new ApplyPowerAction(p, p, new AutoRefrigerationPower(p, 1), 1));
    }

}