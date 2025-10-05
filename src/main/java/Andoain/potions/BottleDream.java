package Andoain.potions;

import Andoain.helpers.ModHelper;
import Andoain.powers.DeepDreamPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class BottleDream extends AbstractPotion {
    public static final String ID = "AndoainMod:BottleDream";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString("AndoainMod:BottleDream");

    public BottleDream() {
        super(potionStrings.NAME, "AndoainMod:BottleDream", AbstractPotion.PotionRarity.UNCOMMON,
                PotionSize.MOON, PotionColor.NONE);

        // 设置药水瓶图像
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg",
                new Texture("AndoainResources/img/potions/BottleDream.png"));

        this.labOutlineColor = Color.PURPLE;
        this.isThrown = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(potionStrings.DESCRIPTIONS[1], potionStrings.DESCRIPTIONS[2]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;

            if (p.hasPower(DeepDreamPower.POWER_ID)) {
                // 已在迷梦中，离开迷梦
                addToBot(new RemoveSpecificPowerAction(p, p, DeepDreamPower.POWER_ID));
            } else {
                // 不在迷梦中，进入迷梦
                addToBot(new ApplyPowerAction(
                        p, p,
                        new DeepDreamPower(p, 1),
                        1));
            }
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BottleDream();
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 0; // 不需要强度值
    }
}