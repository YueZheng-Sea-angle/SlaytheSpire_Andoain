package Andoain.util;

import Andoain.monster.IceCreamMachine;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class BreakIceCreamTrigger {
    private static final Set<Consumer<AbstractMonster>> callbacks = new HashSet<>();
    private static boolean triggeredThisCombat = false;

    public static void addCallback(Consumer<AbstractMonster> callback) {
        callbacks.add(callback);
    }

    public static void onMonsterDeath(AbstractMonster monster) {
        if (!triggeredThisCombat && monster instanceof IceCreamMachine) {
            triggeredThisCombat = true;
            callbacks.forEach(callback -> callback.accept(monster));
        }
    }

    public static void onCombatStart() {
        triggeredThisCombat = false;
    }

    public static AbstractGameAction createDelayedCheckAction(AbstractCreature target) {
        return new AbstractGameAction() {
            @Override
            public void update() {
                if (target instanceof AbstractMonster) {
                    AbstractMonster m = (AbstractMonster) target;
                    if (m.isDeadOrEscaped()) {
                        onMonsterDeath(m);
                    }
                }
                isDone = true;
            }
        };
    }
}