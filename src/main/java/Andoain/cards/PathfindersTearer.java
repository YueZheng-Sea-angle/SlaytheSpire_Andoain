package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.powers.GuidingAheadPower;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PathfindersTearer extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("PathfindersTearer");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;

    private static final String IMG_PATH = ModHelper.getCardImagePath("PathfindersTearer");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = AbstractCard.CardColor.COLORLESS;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.SPECIAL;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ALL_ENEMY;

    private static final int DAMAGE = 16;
    private static final int UPGRADE_DAMAGE = 4;

    public PathfindersTearer() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.exhaust = true;
        this.isMultiDamage = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放技能动画
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 3.0f : 4.0f;
        addToBot(new WaitAction(delay));
        
        // 对所有敌人造成伤害
        AbstractDungeon.getMonsters().monsters.forEach(mon -> {
            if (!mon.isDeadOrEscaped()) {
                addToBot(new DamageAction(
                        mon,
                        new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.FIRE
                ));
            }
        });
        if (p.hasPower(LightUntoSufferers.POWER_ID) &&
                p.hasPower(GuidingAheadPower.POWER_ID)) {
            p.gainEnergy(1);
            p.getPower(GuidingAheadPower.POWER_ID).flash();
        }
    }

    @Override
    public void onChoseThisOption() {
        AbstractCard copy = this.makeStatEquivalentCopy();
        if (upgraded) copy.upgrade();
        addToBot(new MakeTempCardInHandAction(copy));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PathfindersTearer();
    }
}