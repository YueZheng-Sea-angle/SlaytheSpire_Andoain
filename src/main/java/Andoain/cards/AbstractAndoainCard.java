package Andoain.cards;

import Andoain.monster.IceCreamMachine;
import Andoain.powers.FreeNextCardPower;
import Andoain.powers.ThriftyHabitsPower;
import Andoain.relics.AuraRelic;
import Andoain.relics.EpiphanyPendant;
import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Andoain.powers.AmmunitionPower;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.FreeAttackPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;

public abstract class AbstractAndoainCard extends CustomCard {
    public boolean hasRicochet = false; // 弹射开关
    private static float FIRST_MULTI = 0.8f;
    private static float SECOND_MULTI = 0.6f;
    private static final Color ENERGY_COST_MODIFIED_COLOR = Color.GREEN.cpy();
    private static final Color ENERGY_COST_RESTRICTED_COLOR = Color.RED.cpy();

    public AbstractAndoainCard(String id, String name, String img, int cost, String rawDescription,
                               CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    public static void setFirstMulti(float firstMulti) {
        FIRST_MULTI = firstMulti;
    }
    public static void setSecondMulti(float secondMulti){
        SECOND_MULTI = secondMulti;
    }


    // 弹射核心逻辑
    protected void handleRicochet(AbstractPlayer p, AbstractMonster mainTarget) {
        if (!hasRicochet) return;

        AmmunitionPower ammo = (AmmunitionPower) p.getPower(AmmunitionPower.POWER_ID);
        if (ammo == null || ammo.amount < 1) return;



        List<AbstractMonster> aliveMonsters = AbstractDungeon.getMonsters().monsters.stream()
                .filter(m -> !m.isDeadOrEscaped())
                .collect(Collectors.toList());

        // 第一次弹射
        AbstractMonster firstTarget = selectRicochetTarget(mainTarget, aliveMonsters, true);
        BaseMod.logger.info("target:"+firstTarget+"has been selected.");
        if (firstTarget == null || (firstTarget instanceof IceCreamMachine && ((IceCreamMachine) firstTarget).isAlly())) {
            notifyRicochetFail(p);
        }
        ammo.spend(1);
        if (firstTarget != null) {
            applyRicochetDamage(p, firstTarget, FIRST_MULTI);

            // 添加回调，确保在第一次伤害处理后执行第二次弹射
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    // 获取当前存活的怪物（此时第一次弹射的伤害已生效）
                    List<AbstractMonster> currentAliveMonsters = AbstractDungeon.getMonsters().monsters.stream()
                            .filter(m -> !m.isDeadOrEscaped())
                            .collect(Collectors.toList());

                    // 第二次弹射，排除第一次的目标（无论是否存活）
                    AbstractMonster secondTarget = selectRicochetTarget(firstTarget, currentAliveMonsters, false);
                    if (secondTarget != null) {
                        applyRicochetDamage(p, secondTarget, SECOND_MULTI);
                    }
                    this.isDone = true;
                }
            });
        }
    }

    private AbstractMonster selectRicochetTarget(AbstractMonster excludeTarget,
                                                 List<AbstractMonster> candidates,
                                                 boolean isFirstBounce) {
        // 获取玩家是否拥有光环遗物
        boolean hasAura = AbstractDungeon.player.hasRelic(AuraRelic.ID);

        return candidates.stream()
                .filter(m -> isValidTarget(m, excludeTarget, isFirstBounce, hasAura))
                .sorted((a, b) -> targetPriority(a, b, hasAura))
                .findFirst()
                .orElse(null);
    }

    private boolean isValidTarget(AbstractMonster target,
                                  AbstractMonster excludeTarget,
                                  boolean isFirstBounce,
                                  boolean hasAura) {
        // 通用排除条件
        if (target == excludeTarget) return false;

        // 如果没有光环遗物，直接排除友方冰淇淋机
        if (!hasAura && isAllyIceCream(target)) return false;

        // 第一次弹射特殊条件
        if (isFirstBounce && excludeTarget instanceof IceCreamMachine) {
            return !(target instanceof IceCreamMachine);
        }
        return true;
    }

    private int targetPriority(AbstractMonster a, AbstractMonster b, boolean hasAura) {
        // 优先级1：敌方冰淇淋机
        boolean aIsEnemyIce = isEnemyIceCream(a);
        boolean bIsEnemyIce = isEnemyIceCream(b);
        if (aIsEnemyIce != bIsEnemyIce) {
            return aIsEnemyIce ? -1 : 1;
        }

        // 优先级2：普通怪物
        boolean aIsNormal = !(a instanceof IceCreamMachine);
        boolean bIsNormal = !(b instanceof IceCreamMachine);
        if (aIsNormal != bIsNormal) {
            return aIsNormal ? -1 : 1;
        }

        // 优先级3：（有光环时）友方冰淇淋机
        if (hasAura) {
            boolean aIsAllyIce = isAllyIceCream(a);
            boolean bIsAllyIce = isAllyIceCream(b);
            if (aIsAllyIce != bIsAllyIce) {
                return aIsAllyIce ? 1 : -1; // 友方优先级最低
            }
        }

        return 0; // 同优先级随机
    }
    protected void applyRicochetDamage(AbstractPlayer p, AbstractMonster target, float baseMulti) {
        // 检查显圣吊坠遗物
        float finalMulti = p.hasRelic(EpiphanyPendant.ID) ? 1.0f : baseMulti;

        int damage = (int)(this.damage * finalMulti);
        addToBot(new DamageAction(
                target,
                new DamageInfo(p, damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.FIRE
        ));

        // 触发遗物特效
        if (p.hasRelic(EpiphanyPendant.ID)) {
            p.getRelic(EpiphanyPendant.ID).flash();
        }
    }

    // 辅助方法
    private boolean isEnemyIceCream(AbstractMonster m) {
        return m instanceof IceCreamMachine && !((IceCreamMachine) m).isAlly();
    }

    private boolean isAllyIceCream(AbstractMonster m) {
        return m instanceof IceCreamMachine && ((IceCreamMachine) m).isAlly();
    }

    // 复制自UnInvited的小队牌ID列表
    private static final List<String> TEAM_CARD_IDS = Arrays.asList(
            "Mostima", "Fiammetta", "Lemuen", "MyLateran"
    );
    // 在 AbstractAndoainCard.java 中修改 isTeamCard 方法
    public boolean isTeamCard() {
        return UnInvited.isTeamCard(this); // 复用统一逻辑
    }
    // 完全模仿原版 FreeToPlay 实现
    @Override
    public boolean freeToPlay() {
        if (this.freeToPlayOnce) {
            return true;
        } else {
            return AbstractDungeon.player != null &&
                    AbstractDungeon.currMapNode != null &&
                    AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                    (AbstractDungeon.player.hasPower(FreeNextCardPower.POWER_ID) || AbstractDungeon.player.hasPower(FreeAttackPower.POWER_ID));
        }
    }

    // 完全模仿原版 hasEnoughEnergy 实现
    @Override
    public boolean hasEnoughEnergy() {
        if (AbstractDungeon.actionManager.turnHasEnded) {
            this.cantUseMessage = TEXT[9];
            return false;
        } else {
            for(AbstractPower p : AbstractDungeon.player.powers) {
                if (!p.canPlayCard(this)) {
                    // 如果卡牌已经有自定义的cantUseMessage，则保留它
                    if (this.cantUseMessage == null || this.cantUseMessage.isEmpty()) {
                        this.cantUseMessage = TEXT[13];
                    }
                    return false;
                }
            }

            if (AbstractDungeon.player.hasPower("Entangled") && this.type == AbstractCard.CardType.ATTACK) {
                this.cantUseMessage = TEXT[10];
                return false;
            } else {
                for(AbstractRelic r : AbstractDungeon.player.relics) {
                    if (!r.canPlay(this)) {
                        return false;
                    }
                }

                for(AbstractBlight b : AbstractDungeon.player.blights) {
                    if (!b.canPlay(this)) {
                        return false;
                    }
                }

                for(AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (!c.canPlay(this)) {
                        return false;
                    }
                }

                if (EnergyPanel.totalCount < this.costForTurn && !this.freeToPlay() && !this.isInAutoplay) {
                    this.cantUseMessage = TEXT[11];
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    // 完全模仿原版 canUse 实现
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (this.type == AbstractCard.CardType.STATUS && this.costForTurn < -1 && !AbstractDungeon.player.hasRelic("Medical Kit")) {
            return false;
        } else if (this.type == AbstractCard.CardType.CURSE && this.costForTurn < -1 && !AbstractDungeon.player.hasRelic("Blue Candle")) {
            return false;
        } else {
            return this.cardPlayable(m) && this.hasEnoughEnergy();
        }
    }

    // 完全模仿原版 getCost 实现
    private String getCost() {
        if (this.cost == -1) {
            return "X";
        } else {
            return this.freeToPlay() ? "0" : Integer.toString(this.costForTurn);
        }
    }



    // 复制其他必要的原生方法
    @Override
    public void triggerOnGlowCheck() {
        if (this.freeToPlay()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            super.triggerOnGlowCheck();
        }
    }
    // 在 AbstractAndoainCard 类中添加以下方法
    protected void handleRicochetMulti(AbstractPlayer p, AbstractMonster mainTarget, int times) {
        boolean hasspend = false;
        if (!hasRicochet) return;

        AmmunitionPower ammo = (AmmunitionPower) p.getPower(AmmunitionPower.POWER_ID);
        if (ammo == null || ammo.amount < 1) return;




        for (int i = 0; i < times; i++) {
            // 为每次伤害创建独立的弹射目标列表
            List<AbstractMonster> aliveMonsters = AbstractDungeon.getMonsters().monsters.stream()
                    .filter(m -> !m.isDeadOrEscaped())
                    .collect(Collectors.toList());

            // 第一次弹射
            AbstractMonster firstTarget = selectRicochetTarget(mainTarget, aliveMonsters, true);
            if (firstTarget == null) {
                notifyRicochetFail(p);
            }
            BaseMod.logger.info("SP"+hasspend);
            if(!hasspend && i== 1){
                ammo.spend(1);// 只消耗一次弹药
                BaseMod.logger.info("SPEND1"+hasspend);
            hasspend = true;
            BaseMod.logger.info("HASSPEND"+hasspend);}
            BaseMod.logger.info("SKIPSPEND"+hasspend);
            hasspend = false;
            BaseMod.logger.info("RESETSPEND"+hasspend);
            if (firstTarget != null) {
                applyRicochetDamage(p, firstTarget, FIRST_MULTI);

                // 添加回调，确保在第一次伤害处理后执行第二次弹射
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        // 获取当前存活的怪物（此时第一次弹射的伤害已生效）
                        List<AbstractMonster> currentAliveMonsters = AbstractDungeon.getMonsters().monsters.stream()
                                .filter(m -> !m.isDeadOrEscaped())
                                .collect(Collectors.toList());

                        // 第二次弹射，排除第一次的目标（无论是否存活）
                        AbstractMonster secondTarget = selectRicochetTarget(firstTarget, currentAliveMonsters, false);
                        if (secondTarget != null) {
                            applyRicochetDamage(p, secondTarget, SECOND_MULTI);
                        }
                        this.isDone = true;
                    }
                });
            }
        }

    }
    private void notifyRicochetFail(AbstractPlayer p) {
        ThriftyHabitsPower power = (ThriftyHabitsPower) p.getPower(ThriftyHabitsPower.POWER_ID);
        if (power != null) {
            power.onRicochetFail();
        }
    }
    }
