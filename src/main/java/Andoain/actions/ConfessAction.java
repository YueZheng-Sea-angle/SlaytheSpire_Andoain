package Andoain.actions;

import Andoain.relics.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class ConfessAction extends AbstractGameAction {
    private final DamageInfo info;
    private final float chance;
    private static final float DURATION = Settings.ACTION_DUR_FAST;
    private final Random random = new Random();
    
    // 追踪本场战斗中通过告解获得的遗物
    private static final Set<String> battleRelicsObtained = new HashSet<>();

    public ConfessAction(AbstractCreature target, DamageInfo info, float chance) {
        this.info = info;
        this.target = target;
        this.chance = chance;
        this.actionType = ActionType.DAMAGE;
        this.duration = DURATION;
    }

    @Override
    public void update() {
        if (this.duration == DURATION && this.target != null) {
            // 显示攻击特效
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                    this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_HEAVY));

            // 造成伤害
            this.target.damage(this.info);

            // 检查是否斩杀
            if ((this.target.isDying || this.target.currentHealth <= 0) &&
                    !this.target.halfDead &&
                    !this.target.hasPower("Minion")) {

                // 根据概率决定是否获得遗物
                if (random.nextFloat() < chance) {
                    // 随机选择遗物
                    AbstractRelic relic = getRandomRelic();

                    // 检查是否已拥有该遗物（包括本场战斗中已获得的）
                    if (AbstractDungeon.player.hasRelic(relic.relicId) || battleRelicsObtained.contains(relic.relicId)) {
                        // 已拥有，改为获得50金币
                        AbstractDungeon.getCurrRoom().addGoldToRewards(50);
                    } else {
                        // 未拥有，添加遗物到奖励并记录到战斗遗物列表
                        AbstractDungeon.getCurrRoom().addRelicToRewards(relic);
                        battleRelicsObtained.add(relic.relicId);

                        // 显示特效
                        AbstractDungeon.actionManager.addToTop(
                                new RelicAboveCreatureAction(AbstractDungeon.player, relic));
                    }
                }
            }

            // 检查是否所有怪物都已死亡
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }

    private AbstractRelic getRandomRelic() {
        float rand = random.nextFloat();
        if (rand < 0.4f) { // 40% 显圣吊坠
            return new EpiphanyPendant();
        } else if (rand < 0.8f) { // 40% 光环
            return new AuraRelic();
        } else { // 20% 教堂救济餐券
            return new ChurchMealVoucher();
        }
    }
    
    /**
     * 清理战斗内遗物追踪列表，应在战斗结束时调用
     */
    public static void clearBattleRelics() {
        battleRelicsObtained.clear();
    }
}