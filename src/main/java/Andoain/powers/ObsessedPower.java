package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class ObsessedPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("ObsessedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean upgraded;

    public ObsessedPower(AbstractCreature owner, boolean upgraded) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = -1; // 不使用amount变量
        this.upgraded = upgraded;

        // 加载图标资源
        String path128 = "AndoainResources/img/powers/digongsuojian84.png";
        String path48 = "AndoainResources/img/powers/digongsuojian32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        updateDescription();
    }

    public void updateDescription() {
        if (this.upgraded) {
            this.description = DESCRIPTIONS[1]; // 升级版描述
        } else {
            this.description = DESCRIPTIONS[0]; // 基础版描述
        }
    }

    public void stackPower(int stackAmount) {
        // 不需要堆叠功能，但保留方法以符合父类要求
    }

    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flash();

            // 获得1能量
            addToBot(new GainEnergyAction(1));

            // 获得1力量和1敏捷
            addToBot(new ApplyPowerAction(this.owner, this.owner,
                    new StrengthPower(this.owner, 1), 1));
            addToBot(new ApplyPowerAction(this.owner, this.owner,
                    new DexterityPower(this.owner, 1), 1));

            // 获得1金币
            addToBot(new GainGoldAction(1));

            // 根据升级状态决定卡牌去向
            if (this.upgraded) {
                addToBot(new MakeTempCardInDiscardAction(new Doubt(), 1));
                addToBot(new MakeTempCardInHandAction(new Wound(), 1));
            } else {
                addToBot(new MakeTempCardInDrawPileAction(new Doubt(), 1, true, true));
                addToBot(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
            }
        }
    }
}