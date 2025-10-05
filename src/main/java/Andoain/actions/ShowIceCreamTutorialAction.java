package Andoain.actions;

import Andoain.modcore.AndoainMod;
import Andoain.monster.IceCreamMachine;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.FtueTip;

public class ShowIceCreamTutorialAction extends com.megacrit.cardcrawl.actions.AbstractGameAction {
    private static final UIStrings uiStrings =
            CardCrawlGame.languagePack.getUIString("Andoain:IceCreamTutorial");
    private static final String[] TEXT = uiStrings.TEXT;

    private final IceCreamMachine machine;
    private final int step;

    public ShowIceCreamTutorialAction(IceCreamMachine machine, int step) {
        this.machine = machine;
        this.step = step;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // 计算提示框位置（怪物左侧）
            float x = machine.hb.cX - machine.hb.width - 140.0F * Settings.scale;
            float y = machine.hb.cY;

            // 根据步骤选择不同的文本
            String title = TEXT[step * 2];
            String description = TEXT[step * 2 + 1];

            // 创建提示框
            AbstractDungeon.ftue = new FtueTip(
                    title,
                    description,
                    x, y,
                    FtueTip.TipType.CREATURE
            );

            // 关联怪物模型
            ReflectionHacks.setPrivate(
                    AbstractDungeon.ftue,
                    FtueTip.class,
                    "m",
                    machine
            );

            // 如果是最后一步，更新状态并保存
            if (step == 2) {
                AndoainMod.iceCreamTutorialShown = true;
                AndoainMod.saveTutorialStatus();
            }
        }
        this.tickDuration();
    }
}