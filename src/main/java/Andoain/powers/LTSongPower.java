package Andoain.powers;

import Andoain.helpers.ModHelper;
import Andoain.cards.Prayer;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LTSongPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("LTSong");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final boolean isUpgraded;

    public LTSongPower(AbstractCreature owner, boolean isUpgraded) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.isUpgraded = isUpgraded;
        this.type = PowerType.BUFF;
        this.amount = -1; // 不需要堆叠层数

        // 使用光赐于苦的美术资源
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/songge84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/songge32.png"), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flash();
            // 创建祷告牌
            AbstractCard prayer = new Prayer();
            if (isUpgraded) {
                prayer.upgrade();
            }
            addToBot(new MakeTempCardInHandAction(prayer, 1));
        }
    }

    @Override
    public void updateDescription() {
        if (isUpgraded) {
            this.description = powerStrings.DESCRIPTIONS[1];
        } else {
            this.description = powerStrings.DESCRIPTIONS[0];
        }
    }
}