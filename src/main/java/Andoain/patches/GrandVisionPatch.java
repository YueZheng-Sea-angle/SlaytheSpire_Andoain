package Andoain.patches;

import Andoain.events.GrandVision;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.random.Random;

public class GrandVisionPatch {
    public static boolean eventTriggered = false;

    @SpirePatch(clz = AbstractDungeon.class, method = "generateEvent")
    public static class EventGenPatch {
        @SpirePostfixPatch
        public static AbstractEvent Postfix(AbstractEvent _ret, Random rng) {
            if (!eventTriggered && !(_ret instanceof GrandVision)) {
                eventTriggered = true; // 标记已触发
                //return new GrandVision(); // 100%返回我们的事件
            }
            return _ret;
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition",
            paramtypez = {com.megacrit.cardcrawl.saveAndContinue.SaveFile.class})
    public static class ResetPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon __instance) {
            //eventTriggered = false; // 每次进入新房间时重置(保险措施)
        }
    }
}