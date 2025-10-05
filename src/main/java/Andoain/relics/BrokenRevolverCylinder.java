package Andoain.relics;

import Andoain.helpers.ModHelper;
import Andoain.powers.AmmunitionPower;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BrokenRevolverCylinder extends CustomRelic {
    public static final String ID = ModHelper.makePath("BrokenRevolverCylinder");
    private static final String IMG_PATH = "AndoainResources/img/relics/BrokenRevolverCylinder.png";
    private static final int AMMO_CONSUME_THRESHOLD = 5;

    private int ammoConsumedThisCombat = 0;
    private boolean effectTriggered = false;

    public BrokenRevolverCylinder() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RelicTier.BOSS, LandingSound.CLINK);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void atBattleStart() {
        ammoConsumedThisCombat = 0;
        effectTriggered = false;
        this.counter = 0; // 初始化计数器
        beginLongPulse();
    }

    @Override
    public void atTurnStart() {
        // 每回合开始时获得1能量
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        if (effectTriggered) {
            stopPulse();
        }
    }

    // 修改后的弹药消耗监听方法
    public void onAmmoSpent(int amount) {
        if (effectTriggered || AbstractDungeon.player == null) return;

        ammoConsumedThisCombat += amount;
        this.counter = ammoConsumedThisCombat; // 更新计数器显示
        
        if (ammoConsumedThisCombat >= AMMO_CONSUME_THRESHOLD) {
            // 使用延迟动作避免递归
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    triggerAmmoReset();
                    isDone = true;
                }
            });
        }
    }

    private void triggerAmmoReset() {
        AbstractPlayer p = AbstractDungeon.player;
        AmmunitionPower ammo = (AmmunitionPower) p.getPower(AmmunitionPower.POWER_ID);

        if (ammo != null) {
            flash();
            stopPulse();
            // 直接设置值而不是调用方法，避免触发监听
            ammo.currentMax = 0;
            ammo.amount = 0;
            ammo.updateDescription();
            effectTriggered = true;
            addToTop(new RelicAboveCreatureAction(p, this));
        }
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onVictory() {
        this.counter = -1; // 战斗结束后隐藏层数显示
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BrokenRevolverCylinder();
    }
}