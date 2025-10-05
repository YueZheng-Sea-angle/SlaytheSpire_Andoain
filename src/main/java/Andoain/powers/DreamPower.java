package Andoain.powers;

import Andoain.helpers.ModHelper;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DreamPower extends AbstractPower {

    public static final String POWER_ID = ModHelper.makePath("Dream");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int DREAM_THRESHOLD = 7;

    // 改为实例变量，追踪本场战斗获得的梦境总层数
    public int totalDreamGainedThisCombat = 0;

    public DreamPower(AbstractCreature owner, int amount) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;

        // 初始化时设置总层数
        this.totalDreamGainedThisCombat = 0;

        // 美术资源
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/mengjing84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/mengjing32.png"), 0, 0, 32, 32);
        if (owner.isPlayer && hasTheWorld()) {
            BaseMod.logger.info("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADreamWorld.");
            addToBot(new DrawCardAction(1));
        }
        updateDescription();

    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.totalDreamGainedThisCombat += stackAmount;
        checkDreamThreshold();
        // 检查众生行记效果


    }
    // 添加检查方法
    private boolean hasTheWorld() {
        return owner != null && owner.isPlayer &&
                ((AbstractPlayer)owner).hasPower(TheWorldPower.POWER_ID);
    }
    @Override
    public void onInitialApplication() {
        this.totalDreamGainedThisCombat += this.amount;
        checkDreamThreshold();
    }

    private void checkDreamThreshold() {
        if (this.amount >= DREAM_THRESHOLD) {
            addToBot(new ApplyPowerAction(owner, owner, new DeepDreamPower(owner, 1), 1));
            this.amount -= DREAM_THRESHOLD;
        }
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount +
                powerStrings.DESCRIPTIONS[1];
    }
}