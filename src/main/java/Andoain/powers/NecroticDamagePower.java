package Andoain.powers;

import Andoain.cards.IceCreamBlack;
import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;


public class NecroticDamagePower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("NecroticDamage");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    // 不再使用固定伤害，改为基于当前生命值的百分比
    private static final int THRESHOLD = 3;

    public NecroticDamagePower(AbstractCreature owner) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // 加载图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/qiaokelibingqilin84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/qiaokelibingqilin32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        if (!IceCreamBlack.hasFriendlyIceCreamMachine()) {
            return;
        }

        // 在回合开始时只添加凋亡损伤层数
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.isDeadOrEscaped() || m instanceof IceCreamMachine) continue;

            if (!m.hasPower(NecroticImmunityPower.POWER_ID)) {
                // 施加凋亡损伤
                addToBot(new ApplyPowerAction(
                        m, owner,
                        new NecroticStackPower(m, 1),
                        1
                ));
            }
            // 检查是否达到阈值
            NecroticStackPower stackPower = (NecroticStackPower)m.getPower(NecroticStackPower.POWER_ID);
            if (stackPower != null && stackPower.amount >= THRESHOLD-1) {
                // 达到阈值时触发效果
                addToBot(new RemoveSpecificPowerAction(
                        m, owner, NecroticStackPower.POWER_ID
                ));
                addToBot(new ApplyPowerAction(
                        m, owner,
                        new WeakPower(m, THRESHOLD, false),
                        THRESHOLD
                ));
                addToBot(new ApplyPowerAction(
                        m, owner,
                        new NecroticImmunityPower(m, THRESHOLD),
                        THRESHOLD
                ));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer || !IceCreamBlack.hasFriendlyIceCreamMachine()) {
            return;
        }

        // 在玩家回合结束时处理免疫单位的伤害和层数减少
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.isDeadOrEscaped() || m instanceof IceCreamMachine) continue;

            // 处理免疫单位的伤害（免疫效果永久保持不变）
            if (m.hasPower(NecroticImmunityPower.POWER_ID)) {
                // 计算当前生命值的8%
                int damageAmount = (int)(m.currentHealth * 0.08f);
                // 确保伤害至少为1
                if (damageAmount < 1) damageAmount = 1;
                
                addToBot(new DamageAction(
                        m,
                        new DamageInfo(AbstractDungeon.player, damageAmount, DamageInfo.DamageType.HP_LOSS),
                        AbstractGameAction.AttackEffect.POISON
                ));
                // 不再减少免疫层数，免疫效果永久有效
            }
        }
    }
}