package Andoain.cards;

import Andoain.monster.IceCreamMachine;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DivineDesire extends AbstractAndoainCard {
    private static final Logger logger = LogManager.getLogger(DivineDesire.class.getName());
    public static final String ID = ModHelper.makePath("DivineDesire");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final int COST = 2;
    private static final int DAMAGE = 15;
    private static final int ENERGY_GAIN = 3;
    private static final int UPGRADE_DAMAGE = 5;
    private static final String IMG_PATH = ModHelper.getCardImagePath("DivineDesire");

    private boolean wasAlly;
    private IceCreamMachine trackedMachine;
    private boolean energyGranted = false;

    public DivineDesire() {
        super(ID,
                NAME,
                IMG_PATH,
                COST,
                DESCRIPTION,
                AbstractCard.CardType.ATTACK,
                CardColorEnum.Andoain_Blue,
                AbstractCard.CardRarity.UNCOMMON,
                AbstractCard.CardTarget.ENEMY);

        this.damage = this.baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        logger.info("----- DivineDesire card used -----");
        logger.info("Target: " + (m == null ? "null" : m.name));

        energyGranted = false;
        trackedMachine = null;
        wasAlly = false;

        // 补充1点弹药
        logger.info("Adding ammunition power...");
        if (!p.hasPower(AmmunitionPower.POWER_ID)) {
            logger.info("Adding new AmmunitionPower");
            addToBot(new ApplyPowerAction(p, p, new AmmunitionPower(p, 1)));
        } else {
            logger.info("Increasing existing AmmunitionPower");
            AmmunitionPower ammoPower = (AmmunitionPower) p.getPower(AmmunitionPower.POWER_ID);
            if (ammoPower != null) {
                ammoPower.replenish(1);
            } else {
                logger.error("AmmunitionPower is null!");
            }
        }

        // 如果目标是冰淇淋机，记录其当前状态
        if (m instanceof IceCreamMachine) {
            trackedMachine = (IceCreamMachine) m;
            wasAlly = trackedMachine.isAlly();
            logger.info("Tracking IceCreamMachine: ID=" + System.identityHashCode(trackedMachine) +
                    ", currentHealth=" + m.currentHealth +
                    ", wasAlly=" + wasAlly);
        } else {
            logger.info("Target is not an IceCreamMachine");
        }

        // 添加伤害行动
        logger.info("Adding damage action: " + this.damage + " damage");
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));

        // 添加检查行动
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                logger.info("----- Checking for IceCream break -----");
                checkForIceCreamBreak(m);
                isDone = true;
            }
        });
    }

    private void checkForIceCreamBreak(AbstractMonster target) {
        // 记录当前状态用于调试
        if (target != null) {
            logger.info("Target: " + target.name +
                    ", currentHealth=" + target.currentHealth +
                    ", isDead=" + target.isDead);
        } else {
            logger.warn("Target is null during break check");
        }

        if (trackedMachine == null) {
            logger.warn("No tracked machine");
            return;
        }

        logger.info("Tracked machine state: ID=" + System.identityHashCode(trackedMachine) +
                ", currentHealth=" + trackedMachine.currentHealth +
                ", isDead=" + trackedMachine.isDead +
                ", isAlly=" + trackedMachine.isAlly() +
                ", wasAlly=" + wasAlly);

        // 直接检查机器是否被击破（死亡或生命值≤0）
        if (trackedMachine.isDeadOrEscaped() || trackedMachine.currentHealth <= 0) {
            logger.info("IceCreamMachine was destroyed");
            grantEnergy();
        }
        // 检查阵营是否变化
        else if (trackedMachine.isAlly() != wasAlly) {
            logger.info("IceCreamMachine faction changed");
            grantEnergy();
        } else {
            logger.info("No break detected");
        }
    }

    private void grantEnergy() {
        if (!energyGranted) {
            energyGranted = true;
            logger.info("Granting " + ENERGY_GAIN + " energy");
            addToTop(new GainEnergyAction(ENERGY_GAIN));
            
            // 击破冰淇淋机时额外抽2张牌
            addToTop(new DrawCardAction(AbstractDungeon.player, 2));

            // 添加视觉反馈
            AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    logger.info("Playing energy gain effect");
                    if (AbstractDungeon.effectList.isEmpty()) {
                        CardCrawlGame.sound.play("POWER_MANTRA");
                    }
                    isDone = true;
                }
            });
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            initializeDescription();
        }
    }
}