package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.Iterator;

public class RequiemChurchPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("RequiemChurch");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public RequiemChurchPower(AbstractCreature owner, int blockPerAmmo) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = blockPerAmmo; // 关键：用amount存储每层的格挡系数
        this.type = PowerType.BUFF;

        // 默认使用光赐于苦的美术资源
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/anhunjiaotang84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/anhunjiaotang32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        // 叠加时直接累加格挡系数
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(
                powerStrings.DESCRIPTIONS[0],
                this.amount
        );
    }

    @Override
    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            // 计算总格挡系数（所有同名Power的amount之和）
            int totalMultiplier = getTotalMultiplier();

            // 获取弹药数量
            AbstractPower ammo = owner.getPower(AmmunitionPower.POWER_ID);
            if (ammo != null && ammo.amount > 0) {
                int block = totalMultiplier * ammo.amount;
                flash();
                addToBot(new GainBlockAction(owner, owner, block));
            }
        }
    }

    // 获取所有同名Power的总加成
    private int getTotalMultiplier() {
        int total = 0;
        Iterator<AbstractPower> it = owner.powers.iterator();
        while(it.hasNext()) {
            AbstractPower p = it.next();
            if (p.ID.equals(POWER_ID)) {
                total += p.amount;
            }
        }
        return total;
    }
}