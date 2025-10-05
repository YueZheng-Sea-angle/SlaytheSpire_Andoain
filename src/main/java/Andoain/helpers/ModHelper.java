/*    */ package Andoain.helpers;

import Andoain.cards.Worship;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.lang.reflect.Field;

/*    */
/*    */ public class ModHelper {
/*    */   public static String makePath(String id) {
/*  5 */     return "AndoainMod:" + id;
/*    */   }
/*  7 */   public static String getCardImagePath(String id) { return "AndoainResources/img/cards/" + id + ".png"; }
/*  8 */   public static String getCardImagePath2(String id) { return "AndoainResources/img/cards/" + id + ".jpg"; }
/*  9 */   public static String getRelicImagePath(String id) { return "AndoainResources/img/relics/" + id + ".png"; } public static String useDemoImage() {
/* 10 */     return "AndoainResources/img/cards/Strike_demo.png";
/* 11 */   } public static String addtional_description = " NL 虚无 。 NL Andoainmod:厄尔苏拉 。";
/* 12 */   public static String addtional_description2 = " NL Andoainmod:厄尔苏拉 。";
/* 13 */   public static String addtional_description3 = " NL 虚无 。";
    private static Worship.DayOfWeek savedWeekDay = Worship.DayOfWeek.MONDAY;

    public static Worship.DayOfWeek getWeekDay() {
        return savedWeekDay;
    }

    public static void setWeekDay(Worship.DayOfWeek day) {
        savedWeekDay = day;
    }

    // 如果需要支持存档/读档（可选）
    public static void saveWeekDay() {
        // 实现存档逻辑（如使用BaseMod的保存系统）
    }

    public static void loadWeekDay() {
        // 实现读档逻辑
    }

    /**
     * 检查当前是否正在播放自定义音乐
     * @return 如果正在播放Angel.ogg或Guiding.ogg返回true，否则返回false
     */
    public static boolean isCustomMusicPlaying() {
        try {
            // 使用反射访问MusicMaster的tempTrack字段
            Field tempTrackField = CardCrawlGame.music.getClass().getDeclaredField("tempTrack");
            tempTrackField.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.ArrayList<TempMusic> tempTrack = (java.util.ArrayList<TempMusic>) tempTrackField.get(CardCrawlGame.music);
            
            // 检查tempTrack中是否有自定义音乐
            for (TempMusic tempMusic : tempTrack) {
                if (tempMusic != null && tempMusic.key != null) {
                    if (tempMusic.key.equals("Angel.ogg") || tempMusic.key.equals("Guiding.ogg")) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            // 如果反射失败，返回false，允许播放音乐
            System.out.println("检查自定义音乐播放状态时出现异常: " + e.getMessage());
            return false;
        }
    }

    /**
     * 检查当前是否在与乐加维林战斗
     * 乐加维林会播放自己的临时音乐（ELITE），需要同时淡出临时音乐和层数音乐
     * @return 如果是乐加维林返回true，否则返回false
     */
    public static boolean isFightingLagavulin() {
        try {
            com.megacrit.cardcrawl.rooms.AbstractRoom room = com.megacrit.cardcrawl.dungeons.AbstractDungeon.getCurrRoom();
            
            if (room instanceof com.megacrit.cardcrawl.rooms.MonsterRoom) {
                com.megacrit.cardcrawl.rooms.MonsterRoom monsterRoom = (com.megacrit.cardcrawl.rooms.MonsterRoom) room;
                
                if (monsterRoom.monsters != null && !monsterRoom.monsters.monsters.isEmpty()) {
                    for (com.megacrit.cardcrawl.monsters.AbstractMonster monster : monsterRoom.monsters.monsters) {
                        if (monster.type == com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType.ELITE && 
                            (monster.name.equals("Lagavulin") || monster.name.equals("乐加维林"))) {
                            return true;
                        }
                    }
                }
            }
            
            // 检查事件房间
            if (room instanceof com.megacrit.cardcrawl.rooms.EventRoom) {
                if (AbstractDungeon.getMonsters() != null && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    for (com.megacrit.cardcrawl.monsters.AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                        if (!monster.isDead && !monster.isEscaping &&
                            monster.type == com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType.ELITE && 
                            (monster.name.equals("Lagavulin") || monster.name.equals("乐加维林"))) {
                            return true;
                        }
                    }
                }
            }
            
            return false;
        } catch (Exception e) {
            System.out.println("检查乐加维林战斗时出现异常: " + e.getMessage());
            return false;
        }
    }

    /**
     * 检查当前是否需要进行特殊的BGM切换处理
     * 只包括boss战斗、第一阶段boss战斗和心灵绽放事件中的战斗
     * 注意：乐加维林等精英怪物使用普通的淡出逻辑，不需要特殊处理
     * @return 如果需要特殊处理返回true，否则返回false
     */
    public static boolean needsSpecialBGMSwitch() {
        try {
            // System.out.println("开始检查特殊BGM切换需求...");
            
            // 检查是否为boss战斗（标准boss房间）
            if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss) {
                // System.out.println("检测到标准Boss房间");
                return true;
            }
            
            // 检查任何房间中是否有boss类型的怪物
            com.megacrit.cardcrawl.rooms.AbstractRoom room = com.megacrit.cardcrawl.dungeons.AbstractDungeon.getCurrRoom();
            // System.out.println("当前房间类型: " + room.getClass().getSimpleName());
            
            if (room instanceof com.megacrit.cardcrawl.rooms.MonsterRoom) {
                com.megacrit.cardcrawl.rooms.MonsterRoom monsterRoom = (com.megacrit.cardcrawl.rooms.MonsterRoom) room;
                
                // 检查房间中是否有boss类型怪物（包括第一阶段boss和普通boss）
                if (monsterRoom.monsters != null && !monsterRoom.monsters.monsters.isEmpty()) {
                    for (com.megacrit.cardcrawl.monsters.AbstractMonster monster : monsterRoom.monsters.monsters) {
                        // System.out.println("发现怪物: " + monster.name + " 类型: " + monster.type);
                        
                        // 检查是否为boss类型（包括第一阶段boss）
                        if (monster.type == com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType.BOSS) {
                            // System.out.println("检测到Boss怪物，需要特殊BGM切换");
                            return true;
                        }
                    }
                }
            }
            
            // 检查是否为事件房间（如心灵绽放事件）
            if (room instanceof com.megacrit.cardcrawl.rooms.EventRoom) {
                // System.out.println("检测到事件房间");
                // 对于事件房间，检查是否有怪物在场（如心灵绽放事件中的战斗）
                if (AbstractDungeon.getMonsters() != null && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    // System.out.println("事件房间中有活着的怪物");
                    // 检查当前怪物是否为boss类型
                    for (com.megacrit.cardcrawl.monsters.AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                        if (!monster.isDead && !monster.isEscaping) {
                            // System.out.println("事件房间中的怪物: " + monster.name + " 类型: " + monster.type);
                            if (monster.type == com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType.BOSS) {
                                // System.out.println("事件房间中检测到Boss怪物，需要特殊BGM切换");
                                return true;
                            }
                        }
                    }
                }
            }
            
            // System.out.println("未检测到需要特殊BGM切换的情况");
            return false;
        } catch (Exception e) {
            System.out.println("检查特殊BGM切换需求时出现异常: " + e.getMessage());
            // 出现异常时，保守起见返回true，确保进行正确的BGM切换
            return true;
        }
    }
/*    */ }


/* Location:              C:\Users\24430\Desktop\Andoain-mod.jar!\Andoain\helpers\ModHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */