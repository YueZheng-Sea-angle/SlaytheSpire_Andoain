package Andoain.potions;

import Andoain.cards.*;
import Andoain.helpers.ModHelper;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class IceCreamCoffee extends AbstractPotion {
    public static final String ID = "AndoainMod:IceCreamCoffee";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);

    // 可选择的卡牌列表
    private static final String[] CHOICE_CARDS = {
            "AndoainMod:Iris",
            "AndoainMod:IrisBlue",
            "AndoainMod:Hyacinth"
    };

    public IceCreamCoffee() {
        super(potionStrings.NAME, ID, AbstractPotion.PotionRarity.UNCOMMON,
                PotionSize.JAR, PotionColor.NONE);

        // 设置药水瓶图像
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg",
                new Texture("AndoainResources/img/potions/IceCreamCoffee.png"));

        this.labOutlineColor = Color.BROWN;
        this.isThrown = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        for (int i = 0; i < getPotency(); i++) {
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                AbstractPlayer p = AbstractDungeon.player;

                // 创建选择列表
                ArrayList<AbstractCard> choices = new ArrayList<>();
                for (String cardId : CHOICE_CARDS) {
                    AbstractCard card = createChoiceCard(cardId);
                    if (card != null) {
                        choices.add(card);
                    }
                }

                // 使用ChooseOneAction确保卡牌正确添加到手牌
                addToBot(new ChooseOneAction(choices));


            }
        }}
        // 创建选择卡牌并设置0费效果
        private AbstractCard createChoiceCard (String cardId){
            // 使用CardLibrary获取卡牌实例
            AbstractCard card = CardLibrary.getCard(cardId);
            if (card != null) {
                card = card.makeCopy();

                // 设置0费效果
                card.cost = 0;
                card.costForTurn = 0;
                card.isCostModified = true;
                card.isCostModifiedForTurn = true;

                // 添加特殊效果：加入手牌时触发
                card.purgeOnUse = true;
            }
            return card;
        }

    @Override
    public AbstractPotion makeCopy() {
        return new IceCreamCoffee();
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 1; // 不需要强度值
    }
}