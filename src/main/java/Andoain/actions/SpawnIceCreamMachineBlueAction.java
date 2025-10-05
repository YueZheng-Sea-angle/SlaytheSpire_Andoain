package Andoain.actions;

import Andoain.monster.IceCreamMachineBlue;
import Andoain.patches.IceCreamMachineBluePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SpawnIceCreamMachineBlueAction extends AbstractGameAction {
    private static final float SPAWN_OFFSET_X = 180f * Settings.scale;
    private static final float SPAWN_OFFSET_Y = -50f * Settings.scale;

    public SpawnIceCreamMachineBlueAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // 确保场上只有一台
            if (IceCreamMachineBluePatch.iceCreamMachineBlue == null ||
                    IceCreamMachineBluePatch.iceCreamMachineBlue.isDeadOrEscaped()) {

                float spawnX = AbstractDungeon.player.hb.cX + SPAWN_OFFSET_X;
                float spawnY = AbstractDungeon.player.hb.cY + SPAWN_OFFSET_Y;

                IceCreamMachineBlue machine = new IceCreamMachineBlue(spawnX, spawnY);
                IceCreamMachineBluePatch.iceCreamMachineBlue = machine;

                // 初始化并显示
                machine.init();
                machine.showHealthBar();
                machine.applyPowers();
            }
        }
        this.tickDuration();
    }
}