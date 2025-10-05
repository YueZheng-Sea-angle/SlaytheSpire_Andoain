package Andoain.powers;

import Andoain.cards.IceCreamWhite;
import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WhiteCreamPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("WhiteCream");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int HEAL_AMOUNT = 4;
    private static final int HP_LOSS = 6;

    public WhiteCreamPower(AbstractCreature owner, int stacks) {
        this.amount = stacks;
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // 使用光赐于苦美术资源代替
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/xiangcao84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/xiangcao32.png"),
                0, 0, 32, 32);

        updateDescription();

    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] +
                HP_LOSS + powerStrings.DESCRIPTIONS[1] +
                HEAL_AMOUNT + powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void atStartOfTurn() {
        if (!IceCreamWhite.hasFriendlyIceCreamMachine()) {
            amount--;
            updateDescription();
            return;
        }

        // 只有有可用层数时才触发
        if (amount <= 0) {
            // 可选：移除层数为0的能力
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
            return;
        }

        IceCreamMachine machine = IceCreamWhite.getFriendlyIceCreamMachine();
        if (machine != null) {
            // 使冰淇淋机失去生命
            int damageToTake = Math.min(HP_LOSS, machine.currentHealth - 1);
            if (damageToTake > 0) {
                machine.currentHealth -= damageToTake;
                machine.healthBarUpdatedEvent();

                // 治疗玩家
                addToBot(new HealAction(owner, owner, HEAL_AMOUNT));

                // 减少层数
                amount--;
                updateDescription();

                // 视觉反馈
                flash();
            }
        }
    }
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }
}