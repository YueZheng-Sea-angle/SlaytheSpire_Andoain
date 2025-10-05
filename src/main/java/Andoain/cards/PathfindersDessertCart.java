package Andoain.cards;

import Andoain.actions.ApplyMuddyBurnAction;
import Andoain.helpers.ModHelper;
import Andoain.powers.GuidingAheadPower;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PathfindersDessertCart extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("PathfindersDessertCart");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("PathfindersDessertCart");
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = AbstractCard.CardColor.COLORLESS;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.SPECIAL;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ALL_ENEMY;

    private static final int DAMAGE = 5; // 升级后额外伤害
    private static final int SCALDING = 1; // 灼燃损伤层数

    public PathfindersDessertCart() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.selfRetain = true;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人应用灼燃损伤
        AbstractDungeon.getMonsters().monsters.forEach(mon -> {
            if (!mon.isDeadOrEscaped()) {
                addToBot(new ApplyMuddyBurnAction(mon, p, SCALDING));

                // 升级后额外伤害
                if (upgraded) {
                    addToBot(new DamageAction(
                            mon,
                            new DamageInfo(p, DAMAGE, damageTypeForTurn),
                            AbstractGameAction.AttackEffect.FIRE
                    ));
                }
            }
        });
        if (p.hasPower(LightUntoSufferers.POWER_ID) &&
                p.hasPower(GuidingAheadPower.POWER_ID)) {
            p.gainEnergy(1);
            p.getPower(GuidingAheadPower.POWER_ID).flash();
        }
    }
    @Override
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("andoainmod:浊燃损伤");
    }
    @Override
    public AbstractCard makeCopy() {
        return new PathfindersDessertCart();
    }
}