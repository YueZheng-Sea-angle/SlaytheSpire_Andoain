package Andoain.powers;

import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;

public class DamageLinkPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("DamageLinkPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamageLinkPower(AbstractCreature owner) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // 加载图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/bingqilin84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/bingqilin32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && owner.isPlayer && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            int truedamage = (int) (damageAmount * 0.8);
            if(!owner.hasPower(BufferPower.POWER_ID)){
            // 查找当前友方冰淇淋机
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m instanceof IceCreamMachine && !m.isDeadOrEscaped() && ((IceCreamMachine)m).isAlly()) {
                    addToTop(new LoseHPAction(m, owner, truedamage));
                    break;
                }
            }}
        }
        return damageAmount;
    }
}