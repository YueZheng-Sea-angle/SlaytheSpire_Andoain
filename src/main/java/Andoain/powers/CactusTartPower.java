package Andoain.powers;

import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;

import java.util.List;
import java.util.stream.Collectors;

public class CactusTartPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("CactusTart");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final int THORNS_PER_STACK = 2;

    public CactusTartPower(AbstractCreature owner, int amount) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;

        // 使用光赐于苦的美术资源
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/xianrenzhangta84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/xianrenzhangta32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] +
                (THORNS_PER_STACK * amount) +
                powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        if (hasEnemyIceCreamMachine()) {
            flash();
            addToBot(new ApplyPowerAction(
                    owner, owner,
                    new ThornsPower(owner, THORNS_PER_STACK * amount),
                    THORNS_PER_STACK * amount
            ));
        }
    }

    // 检查是否存在敌方冰淇淋机
    private boolean hasEnemyIceCreamMachine() {
        List<AbstractMonster> machines = AbstractDungeon.getMonsters().monsters.stream()
                .filter(mon -> mon instanceof IceCreamMachine && !mon.isDeadOrEscaped())
                .collect(Collectors.toList());

        for (AbstractMonster mon : machines) {
            if (!((IceCreamMachine) mon).isAlly()) {
                return true;
            }
        }
        return false;
    }
}