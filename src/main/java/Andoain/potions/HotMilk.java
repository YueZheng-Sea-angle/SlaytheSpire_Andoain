package Andoain.potions;

import Andoain.cards.Prayer;
import Andoain.helpers.ModHelper;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class HotMilk extends AbstractPotion {
    public static final String ID = "AndoainMod:HotMilk";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);

    public HotMilk() {

        super(potionStrings.NAME, ID, AbstractPotion.PotionRarity.RARE,
                PotionSize.SPHERE, PotionColor.NONE);
        this.potency = this.getPotency();
        // 设置药水瓶图像
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg",
                new Texture("AndoainResources/img/potions/HotMilk.png"));

        this.labOutlineColor = Color.WHITE;
        this.isThrown = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            // 获得三张祷告+卡牌
            for (int i = 0; i < potency; i++) {
                AbstractCard card = new Prayer();
                card.upgrade(); // 升级为祷告+
                addToBot(new MakeTempCardInHandAction(card, 1));
            }
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new HotMilk();
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 3;
    }
}