package Andoain.actions;

import Andoain.powers.StunForPlayerPower;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.List;

public class NurserySanctumAction extends AbstractGameAction {
    public enum EffectType {
        // 负面效果（对玩家有害）
        WEAK(2),             // 虚弱
        FRAIL(2),            // 脆弱
        VULNERABLE(2),       // 易伤
        POISON(5),           // 中毒
        STUN(1),             // 晕眩
        SLOW(5),             // 缓慢

        // 正面效果（对玩家有益）
        STRENGTH(2),         // 力量
        DEXTERITY(2),        // 敏捷
        FOCUS(4),            // 集中
        THORNS(3),           // 荆棘
        BLOCK(10);           // 格挡

        public final int amount;

        EffectType(int amount) {
            this.amount = amount;
        }

        public boolean isPositive() {
            return this == STRENGTH || this == DEXTERITY || this == FOCUS ||
                    this == THORNS || this == BLOCK;
        }
    }

    private final AbstractCreature target;
    private final AbstractCreature source;
    private final float positiveChance;
    private final boolean isPlayer;

    public NurserySanctumAction(AbstractCreature target, AbstractCreature source, float positiveChance, boolean isPlayer) {
        this.target = target;
        this.source = source;
        this.positiveChance = positiveChance;
        this.isPlayer = isPlayer;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // 决定应用正面还是负面效果
            boolean applyPositive = AbstractDungeon.monsterRng.randomBoolean(positiveChance);

            // 获取可用效果列表
            List<EffectType> validEffects = getValidEffects(applyPositive);

            if (!validEffects.isEmpty()) {
                // 随机选择一个效果
                Random rng = AbstractDungeon.monsterRng;
                EffectType selectedEffect = validEffects.get(rng.random(validEffects.size() - 1));

                // 应用效果
                applyEffect(selectedEffect);
            }
        }
        tickDuration();
        this.isDone = true;
    }

    private List<EffectType> getValidEffects(boolean wantPositive) {
        List<EffectType> valid = new ArrayList<>();

        for (EffectType effect : EffectType.values()) {
            // 筛选正负面类型
            if (effect.isPositive() != wantPositive) continue;

            // 筛选无效效果
            if (!isValidEffect(effect)) continue;

            valid.add(effect);
        }
        return valid;
    }

    private boolean isValidEffect(EffectType effect) {
        // 怪物不能获得集中
        if (!isPlayer && effect == EffectType.FOCUS) {
            return false;
        }
        // 自身不能获得缓慢
        if (isPlayer && effect == EffectType.SLOW) {
            return false;
        }
        return true;
    }

    private void applyEffect(EffectType effect) {
        switch (effect) {
            case WEAK:
                addToTop(new ApplyPowerAction(target, source, new WeakPower(target, effect.amount, false), effect.amount));
                break;
            case FRAIL:
                addToTop(new ApplyPowerAction(target, source, new FrailPower(target, effect.amount, false), effect.amount));
                break;
            case VULNERABLE:
                addToTop(new ApplyPowerAction(target, source, new VulnerablePower(target, effect.amount, false), effect.amount));
                break;
            case POISON:
                addToTop(new ApplyPowerAction(target, source, new PoisonPower(target, source, effect.amount), effect.amount));
                break;
            case STUN:
                if (target instanceof AbstractMonster) {
                    addToTop(new StunMonsterAction((AbstractMonster) target, source, effect.amount));
                } else {
                    addToTop(new ApplyPowerAction(target, source, new StunForPlayerPower(target, effect.amount), effect.amount));
                }
                break;
            case SLOW:
                addToTop(new ApplyPowerAction(target, source, new SlowPower(target, effect.amount), effect.amount));
                break;
            case STRENGTH:
                addToTop(new ApplyPowerAction(target, source, new StrengthPower(target, effect.amount), effect.amount));
                break;
            case DEXTERITY:
                addToTop(new ApplyPowerAction(target, source, new DexterityPower(target, effect.amount), effect.amount));
                break;
            case FOCUS:
                addToTop(new ApplyPowerAction(target, source, new FocusPower(target, effect.amount), effect.amount));
                break;
            case THORNS:
                addToTop(new ApplyPowerAction(target, source, new ThornsPower(target, effect.amount), effect.amount));
                break;
            case BLOCK:
                addToTop(new GainBlockAction(target, source, effect.amount));
                break;
        }
    }
}