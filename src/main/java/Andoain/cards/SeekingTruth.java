package Andoain.cards;

import Andoain.actions.SeekingTruthDamageDownAction;
import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.utility.WaitAction;

public class SeekingTruth extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("SeekingTruth");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("SeekingTruth");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int DAMAGE = 7;
    private static final int TIMES = 2;
    private static final int DAMAGE_DECREASE = 1;

    public SeekingTruth() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = TIMES;
        this.hasRicochet = true;
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0); // 升级后变为0费
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查是否有弹药，有弹药才播放攻击动画
        boolean hasAmmo = p.hasPower(AmmunitionPower.POWER_ID) &&
                ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).amount > 0;
        
        if (hasAmmo) {
            andoain.onAttack();
            float delay = Settings.FAST_MODE ? 0.7f : 1.4f;
            addToBot(new WaitAction(delay));
        }

        // 对主目标造成两次伤害
        for (int i = 0; i < this.magicNumber; i++) {
            addToBot(new DamageAction(
                    m,
                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.FIRE
            ));
        }

        // 触发弹射效果（两次伤害都触发完整弹射）
        handleRicochetMulti(p, m, this.magicNumber);

        // 永久减少所有同名卡牌的伤害
        addToBot(new SeekingTruthDamageDownAction(this, DAMAGE_DECREASE));
    }
}