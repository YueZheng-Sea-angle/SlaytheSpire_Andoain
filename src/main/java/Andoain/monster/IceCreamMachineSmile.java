package Andoain.monster;

import Andoain.helpers.PetHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class IceCreamMachineSmile extends Andoain.monster.AbstractPet {
    public static final String ID = "IceCreamMachineSmile";
    private static final MonsterStrings monsterStrings =
            CardCrawlGame.languagePack.getMonsterStrings(ID);

    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private static final int HP = 20;
    private static final float HB_X = 0.0F;
    private static final float HB_Y = 0.0F;
    private static final float HB_W = 100.0F;
    private static final float HB_H = 100.0F;
    private EnemyMoveInfo moveInfo;

    public IceCreamMachineSmile() {
        super(NAME, ID, HP, HB_X, HB_Y, HB_W, HB_H,
                "AndoainResources/img/monsters/IceCreamMachine_Blue.png",
                0.0F, 0.0F);

        this.type = EnemyType.NORMAL;
    }
    @Override
    protected void getMove(int num) {
        // 设置为BUFF意图，显示为蓝色图标
        this.setMove((byte)0, Intent.BUFF);

        // 自定义BUFF描述（可选）
        this.moveInfo = new EnemyMoveInfo((byte) 0, Intent.BUFF, -1, 0, false);
    }

    @Override
    public void usePreBattleAction() {
        // 战斗开始时注册到玩家
        PetHelper.addPet(AbstractDungeon.player, this);
    }

    @Override
    public void applyStartOfTurnPowers() {
        // 玩家回合开始时抽1张牌
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(AbstractDungeon.player, 1));
    }

    // 处理牵连伤害的方法
    public static void onPlayerDamaged(AbstractCreature player, int damageAmount) {
        for (AbstractMonster pet : PetHelper.getPets((AbstractPlayer) player)) {
            if (pet instanceof IceCreamMachineSmile) {
                // 玩家受到伤害时，冰淇淋机受到50%的牵连伤害
                int splitDamage = (int)(damageAmount * 0.5f);
                if (splitDamage > 0) {
                    pet.damage(new DamageInfo(player, splitDamage, DamageInfo.DamageType.THORNS));
                }
            }
        }
    }

    @Override
    public void die() {
        super.die();
        PetHelper.removePet(AbstractDungeon.player, this);
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        PetHelper.removePet(AbstractDungeon.player, this);
    }
}