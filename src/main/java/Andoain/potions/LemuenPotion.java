package Andoain.potions;

import Andoain.cards.Lemuen;
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

public class LemuenPotion extends AbstractPotion {
    public static final String ID = "AndoainMod:LemuenPotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);

    public LemuenPotion() {
        super(potionStrings.NAME, ID, AbstractPotion.PotionRarity.UNCOMMON,
                PotionSize.HEART, PotionColor.NONE);

        // 设置药水瓶图像
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg",
                new Texture("AndoainResources/img/potions/LemuenPotion.png"));

        this.labOutlineColor = Color.PINK;
        this.isThrown = false;
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            // 获得蕾缪安的注视+卡牌，本回合费用为0
            AbstractCard card = new Lemuen();
            card.upgrade(); // 升级为+
            card.cost = 0;
            card.costForTurn = 0;
            card.isCostModified = true;
            card.isCostModifiedForTurn = true;

            addToBot(new MakeTempCardInHandAction(card, getPotency()));
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new LemuenPotion();
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 1;
    }
}