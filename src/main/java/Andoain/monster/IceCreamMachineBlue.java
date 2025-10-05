package Andoain.monster;

import Andoain.patches.IceCreamMachineBluePatch;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.core.Settings;

public class IceCreamMachineBlue extends AbstractMonster {
    public static final String ID = "Andoain:IceCreamMachineBlue";
    private static final MonsterStrings monsterStrings;
    private static final String BLUE_IMG = "AndoainResources/img/monsters/IceCreamMachine_Blue.png";

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }

    public IceCreamMachineBlue(float x, float y) {
        super(monsterStrings.NAME, ID, 20,
                -50.0f, -20.0f, 180.0f, 180.0f,
                BLUE_IMG, x, y);

        this.damage.add(new DamageInfo(this, 0));
        setMove((byte)0, Intent.BUFF);
    }

    @Override
    public void takeTurn() {
        addToBot(new DrawCardAction(AbstractDungeon.player, 1));
        addToBot(new ApplyPowerAction(
                AbstractDungeon.player, this,
                new StrengthPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    protected void getMove(int num) {
        setMove((byte)0, Intent.BUFF);
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        if (IceCreamMachineBluePatch.iceCreamMachineBlue == this) {
            IceCreamMachineBluePatch.iceCreamMachineBlue = null;
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.intentHb != null) {
            this.intentHb.move(
                    this.hb.cX + this.hb.width / 2.0f,
                    this.hb.y + this.hb.height
            );
        }
    }
}