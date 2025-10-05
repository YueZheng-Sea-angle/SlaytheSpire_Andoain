package Andoain.modcore;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.BitSet;

public class ModCardFlags {
    // 使用BitSet实现多标签系统
    private static final BitSet RICOCHET_FLAG = new BitSet(1);
    static {
        RICOCHET_FLAG.set(0); // 00000001
    }

    public static void makeRicochet(AbstractCard card) {
        card.tags.add(AbstractCard.CardTags.STARTER_STRIKE); // 借用未使用的原生标签作为标识
        card.keywords.add("弹射"); // 本地化显示用
    }

    public static boolean isRicochet(AbstractCard card) {
        // 双重验证机制
        return card.tags.contains(AbstractCard.CardTags.STARTER_STRIKE) ||
                card.keywords.contains("弹射");
    }
}
