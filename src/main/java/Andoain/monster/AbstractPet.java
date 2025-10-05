package Andoain.monster;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractPet extends CustomMonster {
    public AbstractPet(String name, String id, int maxHealth,
                       float hb_x, float hb_y, float hb_w, float hb_h,
                       String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }

    @Override
    public void damage(DamageInfo info) {
        // 只有非玩家来源的伤害才会生效
        if (info.owner != null && info.owner != AbstractDungeon.player) {
            super.damage(info);
        }
    }

    @Override
    public void takeTurn() {
        // 宠物通常不主动行动
    }

    @Override
    public void update() {
        super.update();
    }
}