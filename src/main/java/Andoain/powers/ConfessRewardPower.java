package Andoain.powers;

import Andoain.helpers.ModHelper;
import Andoain.relics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Random;

public class ConfessRewardPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("ConfessReward");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final float chance;
    private final Random random = new Random();

    public ConfessRewardPower(AbstractCreature owner, float chance) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.chance = chance;
        this.isTurnBased = false;

        // 加载图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/LightUntoSufferers84.jpg"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/LightUntoSufferers32.jpg"), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        int percent = (int)(chance * 100);
        this.description = String.format(DESCRIPTIONS[0], percent);
    }

    @Override
    public void onVictory() {
        if (random.nextFloat() < chance) {
            // 随机选择遗物
            AbstractRelic relic = getRandomRelic();

            // 添加遗物到奖励
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.valueOf(relic.relicId));

            // 显示特效
            flash();
            addToTop(new RelicAboveCreatureAction(owner, relic));
        }
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
}