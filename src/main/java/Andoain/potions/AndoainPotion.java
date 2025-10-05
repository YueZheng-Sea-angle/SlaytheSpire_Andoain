package Andoain.potions;

import Andoain.helpers.ModHelper;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ObtainPotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AndoainPotion extends AbstractPotion {
    public static final String ID = "AndoainMod:AndoainPotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);

    // 专属药水列表（包含自身）
    private static final List<String> EXCLUSIVE_POTIONS = Arrays.asList(
            "BottleLight",
            "BottleAmmi",
            "BottleDream",
            "AndoainPotion",
            "IceCreamCoffee",
            "HotMilk",
            "MostimaPotion",
            "FiammettaPotion",
            "LemuenPotion"
    );

    public AndoainPotion() {
        super(potionStrings.NAME, ID, AbstractPotion.PotionRarity.RARE,
                PotionSize.BOTTLE, PotionColor.NONE);

        // 设置药水瓶图像
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg",
                new Texture("AndoainResources/img/potions/BottleAndoain.png"));

        this.labOutlineColor = Color.CYAN;
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
        AbstractPlayer p = AbstractDungeon.player;

        // 1. 移除所有负面效果
        removeNegativeEffects(p);

        // 2. 像混沌药水一样生成药水
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            // 在战斗中使用
            for(int i = 0; i < AbstractDungeon.player.potionSlots; i++) {
                AbstractPotion randomPotion = getRandomExclusivePotion();
                if (randomPotion != null) {
                    addToBot(new ObtainPotionAction(randomPotion));
                }
            }
        } else {
            // 在非战斗中使用
            for(int i = 0; i < AbstractDungeon.player.potionSlots; i++) {
                AbstractPotion randomPotion = getRandomExclusivePotion();
                if (randomPotion != null) {
                    AbstractDungeon.effectsQueue.add(new ObtainPotionEffect(randomPotion));
                }
            }
        }
    }

    // 移除所有负面效果
    private void removeNegativeEffects(AbstractPlayer p) {
        List<AbstractPower> toRemove = new ArrayList<>();
        for (AbstractPower power : p.powers) {
            if (isNegativePower(power)) {
                toRemove.add(power);
            }
        }

        for (AbstractPower power : toRemove) {
            addToTop(new RemoveSpecificPowerAction(p, p, power.ID));
        }
    }

    // 判断是否为负面效果
    private boolean isNegativePower(AbstractPower power) {
        return power.type == AbstractPower.PowerType.DEBUFF;
    }

    // 获取随机专属药水实例
    private AbstractPotion getRandomExclusivePotion() {
        try {
            // 完全随机选择药水（包括自身AndoainPotion）
            String potionName = EXCLUSIVE_POTIONS.get(AbstractDungeon.potionRng.random(EXCLUSIVE_POTIONS.size() - 1));
            Class<?> potionClass = Class.forName("Andoain.potions." + potionName);
            return (AbstractPotion) potionClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new AndoainPotion();
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 0;
    }
}