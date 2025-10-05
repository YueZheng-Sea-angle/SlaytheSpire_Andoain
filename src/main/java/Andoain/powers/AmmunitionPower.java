package Andoain.powers;

import Andoain.helpers.ModHelper;
import Andoain.relics.BrokenRevolverCylinder;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AmmunitionPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("Ammunition");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public boolean canGain = true;
    public static final int MAX_AMMO = 3; // 默认上限
    public int currentMax; // 动态上限

    public AmmunitionPower(AbstractCreature owner) {
        this(owner, MAX_AMMO);
    }

    public AmmunitionPower(AbstractCreature owner, int maxAmmo) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.currentMax = maxAmmo;
        this.amount = 0; // 初始0弹药

        // 加载图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/Ammunition84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/Ammunition32.png"),
                0, 0, 32, 32);

        updateDescription();
    }
    public AmmunitionPower(AbstractCreature owner, int maxAmmo, int initialAmount) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.currentMax = maxAmmo;
        this.amount = initialAmount; // 设置初始弹药数量

        // 加载图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/Ammunition84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/Ammunition32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(
                powerStrings.DESCRIPTIONS[0],
                amount, currentMax);
    }

    // 补充弹药（不超过上限）
    public void replenish(int amount) {
        if(canGain){
            int prev = this.amount;
            this.amount = Math.min(currentMax, this.amount + amount);
            updateDescription();

            // 检查众生行记效果
            if (owner.isPlayer && owner.hasPower(TheWorldPower.POWER_ID) && this.amount > prev) {
                addToBot(new DrawCardAction(1));
                BaseMod.logger.info("AmmuWorld.");
            }
        }
    }

    // 消耗弹药（不少于0）
    public boolean spend(int amount) {
        if (this.amount >= amount) {
            int prev = this.amount;
            this.amount -= amount;
            updateDescription();
            onSpendAmmo(amount);
            notifyAmmoSpent(prev);
            return true;
        }
        return false;
    }
    public boolean spend(int amount, boolean special) {
        if (this.amount >= amount) {
            int prev = this.amount;
            this.amount -= amount;
            updateDescription();
            return true;
        }
        return false;
    }

    // 修改弹药上限
    public void setMax(int newMax) {
        this.currentMax = Math.max(0,newMax);
        this.amount = Math.min(amount, newMax);
        updateDescription();
    }
    public int getammo(){
        return  this.amount;
    }
    private void notifyAmmoSpent(int previousAmount) {
        if (owner.isPlayer && AbstractDungeon.player != null) {
            ThriftyHabitsPower power = (ThriftyHabitsPower) owner.getPower(ThriftyHabitsPower.POWER_ID);
            if (power != null) {
                int gained = power.onSpendAmmo();
                if (gained > 0) {
                    this.replenish(gained);
                }
            }
        }
}
    public void onSpendAmmo(int amount) {
        // 通知遗物弹药消耗
        if (owner.isPlayer && AbstractDungeon.player != null) {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof BrokenRevolverCylinder) {
                    ((BrokenRevolverCylinder)relic).onAmmoSpent(amount);
                }
            }
        }
    }

}