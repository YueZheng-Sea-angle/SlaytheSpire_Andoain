package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.DreamPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TheTime extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("TheTime");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("TheTime");

    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int DAMAGE = 14;
    private static final int UPGRADE_DAMAGE = 18;
    private static final int WEAK_AMOUNT = 1;
    private static final int UPGRADE_WEAK_AMOUNT = 2;

    public TheTime() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.hasRicochet = true; // 启用弹射功能
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE - DAMAGE);
            this.magicNumber = UPGRADE_WEAK_AMOUNT;
            this.baseMagicNumber = UPGRADE_WEAK_AMOUNT;
            initializeDescription();
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(
                p, p,
                new DreamPower(p, 2),
                2
        ));
        // 检查是否有弹药
        boolean hasAmmo = p.hasPower(AmmunitionPower.POWER_ID) &&
                ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).amount > 0;

        // 如果有弹药才播放攻击动画
        if (hasAmmo) {
            andoain.onAttack();
            float delay = Settings.FAST_MODE ? 0.7f : 1.4f;
            addToBot(new WaitAction(delay));
        }

        // 造成伤害
        addToBot(new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, DamageInfo.DamageType.NORMAL)));

        // 处理精英战和领袖战的虚弱效果
        if (m.type == AbstractMonster.EnemyType.ELITE || m.type == AbstractMonster.EnemyType.BOSS) {
            int weakAmount = this.upgraded ? UPGRADE_WEAK_AMOUNT : WEAK_AMOUNT;
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, weakAmount, false), weakAmount));
        }

        // 处理弹射
        handleRicochet(p, m);
    }
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("andoainmod:迷梦");
    }
}