package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Fiammetta extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Fiammetta");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Fiammetta");
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ALL;

    private static final int BASE_DAMAGE = 27;
    private static final int UPGRADE_DAMAGE = 35;
    private static final int SELF_DAMAGE = 8;
    
    public Fiammetta() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = BASE_DAMAGE;
        this.magicNumber = this.baseMagicNumber = SELF_DAMAGE;
        this.isMultiDamage = true;
        this.cardsToPreview = new YouMustRepay();
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE - BASE_DAMAGE);
            this.cardsToPreview.upgrade();
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 0.5f : 1.0f;

        // 群体伤害
        addToBot(new DamageAllEnemiesAction(
                (AbstractCreature)p,
                this.multiDamage,
                DamageInfo.DamageType.NORMAL,
                AbstractGameAction.AttackEffect.FIRE
        ));

        // 自伤效果
        AbstractCard debtCard = new YouMustRepay();
        addToBot(new MakeTempCardInHandAction(debtCard, 1));

        addToBot(new WaitAction(delay));
    }
}