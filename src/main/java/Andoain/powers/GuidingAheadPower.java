package Andoain.powers;

import Andoain.cards.Pathfinders;
import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GuidingAheadPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("GuidingAhead");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final boolean upgraded;

    public GuidingAheadPower(AbstractCreature owner, boolean upgraded) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.upgraded = upgraded;
        this.type = PowerType.BUFF;

        // 测试阶段统一使用光赐于苦美术资源代替
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/wudaoxianlu84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/wudaoxianlu32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        // 改进描述拼接方式
        this.description = upgraded ?
                powerStrings.DESCRIPTIONS[1] :
                powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        if (owner.isPlayer || !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flash();
            AbstractCard card = new Pathfinders();
            if (upgraded) card.upgrade();
            addToBot(new MakeTempCardInHandAction(card, 1));
        }
    }
}