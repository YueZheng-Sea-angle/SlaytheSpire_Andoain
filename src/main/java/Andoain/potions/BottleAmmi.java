package Andoain.potions;

import Andoain.helpers.ModHelper;
import Andoain.powers.AmmunitionPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class BottleAmmi extends AbstractPotion {
    public static final String ID = ModHelper.makePath("BottleAmmi");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);

    public BottleAmmi() {
        super(potionStrings.NAME, ID, AbstractPotion.PotionRarity.COMMON,
                PotionSize.JAR, AbstractPotion.PotionColor.FIRE);

        // 设置药水瓶图像
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg",
                new Texture("AndoainResources/img/potions/BottleAmmi.png"));

        this.labOutlineColor = Color.OLIVE;
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
            AbstractPlayer p = AbstractDungeon.player;

            if (p.hasPower(AmmunitionPower.POWER_ID)) {
                // 已有弹药能力，补充3弹药
                AmmunitionPower ammo = (AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID);
                ammo.replenish(this.potency);
            } else {
                // 没有弹药能力，添加弹药能力（上限3，初始弹药3）
                addToBot(new ApplyPowerAction(
                        p, p,
                        new AmmunitionPower(p, 3, 3),
                        3));
            }
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BottleAmmi();
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 3; // 不需要强度值
    }
}