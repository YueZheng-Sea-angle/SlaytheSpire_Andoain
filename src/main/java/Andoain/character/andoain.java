package Andoain.character;

import Andoain.cards.Strike;
import Andoain.modcore.AndoainMod;
import Andoain.patches.CardColorEnum;
import Andoain.patches.PlayerEnum;
import Andoain.relics.MassesTravels;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import java.util.ArrayList;

public class andoain extends CustomPlayer {
    private static final String MY_CHARACTER_SHOULDER_1 = "AndoainResources/img/char/shoulder1.png";
    private static final String MY_CHARACTER_SHOULDER_2 = "AndoainResources/img/char/shoulder2.png";
    private static final String[] ORB_TEXTURES = new String[] {
            "images/ui/topPanel/blue/1.png", "images/ui/topPanel/blue/2.png",
            "images/ui/topPanel/blue/3.png", "images/ui/topPanel/blue/4.png",
            "images/ui/topPanel/blue/5.png", "images/ui/topPanel/blue/border.png",
            "images/ui/topPanel/blue/1d.png", "images/ui/topPanel/blue/2d.png",
            "images/ui/topPanel/blue/3d.png", "images/ui/topPanel/blue/4d.png",
            "images/ui/topPanel/blue/5d.png" };

    private static final float[] LAYER_SPEED = new float[] {
            -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F };

    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("AndoainMod:Andoain");

    // 阶段状态
    private boolean isSecondStage = false;

    // 动画状态名称
    private static final String IDLE_1 = "Idle1";
    private static final String ATTACK_1 = "Attack1";
    private static final String IDLE_2 = "Idle2";
    private static final String ATTACK_2 = "Attack2";
    private static final String REVIVE_1 = "Revive_1";
    private static final String REVIVE_2 = "Revive_2";
    private static final String REVIVE_3 = "Revive_3";
    private static final String SKILL_BEGIN = "Skill_1_Begin";
    private static final String SKILL_LOOP = "Skill_1_Loop";
    private static final String SKILL_END = "Skill_1_End";
    private static final String DIE = "Die";

    // 当前阶段的空闲和攻击动画名称
    private String currentIdleAnim;
    private String currentAttackAnim;

    public andoain(String name) {
        super(name, PlayerEnum.MY_CHARACTER_Andoain, ORB_TEXTURES,
                "images/ui/topPanel/energyBlueVFX.png", LAYER_SPEED, null, null);

        this.dialogX = this.drawX + 0.0F * Settings.scale;
        this.dialogY = this.drawY + 150.0F * Settings.scale;

        initializeClass("AndoainResources/img/char/Character_Portrait2.jpg",
                "AndoainResources/img/char/shoulder2.png",
                "AndoainResources/img/char/shoulder1.png",
                null,
                getLoadout(), 0.0F, 0.0F, 200.0F, 220.0F, new EnergyManager(3));

        // 加载动画资源
        loadAnimation("AndoainResources/img/char/enemy_1527_martyr37.atlas",
                "AndoainResources/img/char/enemy_1527_martyr37.json", 1.2F);

        // 初始化阶段和动画
        initializeFirstStage();
    }

    // 初始化第一阶段
    private void initializeFirstStage() {
        isSecondStage = false;
        currentIdleAnim = IDLE_1;
        currentAttackAnim = ATTACK_1;
        playIdleAnimation();
    }

    // 初始化第二阶段
    private void initializeSecondStage() {
        isSecondStage = true;
        currentIdleAnim = IDLE_2;
        currentAttackAnim = ATTACK_2;
    }

