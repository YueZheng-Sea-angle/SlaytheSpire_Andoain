package Andoain.potions;

import Andoain.cards.Fiammetta;
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

public class FiammettaPotion extends AbstractPotion {
    public static final String ID = "AndoainMod:FiammettaPotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);

    public FiammettaPotion() {
        super(potionStrings.NAME, ID, AbstractPotion.PotionRarity.UNCOMMON,
                PotionSize.SPHERE, PotionColor.NONE);

        // 设置药水瓶图像
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg",
                new Texture("AndoainResources/img/potions/FiammettaPotion.png"));

        this.labOutlineColor = Color.RED;
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
            // 获得菲亚梅塔的报复+卡牌，本回合费用为0
            AbstractCard card = new Fiammetta();
            card.upgrade(); // 升级为+
            card.cost = 0;
            card.costForTurn = 0;
            card.isCostModified = true;
            card.isCostModifiedForTurn = true;

            addToBot(new MakeTempCardInHandAction(card, this.potency));
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new FiammettaPotion();
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 1;
    }
}