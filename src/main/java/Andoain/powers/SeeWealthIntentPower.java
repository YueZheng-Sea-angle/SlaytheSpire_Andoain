package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Andoain.cards.SeeWealthIntent;

public class SeeWealthIntentPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("SeeWealthIntentPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private final int goldGain;
    private final SeeWealthIntent sourceCard;

    public SeeWealthIntentPower(AbstractCreature owner, int goldGain, SeeWealthIntent sourceCard) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.goldGain = goldGain;
        this.sourceCard = sourceCard;
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/jcqy84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/jcqy32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + goldGain + powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public void onVictory() {
        AbstractDungeon.getCurrRoom().addGoldToRewards(goldGain);
        flash();
    }

    @Override
    public void onRemove() {
        // 检查是否有其他相同的power存在
        boolean hasOther = false;
        for (AbstractPower p : owner.powers) {
            if (p instanceof SeeWealthIntentPower && p != this) {
                hasOther = true;
                break;
            }
        }

        // 如果没有其他相同的power，且卡片还在牌组中，增加打出次数
        if (!hasOther && sourceCard != null) {
            sourceCard.baseMagicNumber++;
            sourceCard.magicNumber = sourceCard.baseMagicNumber;
        }
    }
}