    // 播放空闲动画
    private void playIdleAnimation() {
        AnimationState.TrackEntry e = this.state.setAnimation(0, currentIdleAnim, true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(1.2F);
    }

    // 播放攻击动画
    public void playAttackAnimation() {
        if (Settings.FAST_MODE) {
            this.state.setTimeScale(1.5F);
        } else {
            this.state.setTimeScale(1.0F);
        }
        this.state.setAnimation(0, currentAttackAnim, false);
        this.state.addAnimation(0, currentIdleAnim, true, 0.0F);
    }

    public void playReviveAnimation() {
        // 只有当前处于第一阶段时才播放转阶段动画
        if (!isSecondStage) {
            this.state.setTimeScale(1.0F);
            this.state.setAnimation(0, REVIVE_1, false);
            this.state.addAnimation(0, REVIVE_2, false, 0.0F);
            this.state.addAnimation(0, REVIVE_3, false, 0.0F);
            this.state.addAnimation(0, IDLE_2, true, 0.0F);
            // 切换到第二阶段
            isSecondStage = true;
            currentIdleAnim = IDLE_2;
            currentAttackAnim = ATTACK_2;
        }
    }
    // 播放技能动画
    public void playSkillAnimation(int loopCount) {
        this.state.setTimeScale(1.0F);
        this.state.setAnimation(0, SKILL_BEGIN, false);

        // 添加循环部分
        for (int i = 0; i < loopCount; i++) {
            this.state.addAnimation(0, SKILL_LOOP, false, 0.0F);
        }

        // 结束部分并返回空闲状态
        this.state.addAnimation(0, SKILL_END, false, 0.0F);
        this.state.addAnimation(0, currentIdleAnim, true, 0.0F);
    }

    // 播放死亡动画
    public void playDeathAnimation() {
        this.state.setTimeScale(1.0F);
        this.state.setAnimation(0, DIE, false);
    }

    // 获取当前阶段状态
    public boolean isSecondStage() {
        return isSecondStage;
    }

    // 重置到第一阶段
    public void resetToFirstStage() {
        // System.out.println("[Andoain] resetToFirstStage called. Current stage: " + (isSecondStage ? "Stage 2" : "Stage 1"));
        // if (this.state != null && this.state.getCurrent(0) != null) {
        //     System.out.println("[Andoain] Current animation: " + this.state.getCurrent(0).getAnimation().getName());
        // }
        
        // 无条件重置，确保Spine动画状态也被正确重置
        isSecondStage = false;
        currentIdleAnim = IDLE_1;
        currentAttackAnim = ATTACK_1;
        
        // 强制清除所有动画轨道和骨骼状态
        if (this.state != null && this.skeleton != null) {
            this.state.clearTracks();
            
            // 重置骨骼到初始姿势
            this.skeleton.setToSetupPose();
            
            // 重新播放第一阶段空闲动画
            AnimationState.TrackEntry e = this.state.setAnimation(0, IDLE_1, true);
            if (e != null) {
                e.setTime(e.getEndTime() * MathUtils.random());
                e.setTimeScale(1.2F);
            }
            
            // 强制更新一次动画状态
            this.state.update(0);
            this.state.apply(this.skeleton);
        }
        
        // System.out.println("[Andoain] Reset complete. New stage: Stage 1, Animation: " + IDLE_1);
    }
    public void damage(DamageInfo info) {
    int thisHealth = this.currentHealth;
    super.damage(info);
    int trueDamage = thisHealth - this.currentHealth;
    if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && trueDamage > 0 && this.isDead) {
    playDeathAnimation();}}
    // 静态方法以便在其他地方调用攻击动画
    public static void onAttack() {
        AbstractPlayer pl = AbstractDungeon.player;
        if (pl instanceof andoain) {
            ((andoain)pl).playAttackAnimation();
        }
    }

    public static void onRevive() {
        AbstractPlayer pl = AbstractDungeon.player;
        if (pl instanceof andoain) {
            ((andoain)pl).playReviveAnimation();
        }
    }

    // 静态方法以便在其他地方调用技能
    public static void onSkill(int loopCount) {
        AbstractPlayer pl = AbstractDungeon.player;
        if (pl instanceof andoain) {
            ((andoain)pl).playSkillAnimation(loopCount);
        }
    }

    // 静态方法以便在其他地方调用死亡
    public static void onDeath() {
        AbstractPlayer pl = AbstractDungeon.player;
        if (pl instanceof andoain) {
            ((andoain)pl).playDeathAnimation();
        }
    }

    // 静态方法以便在其他地方调用重置到第一阶段
    public static void onResetToFirstStage() {
        AbstractPlayer pl = AbstractDungeon.player;
        if (pl instanceof andoain) {
            ((andoain)pl).resetToFirstStage();
        }
    }

    // 其他原有方法保持不变...
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add("AndoainMod:Iris");
        retVal.add("AndoainMod:Defend");
        retVal.add("AndoainMod:Defend");
        retVal.add("AndoainMod:Defend");
        retVal.add("AndoainMod:Defend");
        retVal.add("AndoainMod:Defend");
        retVal.add("AndoainMod:Strike");
        retVal.add("AndoainMod:Strike");
        retVal.add("AndoainMod:Strike");
        retVal.add("AndoainMod:Strike");
        retVal.add("AndoainMod:Strike");
        retVal.add("AndoainMod:Prayer");
        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(MassesTravels.ID);
        return retVal;
    }

    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(characterStrings.NAMES[0], characterStrings.TEXT[0],
                60, 60, 1, 99, 5, (AbstractPlayer)this,
                getStartingRelics(), getStartingDeck(), false);
    }

    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return characterStrings.NAMES[0];
    }

    public AbstractCard.CardColor getCardColor() {
        return CardColorEnum.Andoain_Blue;
    }

    public AbstractCard getStartCardForEvent() {
        return (AbstractCard)new Strike();
    }

    public Color getCardTrailColor() {
        return AndoainMod.MY_COLOR;
    }

    public int getAscensionMaxHPLoss() {
        return 5;
    }

    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED,
                ScreenShake.ShakeDur.SHORT, false);
    }

    public ArrayList<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel("AndoainResources/img/char/Victory1.png",
                "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel("AndoainResources/img/char/Victory2.png"));
        panels.add(new CutscenePanel("AndoainResources/img/char/Victory3.png"));
        return panels;
    }

    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    public AbstractPlayer newInstance() {
        return (AbstractPlayer)new andoain(this.name);
    }

    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    public Color getSlashAttackColor() {
        return AndoainMod.MY_COLOR;
    }

    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    public Color getCardRenderColor() {
        return AndoainMod.MY_COLOR;
    }

    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL };
    }
}