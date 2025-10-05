package Andoain.actions;

import Andoain.monster.IceCreamMachine;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MonsterTurnStartAction extends AbstractGameAction {
    @Override
    public void update() {
        // 检查是否已有冰淇淋机
        if (AbstractDungeon.getMonsters().monsters.stream()
                .noneMatch(m -> m instanceof IceCreamMachine)) {

            // 生成位置调整（右侧偏下，避免遮挡玩家）
            AbstractMonster newMonster = new IceCreamMachine(
                    Settings.WIDTH * 0.7f,  // X位置
                    Settings.HEIGHT * 0.4f  // Y位置
            );
            AbstractDungeon.getMonsters().addMonster(newMonster);

            // 确保新怪物参与回合
            AbstractDungeon.getCurrRoom().monsters.add(newMonster);

            System.out.println("[MonsterTurn] 生成 IceCreamMachine 完成");
        }
        this.isDone = true;
    }
}