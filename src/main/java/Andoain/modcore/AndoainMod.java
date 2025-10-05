package Andoain.modcore;
import Andoain.cards.*;
/*     */ import Andoain.character.andoain;
import Andoain.events.GrandVision;
import Andoain.monster.IceCreamMachine;
import Andoain.patches.CardColorEnum;
import Andoain.patches.GrandVisionPatch;
import Andoain.patches.PlayerEnum;
import Andoain.potions.*;
import Andoain.relics.*;
import basemod.AutoAdd;
import basemod.BaseMod;
/*     */ import basemod.helpers.RelicType;
import basemod.interfaces.*;
/*     */
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import Andoain.powers.LightUntoSufferers;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
/*     */
/*     */
/*     */
/*     */ import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*     */ @SpireInitializer
/*     */ public class AndoainMod implements EditStringsSubscriber, EditKeywordsSubscriber, EditCardsSubscriber, EditRelicsSubscriber, EditCharactersSubscriber,PostBattleSubscriber, PostDungeonInitializeSubscriber,OnStartBattleSubscriber{

    /*     */   private static final String MY_CHARACTER_BUTTON = "AndoainResources/img/char/Character_Button.png";
    /*     */   private static final String MY_CHARACTER_PORTRAIT = "AndoainResources/img/char/Character_Portrait.png";
    /*     */   private static final String BG_ATTACK_512 = "AndoainResources/img/512/bg_attack_512.png";
    /*     */   private static final String BG_POWER_512 = "AndoainResources/img/512/bg_power_512.png";
    /*     */   private static final String BG_SKILL_512 = "AndoainResources/img/512/bg_skill_512.png";
    /*  61 */   public static final Color MY_COLOR = new Color(0.58039216F, 0.6F, 0.6666667F, 1.0F);
    private static final String SMALL_ORB = "AndoainResources/img/char/small_orb.png";
    private static final String BG_ATTACK_1024 = "AndoainResources/img/1024/bg_attack.png";
    private static final String BG_POWER_1024 = "AndoainResources/img/1024/bg_power.png";
    private static final String BG_SKILL_1024 = "AndoainResources/img/1024/bg_skill.png";
    private static final String BIG_ORB = "AndoainResources/img/char/card_orb.png";
    private static final String ENEYGY_ORB = "AndoainResources/img/char/cost_orb.png";
    public static boolean iceCreamTutorialShown = false;
    private static final String CONFIG_KEY = "iceCreamTutorialShown";
    private static boolean hasRestoredBGM = false; // 标志位，防止重复恢复层数音乐
    /*     */
    public AndoainMod() {
        /*  63 */
        BaseMod.logger.debug("Constructor started.");
        /*  64 */
        BaseMod.subscribe((ISubscriber) this);
        /*  65 */
        BaseMod.addColor(CardColorEnum.Andoain_Blue, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, "AndoainResources/img/512/bg_attack_512.png", "AndoainResources/img/512/bg_skill_512.png", "AndoainResources/img/512/bg_power_512.png", "AndoainResources/img/char/cost_orb.png", "AndoainResources/img/1024/bg_attack.png", "AndoainResources/img/1024/bg_skill.png", "AndoainResources/img/1024/bg_power.png", "AndoainResources/img/char/card_orb.png", "AndoainResources/img/char/small_orb.png");
        /*     */
    }
    @Override
    public void receivePostBattle(AbstractRoom room) {
        // 战斗结束后重置伤害计数器
        LightUntoSufferers.resetTotalDamageTaken();
        BaseMod.logger.info("战斗结束，重置光赐于苦伤害计数器");
        
        // 恢复原层数音乐（只执行一次）
        if (!hasRestoredBGM) {
            restoreOriginalBGM();
            hasRestoredBGM = true;
        }
    }
    
    private void restoreOriginalBGM() {
        try {
            // 检查是否真的播放了自定义音乐
            boolean hadCustomMusic = Andoain.helpers.ModHelper.isCustomMusicPlaying();
            
            // 停止临时音乐
            CardCrawlGame.music.fadeOutTempBGM();
            
            // 只有在播放了自定义音乐的情况下才重新播放层数音乐
            if (hadCustomMusic) {
                String currentDungeonId = AbstractDungeon.id;
                if (currentDungeonId != null) {
                    CardCrawlGame.music.changeBGM(currentDungeonId);
                    BaseMod.logger.info("检测到自定义音乐，停止临时音乐并重新播放层数音乐: " + currentDungeonId);
                } else {
                    BaseMod.logger.warn("无法获取当前地牢ID，跳过层数音乐恢复");
                }
            } else {
                BaseMod.logger.info("未检测到自定义音乐，只停止临时音乐");
            }
        } catch (Exception e) {
            BaseMod.logger.error("恢复原层数音乐时出现异常: " + e.getMessage());
        }
    }

    @Override
    public void receivePostDungeonInitialize() {
        // 地牢初始化时重置伤害计数器（处理中途退出重新挑战的情况）
        LightUntoSufferers.resetTotalDamageTaken();
        BaseMod.logger.info("地牢初始化，重置光赐于苦伤害计数器");
    }
    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        LightUntoSufferers.resetTotalDamageTaken();
        BaseMod.logger.info("战斗开始，重置光赐于苦伤害计数器");
        
        // 重置BGM恢复标志位，允许新战斗结束后恢复音乐
        hasRestoredBGM = false;
        
        // 重置角色到第一阶段
        Andoain.character.andoain.onResetToFirstStage();
        BaseMod.logger.info("战斗开始，重置角色到第一阶段");
    }
    /*     */
    /*     */
    public static void initialize() {
        /*  69 */
        new AndoainMod();
    }
    public void receiveEditCards() {
        new AutoAdd("AndoainTest") // 这里填写你在ModTheSpire.json中写的modid
                .packageFilter(Iris.class) // 寻找所有和此类同一个包及内部包的类（本例子是所有卡牌）
                .setDefaultSeen(true) // 是否将卡牌标为可见
                .cards(); // 开始批量添加卡牌
    }

    public void receiveEditRelics() {
        BaseMod.addRelic(new MassesTravels(), RelicType.SHARED); // RelicType表示是所有角色都能拿到的遗物，还是一个角色的独有遗物
        BaseMod.addRelicToCustomPool(new EpiphanyPendant(), CardColorEnum.Andoain_Blue);
        BaseMod.addRelicToCustomPool(new AuraRelic(), CardColorEnum.Andoain_Blue);
        BaseMod.addRelicToCustomPool(new ChurchMealVoucher(), CardColorEnum.Andoain_Blue);
        BaseMod.addRelicToCustomPool(new BrokenRevolverCylinder(),CardColorEnum.Andoain_Blue);
        BaseMod.addRelicToCustomPool(new CathedralPuzzle(),CardColorEnum.Andoain_Blue);
        // RelicType表示是所有角色都能拿到的遗物，还是一个角色的独有遗物
        new AutoAdd("AndoainTest") // 这里填写你在ModTheSpire.json中写的modid
                .packageFilter(EpiphanyPendant.class) // 寻找所有和此类同一个包及内部包的类（本例子是所有卡牌）
                .setDefaultSeen(true);
    }
    public void receiveEditEvents(){
        BaseMod.addEvent(GrandVision.ID, GrandVision.class);
    }
    /*     */   public void receiveEditPotions() {
        /* 120 */     BaseMod.addPotion(BottleDream.class, Color.CLEAR, (Color)null, (Color)null, "AndoainMod:BottleDream", PlayerEnum.MY_CHARACTER_Andoain);
        /* 121 */     BaseMod.addPotion(BottleLight.class, Color.CLEAR, null, null, "AndoainMod:BottleLight", PlayerEnum.MY_CHARACTER_Andoain);
        /* 122 */     BaseMod.addPotion(BottleAmmi.class, Color.CLEAR, (Color)null, (Color)null, "AndoainMod:BottleAmmi", PlayerEnum.MY_CHARACTER_Andoain);
        BaseMod.addPotion(IceCreamCoffee.class, Color.CLEAR, (Color)null, (Color)null, "AndoainMod:IceCreamCoffee", PlayerEnum.MY_CHARACTER_Andoain);
        BaseMod.addPotion(HotMilk.class, Color.CLEAR, (Color)null, (Color)null, "AndoainMod:HotMilk", PlayerEnum.MY_CHARACTER_Andoain);
        BaseMod.addPotion(MostimaPotion.class, Color.CLEAR, (Color)null, (Color)null, "AndoainMod:MostimaPotion", PlayerEnum.MY_CHARACTER_Andoain);
        BaseMod.addPotion(FiammettaPotion.class, Color.CLEAR, (Color)null, (Color)null, "AndoainMod:FiammettaPotion", PlayerEnum.MY_CHARACTER_Andoain);
        BaseMod.addPotion(AndoainPotion.class, Color.CLEAR, (Color)null, (Color)null, "AndoainMod:AndoainPotion", PlayerEnum.MY_CHARACTER_Andoain);
        BaseMod.addPotion(LemuenPotion.class, Color.CLEAR, (Color)null, (Color)null, "AndoainMod:LemuenPotion", PlayerEnum.MY_CHARACTER_Andoain);
        /*     */   }
    /*     */

    /*     */
    /*     */
    /*     */   public void receiveEditKeywords() {
        /*     */     String json;
        /* 174 */     BaseMod.logger.info("=========begin loading keywords============");
        /* 175 */     Gson gson = new Gson();
        /* 176 */     String lang = "eng";
        /* 177 */     if (Settings.language == Settings.GameLanguage.ZHS) {
            /* 178 */       lang = "zhs";
            /*     */     }
        /*     */
        /*     */
        /* 182 */     if (lang.equals("zhs")) {
            /*     */
            /* 184 */       json = Gdx.files.internal("AndoainResources/localization/ZHS/Keywords_" + lang + ".json").readString(String.valueOf(StandardCharsets.UTF_8));
            /*     */     }
        /*     */     else {
            /*     */
            /* 188 */       json = Gdx.files.internal("AndoainResources/localization/ENG/Keywords_" + lang + ".json").readString(String.valueOf(StandardCharsets.UTF_8));
            /*     */     }
        /* 190 */     Keyword[] keywords = (Keyword[])gson.fromJson(json, Keyword[].class);
        /* 191 */     if (keywords != null) {
            /* 192 */       for (Keyword keyword : keywords)
                /*     */       {
                /* 194 */         BaseMod.addKeyword("andoainmod", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
                /*     */       }
            /*     */     }
        /* 197 */     BaseMod.logger.info("=========finish loading keywords============");
        /*     */   } public void receiveEditStrings() {
        /*     */     String lang;
        /* 200 */     BaseMod.logger.info("=========begin loading strings============");
        /*     */
        /* 202 */     if (Settings.language == Settings.GameLanguage.ZHS) {
            /* 203 */       lang = "ZHS";
            /*     */     } else {
            /* 205 */       lang = "ENG";
            /*     */     }
        /* 207 */     BaseMod.loadCustomStringsFile(CardStrings.class, "AndoainResources/localization/" + lang + "/cards.json");
        /*     */
        /* 209 */     BaseMod.loadCustomStringsFile(CharacterStrings.class, "AndoainResources/localization/" + lang + "/characters.json");
        /* 210 */     BaseMod.loadCustomStringsFile(RelicStrings.class, "AndoainResources/localization/" + lang + "/relics.json");
        /*     */
        /* 212 */     BaseMod.loadCustomStringsFile(PowerStrings.class, "AndoainResources/localization/" + lang + "/powers.json");
        /* 213 */     BaseMod.loadCustomStringsFile(PowerStrings.class, "AndoainResources/localization/" + lang + "/ui.json");
                      BaseMod.loadCustomStringsFile(MonsterStrings.class, "AndoainResources/localization/" + lang + "/monsters.json");
                      BaseMod.loadCustomStringsFile(PotionStrings.class, "AndoainResources/localization/" + lang + "/potions.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, "AndoainResources/localization/" + lang + "/events.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "AndoainResources/localization/" + lang + "/UI-strings.json");
        /* 214 */     BaseMod.logger.info("=========finish loading strings============");        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */   public void receiveEditCharacters() {
        /* 220 */     BaseMod.addCharacter((AbstractPlayer)new andoain(CardCrawlGame.playerName), "AndoainResources/img/char/Character_Button.png", "AndoainResources/img/char/Character_Portrait.png", PlayerEnum.MY_CHARACTER_Andoain);
        /*     */  receiveEditPotions();
        receiveEditPotions();
        loadTutorialConfig();

    }
    public void receivePostInitialize() {

        // 公开的怪物注册方法
        BaseMod.addMonster(IceCreamMachine.ID, () -> new IceCreamMachine(0, 0));
        new GrandVisionPatch();
        unlockCards();
        BaseMod.logger.info("卡牌解锁完毕");
    }
    public static void saveTutorialStatus() {
        try {
            SpireConfig config = new SpireConfig("AndoainMod", "config");
            config.setBool(CONFIG_KEY, true);
            config.save();
            BaseMod.logger.info("Saved tutorial config");
        } catch (Exception e) {
            BaseMod.logger.error("Failed to save tutorial config", e);
        }
    }
    private void loadTutorialConfig() {
        try {
            SpireConfig config = new SpireConfig("AndoainMod", "config");
            iceCreamTutorialShown = config.getBool(CONFIG_KEY);
            BaseMod.logger.info("Loaded tutorial config: " + iceCreamTutorialShown);
        } catch (Exception e) {
            BaseMod.logger.error("Failed to load tutorial config", e);
        }
    }
    private static ArrayList<AbstractCard> getAllCardsToAdd() {
        ArrayList<AbstractCard> cards = new ArrayList<>();

        // 添加所有卡牌实例
        cards.add(new Strike());
        cards.add(new Defend());
        cards.add(new Lemuen());
        cards.add(new Eulogy());
        cards.add(new adlm4());

        return cards;
    }
    // 新增方法：解锁所有自定义卡牌
    private static void unlockCards() {
        BaseMod.logger.info("开始解锁卡牌！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
        for (AbstractCard c : getAllCardsToAdd()) {
            String cardId = c.cardID;
            AbstractCard card = CardLibrary.getCard(cardId);

            if (card != null && !card.isSeen) {
                card.isSeen = true;
                card.unlock();
                UnlockTracker.seenPref.putInteger(cardId, 1);
                BaseMod.logger.info("========================已解锁卡牌: {}", cardId);
            }
        }
        UnlockTracker.seenPref.flush();
    }

}
