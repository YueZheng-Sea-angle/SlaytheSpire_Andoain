package Andoain.powers;

import Andoain.actions.ShadowAndAshConsumeAction;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ShadowAndAshPower extends AbstractPower {
    public static final String POWER_ID = "ShadowAndAsh";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public int totalGain = 0; // 新增独立计数器
    // 使用LightUntoSufferers的图标资源路径
    private static final String ICON_PATH_84 = "AndoainResources/img/powers/yingyuhui84.png";
    private static final String ICON_PATH_32 = "AndoainResources/img/powers/yingyuhui32.png";

    public ShadowAndAshPower(AbstractCreature owner) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = 0;
        this.totalGain = 0; // 使用独立字段记录增益

        // 加载图标（复用现有资源）
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage(ICON_PATH_84), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage(ICON_PATH_32), 0, 0, 32, 32);

        updateDescription();
    }
    public int getRemainingCapacity() {
        return 10 - this.totalGain;
    }
    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        // 触发消耗动作
        addToBot(new ShadowAndAshConsumeAction((com.megacrit.cardcrawl.characters.AbstractPlayer) owner));
    }
}