package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import java.util.ArrayList;

public class CherishPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("Cherish");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public boolean isUpgraded;

    public CherishPower(AbstractCreature owner, boolean isUpgraded) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.isUpgraded = isUpgraded;
        this.type = PowerType.BUFF;
        this.priority = 25; // 较高优先级

        // 复用LightUntoSufferers的美术资源，在美术设计阶段再替换
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/worengliulian84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/worengliulian32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        // 禁止叠加
    }

    @Override
    public void updateDescription() {
        if (isUpgraded) {
            this.description = powerStrings.DESCRIPTIONS[1];
        } else {
            this.description = powerStrings.DESCRIPTIONS[0];
        }
    }

    @Override
    public void atStartOfTurn() {
        if (owner.isPlayer) {
            ArrayList<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters;

            // 效果部分
            if (isUpgraded) {
                // 升级版：全体效果
                for (AbstractMonster m : monsters) {
                    if (!m.isDeadOrEscaped()) {
                        addToBot(new RemoveAllBlockAction(m, owner));
                        addToBot(new ApplyPowerAction(m, owner, new SlowPower(m, 1), 1));
                    }
                }
            } else {
                // 基础版：随机单个目标
                AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                if (m != null && !m.isDeadOrEscaped()) {
                    addToBot(new RemoveAllBlockAction(m, owner));
                    addToBot(new ApplyPowerAction(m, owner, new SlowPower(m, 1), 1));
                }
            }

            // 添加灼伤牌到弃牌堆
            AbstractCard burn = new Burn();
            if (isUpgraded) {
                burn.upgrade(); // 生成灼伤+
            }
            addToBot(new MakeTempCardInDiscardAction(burn, 1));
        }
    }
}