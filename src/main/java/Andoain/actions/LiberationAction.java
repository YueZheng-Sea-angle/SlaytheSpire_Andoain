package Andoain.actions;

import Andoain.cards.Liberation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import java.util.UUID;

public class LiberationAction extends AbstractGameAction {
    private DamageInfo info;
    private UUID cardUUID;

    public LiberationAction(AbstractCreature target,
                            DamageInfo info,
                            UUID cardUUID) {
        this.setValues(target, info);
        this.info = info;
        this.cardUUID = cardUUID;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FASTER;
    }

    @Override
    public void update() {
        if (shouldCancelAction()) return;

        // 应用伤害
        target.damage(info);

        // 检查是否完成斩杀
        if ((target.isDying || target.currentHealth <= 0) &&
                !target.halfDead &&
                !target.hasPower("Minion")) {

            // 永久升级所有实例
            upgradeGlobally();

            // 播放特效
            AbstractDungeon.effectsQueue.add(
                    new UpgradeShineEffect(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f)
            );

            // 显示升级后的卡牌预览
            AbstractCard preview = getUpgradedPreview();
            if (preview != null) {
                AbstractDungeon.topLevelEffectsQueue.add(
                        new ShowCardBrieflyEffect(preview)
                );
            }
        }

        this.isDone = true;
    }

    protected boolean shouldCancelAction() {
        return duration == Settings.ACTION_DUR_FASTER &&
                (target == null || target.isDeadOrEscaped());
    }

    private void upgradeGlobally() {
        // 1. 升级主卡组
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(cardUUID) && c instanceof Liberation) {
                c.upgrade(); // 直接调用upgrade方法
            }
        }

        // 2. 升级当前战斗中的所有实例
        for (AbstractCard c : GetAllInBattleInstances.get(cardUUID)) {
            if (c instanceof Liberation) {
                c.upgrade(); // 直接调用upgrade方法
            }
        }
    }

    private AbstractCard getUpgradedPreview() {
        for (AbstractCard c : GetAllInBattleInstances.get(cardUUID)) {
            if (c instanceof Liberation) {
                return c.makeStatEquivalentCopy();
            }
        }
        return null;
    }
}