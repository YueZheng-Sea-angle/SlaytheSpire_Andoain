package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
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
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class BattlefieldFlank extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("BattlefieldFlank");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String IMG_PATH = ModHelper.getCardImagePath("BattlefieldFlank");
    private static final int COST = 2;
    private static final int BASE_DAMAGE = 14;
    private static final int UPGRADE_DAMAGE = 24;
    private static final int BASE_VULNERABLE = 1;
    private static final int UPGRADE_VULNERABLE = 2;
    private static final Boolean BOUNCE = true;

    public BattlefieldFlank() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, CardColorEnum.Andoain_Blue,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.magicNumber = this.baseMagicNumber = BASE_VULNERABLE;
        this.hasRicochet = true; // 启用弹射功能
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查弹药并播放动画
        boolean hasAmmo = p.hasPower(AmmunitionPower.POWER_ID) &&
                ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).amount > 0;

        if (hasAmmo) {
            andoain.onAttack();
            float delay = Settings.FAST_MODE ? 0.7f : 1.4f;
            addToBot(new WaitAction(delay));
        }

        // 先给予目标易伤(升级后2层)
        addToBot(new ApplyPowerAction(m, p,
                new VulnerablePower(m, this.magicNumber, false),
                this.magicNumber));

        // 造成伤害
        addToBot(new SFXAction("ATTACK_DAGGER_1"));
        addToBot(new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, (int) (this.damage*1.5), DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        // 自身获得1层易伤(升级后仍为1层)
        addToBot(new ApplyPowerAction(p, p,
                new VulnerablePower(p, BASE_VULNERABLE, false),
                BASE_VULNERABLE));

        // 处理弹射逻辑
        handleRicochet(p, m);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE - BASE_DAMAGE);
            upgradeMagicNumber(UPGRADE_VULNERABLE - BASE_VULNERABLE);
            initializeDescription();
        }
    }
}