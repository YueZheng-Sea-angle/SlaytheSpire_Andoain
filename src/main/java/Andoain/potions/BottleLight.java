package Andoain.potions;

import Andoain.helpers.ModHelper;
import Andoain.powers.LightUntoSufferers;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class BottleLight extends AbstractPotion {
    public static final String ID = ModHelper.makePath("BottleLight");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);

    public BottleLight() {
        super(potionStrings.NAME, ID, AbstractPotion.PotionRarity.COMMON,
                AbstractPotion.PotionSize.BOTTLE, AbstractPotion.PotionColor.ENERGY);

        // 设置药水瓶图像
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg",
                new Texture("AndoainResources/img/potions/BottleLight.png"));

        this.labOutlineColor = Color.GOLDENROD;
        this.isThrown = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(potionStrings.DESCRIPTIONS[2], potionStrings.DESCRIPTIONS[3]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            // 添加应用光赐于苦效果的动作
            addToBot(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player,
                    new LightUntoSufferers(AbstractDungeon.player, this.potency),
                    this.potency));
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BottleLight();
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 2; // 固定获得2层光赐于苦
    }
}