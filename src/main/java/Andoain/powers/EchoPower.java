package Andoain.powers;

import Andoain.actions.SafeApplyEffectAction;
import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EchoPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("Echo");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Logger logger = LogManager.getLogger(EchoPower.class.getName());

    public boolean isTransferring = false;
    private boolean isProcessing = false;

    public EchoPower(AbstractCreature owner, int amount) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/shengsi84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/shengsi32.png"), 0, 0, 32, 32);

        updateDescription();
    }

    private static int effectDepth = 0;
    private static final int MAX_DEPTH = 1;

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    // 玩家治疗效果 → 冰淇淋机
    @Override
    public int onHeal(int healAmount) {
        // 添加状态标志检查
        if (!isTransferring && healAmount > 0 && owner.isPlayer) {
            shareWithIceCream(healAmount, EffectType.HEAL);
        }
        return healAmount;
    }

    @Override
    public void onGainedBlock(float blockAmount) {
        // 添加状态标志检查
        if (!isTransferring && blockAmount > 0 && owner.isPlayer) {
            shareWithIceCream((int) blockAmount, EffectType.BLOCK);
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // 添加状态标志检查
        if (!isTransferring && damageAmount > 0 && owner.isPlayer &&
                info.type == DamageInfo.DamageType.NORMAL) {
            shareWithIceCream(damageAmount, EffectType.DAMAGE);
        }
        return damageAmount;
    }

    // 冰淇淋机效果 → 玩家
    public static void onIceCreamMachineEffect(AbstractMonster machine, int amount, EffectType type) {
        if (AbstractDungeon.player == null || !(machine instanceof IceCreamMachine)) {
            logger.warn("Invalid Ice Cream Machine effect - player null or not IceCreamMachine");
            return;
        }

        if (AbstractDungeon.player.hasPower(POWER_ID)) {
            EchoPower power = (EchoPower) AbstractDungeon.player.getPower(POWER_ID);
            if (!power.isTransferring) {
                logger.info("Ice Cream Machine effect " + type + " for " + amount + ", attempting to share with player");
                power.shareWithPlayer(amount, type);
            } else {
                logger.debug("Skipping Ice Cream Machine effect - currently transferring");
            }
        }
    }

    private void shareWithIceCream(int amount, EffectType type) {
        if (amount <= 0) return; // 防止无效值

        flash();
        AbstractMonster iceCream = findIceCreamMachine();
        if (iceCream != null && !iceCream.isDeadOrEscaped()) {
            AbstractGameAction action = null;
            switch (type) {
                case BLOCK:
                    action = new GainBlockAction(iceCream, owner, amount);
                    break;
                case HEAL:
                    action = new HealAction(iceCream, owner, amount);
                    break;
                case DAMAGE:
                    action = new DamageAction(iceCream,
                            new DamageInfo(owner, amount, DamageInfo.DamageType.HP_LOSS),
                            AbstractGameAction.AttackEffect.NONE);
                    break;
            }
            if (action != null) {
                addToTop(new SafeApplyEffectAction(action));
            }
        }
    }

    private void shareWithPlayer(int amount, EffectType type) {
        if (amount <= 0) return; // 防止无效值

        flash();
        AbstractGameAction action = null;
        switch (type) {
            case BLOCK:
                action = new GainBlockAction(owner, owner, amount);
                break;
            case HEAL:
                action = new HealAction(owner, owner, amount);
                break;
            case DAMAGE:
                action = new DamageAction(owner,
                        new DamageInfo(owner, amount, DamageInfo.DamageType.NORMAL),
                        AbstractGameAction.AttackEffect.NONE);
                break;
        }
        if (action != null) {
            addToTop(new SafeApplyEffectAction(action));
        }
    }

    private AbstractMonster findIceCreamMachine() {
        AbstractMonster found = (AbstractMonster) AbstractDungeon.getMonsters().monsters.stream()
                .filter(m -> m instanceof IceCreamMachine && !m.isDeadOrEscaped())
                .findFirst()
                .orElse(null);

        logger.debug("Ice Cream Machine search result: " + (found != null ? "Found" : "Not found"));
        return found;
    }

    public enum EffectType {
        BLOCK, HEAL, DAMAGE
    }
}