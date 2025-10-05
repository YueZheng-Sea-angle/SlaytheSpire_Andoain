package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.NoAmmoGainPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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

import java.util.ArrayList;
import java.util.List;

public class Overflowing extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Overflowing");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Overflowing");
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ALL_ENEMY;

    private static final int DAMAGE = 8;
    private static final int NO_AMMO_GAIN_TURNS = 2;
    private static final int UPGRADE_DAMAGE = 2;

    public Overflowing() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = NO_AMMO_GAIN_TURNS;
        this.isMultiDamage = true;
        this.hasRicochet = true;
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE); // 升级后伤害从12提升到14
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放技能动画
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 3.0f : 4.0f;
        addToBot(new WaitAction(delay));

        // 获取当前弹药数但不消耗
        int ammoCount = p.hasPower(AmmunitionPower.POWER_ID) ?
                ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).amount : 0;

        // 获取有效的敌人目标（排除友方冰淇淋机）
        List<AbstractMonster> validTargets = getValidTargets();

        // 如果没有有效目标，直接返回
        if (validTargets.isEmpty()) {
            return;
        }

        // 对每个弹药执行攻击
        for (int i = 0; i < ammoCount; i++) {
            // 随机选择一个敌人
            AbstractMonster target = validTargets.get(AbstractDungeon.monsterRng.random(validTargets.size() - 1));

            // 造成伤害
            addToBot(new DamageAction(target,
                    new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));

            // 处理弹射效果
            handleRicochet(p, target);
        }

        // 施加无法获得弹药的效果
        addToBot(new ApplyPowerAction(p, p,
                new NoAmmoGainPower(p, this.magicNumber), this.magicNumber));
    }

    private List<AbstractMonster> getValidTargets() {
        List<AbstractMonster> validTargets = new ArrayList<>();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !isFriendlyIceCreamMachine(monster)) {
                validTargets.add(monster);
            }
        }
        return validTargets;
    }

    private boolean isFriendlyIceCreamMachine(AbstractMonster monster) {
        // 检查是否是友方冰淇淋机
        if (monster instanceof Andoain.monster.IceCreamMachine) {
            return ((Andoain.monster.IceCreamMachine)monster).isAlly();
        }
        return false;
    }
}