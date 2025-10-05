package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class Eulogy extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Eulogy");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Eulogy");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int DAMAGE = 9;
    private static final int VULNERABLE = 1;
    private static final int UPGRADE_VULNERABLE = 2;
    private static final int BASE_DRAW = 1;
    private static final int UPGRADE_DRAW = 2;

    public Eulogy() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = VULNERABLE;
        this.hasRicochet = true;
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_VULNERABLE - VULNERABLE); // 升级易伤层数
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean hasAmmo = p.hasPower(AmmunitionPower.POWER_ID) &&
                ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).amount > 0;

        // 如果有弹药才播放攻击动画
        if (hasAmmo) {
            andoain.onAttack();
            float delay = Settings.FAST_MODE ? 0.7f : 1.4f;
            addToBot(new WaitAction(delay));
        }
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, DamageInfo.DamageType.NORMAL)));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                (AbstractPower)new VulnerablePower((AbstractCreature)m, this.magicNumber, false),
                this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        
        // 抽牌效果
        int drawAmount = this.upgraded ? UPGRADE_DRAW : BASE_DRAW;
        addToBot(new DrawCardAction(p, drawAmount));
        
        handleRicochet(p, m);
    }
}