package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RequiemPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("Requiem");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final int blockPerCard;
    private final int dreamPerCard;
    private final boolean isUpgraded;
    private int dreamnum;

    public RequiemPower(AbstractCreature owner, int blockPerCard, int dreamPerCard, boolean isUpgraded) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.blockPerCard = blockPerCard;
        this.dreamPerCard = 1;
        this.isUpgraded = isUpgraded;
        this.type = PowerType.BUFF;
        this.amount = -1; // 不需要堆叠层数

        // 使用光赐于苦的美术资源
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/anhunqu84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/anhunqu32.png"), 0, 0, 32, 32);

        updateDescription();
    }
@Override
public void atStartOfTurn() {
        dreamnum = 0;
}
    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (isPlayer) {
            int curseCount = 0;

            // 检查手牌中的诅咒和状态牌
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
                    curseCount++;
                    // 消耗该牌
                    AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction(
                            c, AbstractDungeon.player.hand));
                }
            }
            dreamnum = 0;
            if (curseCount > 0) {
                dreamnum = curseCount;
                flash();
                // 每消耗一张获得格挡和梦境
                for (int i = 0; i < curseCount; i++) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, owner, blockPerCard));
                }
            }
        }
    }

    @Override
        public void atEndOfTurn(boolean isPlayer) {
        for (int i = 0; i < dreamnum; i++) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    owner, owner,
                    new DreamPower(owner, dreamPerCard),
                    dreamPerCard));
        }
    dreamnum = 0;
    }
    public void updateDescription() {
        if (isUpgraded) {
            this.description = powerStrings.DESCRIPTIONS[1];
        } else {
            this.description = powerStrings.DESCRIPTIONS[0];
        }
    }
}