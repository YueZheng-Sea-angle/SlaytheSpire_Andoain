package Andoain.monster;

import Andoain.actions.SafeApplyPowerAction;
import Andoain.actions.ShowIceCreamTutorialAction;
import Andoain.actions.SummonIceCreamMachineAction;
import Andoain.helpers.ModHelper;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.AutoRefrigerationPower;
import Andoain.powers.DamageLinkPower;
import Andoain.powers.EchoPower;
import Andoain.relics.CathedralPuzzle;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import Andoain.cards.AutoRefrigeration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.awt.SystemColor.info;

public class IceCreamMachine extends AbstractAndoainMonster {
    public boolean isFriendlyMinion = false;
    public static final String ID = "Andoain:IceCreamMachine";
    private static MonsterStrings monsterStrings =
            CardCrawlGame.languagePack.getMonsterStrings(ID);
    private boolean hadhandle = false;
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        BaseMod.logger.info("Loaded monster strings for " + ID + ": " +
                (monsterStrings != null ? monsterStrings.NAME : "NULL"));
    }

    private boolean isAlly = false;
    private static final int MAX_HP = 20;
    private float rotation = 0f; // 添加旋转角度字段

    private static final String RED_IMG = "AndoainResources/img/monsters/IceCreamMachine_Red.png";
    private static final String BLUE_IMG = "AndoainResources/img/monsters/IceCreamMachine_Blue.png";

    public IceCreamMachine(float x, float y) {

        super(monsterStrings.NAME, ID, MAX_HP,
                -50.0f, -20.0f, 180.0f, 180.0f,
                RED_IMG,
                x, y);
        // 新增构造函数检查
        if (hasOtherActiveMachine()) {
            return;
            //throw new RuntimeException("已存在激活的冰淇淋机，禁止重复创建");
        }

        this.type = EnemyType.NORMAL;
        this.isAlly = false;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.addPower(new DescriptionPower());
    }

    private void updateImage() {
        if (isAlly) {
            this.img = ImageMaster.loadImage(BLUE_IMG);
        } else {
            this.img = ImageMaster.loadImage(RED_IMG);
        }
    }


    // 修改后的setRotation方法
    public void setRotation(float rotation) {
        this.rotation = rotation;
        // 移除了直接操作Texture的代码
    }


    @Override
    public void heal(int healAmount) {
        int prevHealth = this.currentHealth;
        super.heal(healAmount);

        if (currentHealth > prevHealth) {
            int actualHeal = currentHealth - prevHealth;
            BaseMod.logger.info("IceCreamMachine healed for " + actualHeal +
                    ", triggering EchoPower.HEAL effect");
            EchoPower.onIceCreamMachineEffect(this, actualHeal, EchoPower.EffectType.HEAL);
        } else {
            BaseMod.logger.debug("Heal attempt but no health gained (prev:" + prevHealth +
                    ", current:" + currentHealth + ")");
        }
    }

    @Override
    public void addBlock(int blockAmount) {
        int originalBlock = this.currentBlock;
        super.addBlock(blockAmount);

        if (this.currentBlock > originalBlock) {
            int actualBlock = this.currentBlock - originalBlock;
            BaseMod.logger.info("IceCreamMachine gained " + actualBlock +
                    " block, triggering EchoPower.BLOCK effect");
            EchoPower.onIceCreamMachineEffect(this, actualBlock, EchoPower.EffectType.BLOCK);
        } else {
            BaseMod.logger.debug("Block attempt but no block gained (prev:" + originalBlock +
                    ", current:" + currentBlock + ")");
        }
    }


    // 修改后的render方法
    @Override
    public void render(SpriteBatch sb) {
        if (this.img != null) {
            sb.draw(this.img,
                    this.drawX - this.img.getWidth() / 2f,
                    this.drawY - this.img.getHeight() / 2f,
                    this.img.getWidth() / 2f,
                    this.img.getHeight() / 2f,
                    this.img.getWidth(),
                    this.img.getHeight(),
                    Settings.scale, // 使用Settings.scale替代this.scale
                    Settings.scale
            );
        }
        super.render(sb);
    }

    @Override
    public void update() {
        super.update();
        
        // 处理右键点击显示提示
        if (this.hb.hovered && InputHelper.justClickedRight && !this.isDeadOrEscaped()) {
            showTutorial();
        }
    }

    private void showTutorial() {
        // 播放点击音效
        CardCrawlGame.sound.play("UI_CLICK_1");
        
        // 显示完整的冰淇淋机提示教程
        AbstractDungeon.actionManager.addToTop(
                new ShowIceCreamTutorialAction(this, 2)
        );
        AbstractDungeon.actionManager.addToTop(
                new ShowIceCreamTutorialAction(this, 1)
        );
        AbstractDungeon.actionManager.addToTop(
                new ShowIceCreamTutorialAction(this, 0)
        );
    }
    private boolean shouldSummonNewVersion() {
        return AbstractDungeon.getMonsters().monsters.stream()
                .anyMatch(m ->
                        !m.isDeadOrEscaped() &&  // 未死亡或逃跑
                                !(m instanceof IceCreamMachine) &&  // 不是冰淇淋机
                                (!m.halfDead || isAwakenedOne(m))  // 允许半死的觉醒者
                );
    }

    // 辅助方法：判断是否是觉醒者
    private boolean isAwakenedOne(AbstractMonster m) {
        return m.id != null && m.id.equals("AwakenedOne");  // 使用官方的怪物ID
    }
    private void handleEscapeBehavior() {
        this.setMove((byte)99, Intent.ESCAPE);
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                // 直接移除单位
                IceCreamMachine.this.escape();
                isDone = true;
            }
        });
    }
    @Override
    public void takeTurn() {
        // 添加关键安全检查
        if (AbstractDungeon.getMonsters() == null ||
                AbstractDungeon.getMonsters().monsters == null ||
                AbstractDungeon.player.isDeadOrEscaped()) {
            return;
        }
        if (!hasOtherEnemies()) {
            handleEscapeBehavior();
            return;
        }
        if (isAlly) {
            // 简化后的友方行为：抽牌+弹药
            addToBot(new DrawCardAction(AbstractDungeon.player, 1));

            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractPlayer p = AbstractDungeon.player;
                    addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, 4)));
                    // 1. 确保弹药能力存在
                    if (!p.hasPower(AmmunitionPower.POWER_ID)) {
                        addToTop(new ApplyPowerAction(p, p, new AmmunitionPower(p)));
                    }

                    // 2. 补充1弹药
                    AmmunitionPower ammo = (AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID);
                    if (ammo != null) {
                        ammo.replenish(1);
                        // 视觉反馈
                        ammo.flash();
                    }
                    isDone = true;
                }
            });
        }else {
            if (!this.hasPower("BuffAppliedThisTurn")) {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != null && m != this && !m.isDeadOrEscaped()) {
                        addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, 1)));
                        addToBot(new ApplyPowerAction(m, this, new MetallicizePower(m, 2)));
                    }
                }

                addToTop(new ApplyPowerAction(this, this,
                        new AbstractPower() {
                            public static final String POWER_ID = "BuffAppliedThisTurn";
                            {
                                this.ID = POWER_ID;
                                this.name = "拉特兰冰淇淋";
                                this.owner = IceCreamMachine.this;
                                this.type = PowerType.BUFF;
                                this.region48 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(
                                        ImageMaster.loadImage("AndoainResources/img/powers/LightUntoSufferers32.jpg"), 0, 0, 32, 32);
                                this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(
                                        ImageMaster.loadImage("AndoainResources/img/powers/LightUntoSufferers84.jpg"), 0, 0, 84, 84);
                                updateDescription();
                            }
                            @Override
                            public void updateDescription() {
                                this.description = "制冷完成";
                            }
                        }, 1));
            }
        }
    }

    @Override
    public void damage(DamageInfo info) {

        int prevHealth = this.currentHealth;

        // 如果是友方单位且伤害来自orb，处理伤害转移
        if (isAlly && info.type == DamageInfo.DamageType.THORNS) {
            BaseMod.logger.info("Transform damage.");//
            AbstractMonster weakestMonster = null;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped() && !(m instanceof IceCreamMachine)) {
                    if (weakestMonster == null) {
                        weakestMonster = m;
                    } else if (m.currentHealth < weakestMonster.currentHealth) {
                        weakestMonster = m;
                    }
                }
            }

            if (weakestMonster != null) {
                int damageAmount = info.output;
                weakestMonster.damage(new DamageInfo(info.owner, damageAmount, DamageInfo.DamageType.THORNS));
                BaseMod.logger.info("Redirected " + damageAmount + " orb damage to weakest monster");
            }
            return; // 友方单位不受orb伤害
        }
        if (info.owner != null && info.owner.isPlayer &&
                AbstractDungeon.player.hasRelic(CathedralPuzzle.ID) && info.type == DamageInfo.DamageType.NORMAL) {
            // 免疫伤害
            BaseMod.logger.info("Cathedral！");
            return;
        }
        // 正常处理其他伤害
        super.damage(info);

        BaseMod.logger.info("Damage taken: " + (prevHealth - currentHealth) +
                ", isAlly: " + isAlly + ", owner isPlayer: " +
                (info.owner != null && info.owner.isPlayer));
        if (prevHealth > currentHealth && info.type == DamageInfo.DamageType.NORMAL) {
            int damageAmount = prevHealth - currentHealth;
            EchoPower.onIceCreamMachineEffect(this, damageAmount, EchoPower.EffectType.DAMAGE);
        }
        if (this.currentHealth <= 0) {
            if (isAlly) {
                if(!this.hadhandle){
                    this.hadhandle = true;
                    handleAllyDeath();
                }
            } else {
                if(!this.hadhandle){
                    this.hadhandle = true;
                    handleEnemyDeath(info);
                }
            }
        }
        else if (isAlly && info.owner != null && !info.owner.isPlayer) {
            int damageTaken = (prevHealth - this.currentHealth);
            int damageToTake = (int)(damageTaken * 0.5f);
            this.currentHealth = Math.max(1, this.currentHealth - damageToTake);
            this.healthBarUpdatedEvent();
            BaseMod.logger.info("Shared damage: " + damageToTake +
                    ", new health: " + currentHealth);
        }
    }

    private void handleEnemyDeath(DamageInfo info) {
        boolean exists = AbstractDungeon.getMonsters().monsters.stream()
                .filter(m -> m != this)
                .anyMatch(m -> m instanceof IceCreamMachine && !m.isDeadOrEscaped());

        if (!exists && shouldSummonNewVersion()) {
            addToBot(new SummonIceCreamMachineAction(
                    this.drawX,
                    this.drawY,
                    true,
                    true,// 召唤友方版本,
                    false,
                    true // 添加无实体

            ));
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (AbstractDungeon.player != null && !AbstractDungeon.player.isDeadOrEscaped()) {
                    addToTop(new ApplyPowerAction(
                            AbstractDungeon.player,
                            AbstractDungeon.player, // 使用玩家自身作为source
                            new DamageLinkPower(AbstractDungeon.player),
                            1
                    ));
                }
                isDone = true;
            }
        });
        // AOE伤害逻辑
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractCreature damageSource = (info != null && info.owner != null) ?
                        info.owner : IceCreamMachine.this;

                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != IceCreamMachine.this && !m.isDeadOrEscaped()&& !(m instanceof IceCreamMachine)) {
                        m.damage(new DamageInfo(damageSource, 10));
                    }
                }
                CardCrawlGame.sound.play("ATTACK_FIRE");
                isDone = true;
            }
        });

        // 眩晕其他敌人
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != this) {
                addToBot(new ApplyPowerAction(m, this, new StunMonsterPower(m, 1)));
            }
        }
    }

    private void handleAllyDeath() {
        boolean exists = AbstractDungeon.getMonsters().monsters.stream()
                .filter(m -> m != this)
                .anyMatch(m -> m instanceof IceCreamMachine && !m.isDeadOrEscaped());
        boolean hasAutoRefrigeration = AbstractDungeon.player.hasPower(AutoRefrigerationPower.POWER_ID);
        if (!exists && shouldSummonNewVersion()) {
            addToBot(new SummonIceCreamMachineAction(
                    this.drawX,
                    this.drawY,
                    false,
                    false,
                    false
            ));
        }
        // 安全移除伤害链接
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (AbstractDungeon.player != null) {
                    AbstractDungeon.player.powers.removeIf(p ->
                            p instanceof DamageLinkPower
                    );
                }
                isDone = true;
            }
        });
        if(!hasAutoRefrigeration){
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(
                        new VoidCard(), true, true));
                AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(
                        new VoidCard(), true, true));
                isDone = true;
            }
        });

        addToBot(new ApplyPowerAction(
                AbstractDungeon.player, this,
                new VulnerablePower(AbstractDungeon.player, 1, false)
        ));
    }}
    private enum MachineIntent {
        BUFF, ESCAPE
    }
    private boolean hasOtherEnemies() {
        return AbstractDungeon.getMonsters().monsters.stream()
                .anyMatch(m ->
                        !m.isDeadOrEscaped() &&
                                !(m instanceof IceCreamMachine)
                );
    }
    @Override
    protected void getMove(int num) {
        setMove((byte)(isAlly ? 1 : 0),
                Intent.BUFF,
                -1);
    }

    @Override
    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();
        this.powers.removeIf(p -> p.ID.equals("BuffAppliedThisTurn"));
    }

    @Override
    public boolean isDeadOrEscaped() {
        return super.isDeadOrEscaped() || (this.currentHealth <= 0 && !isAlly);
    }

    public boolean isAlly() {
        return this.isAlly;
    }
    public void setAlly(boolean isAlly) {
        this.isAlly = isAlly;
        this.updateImage();
    }
    private class DescriptionPower extends AbstractPower {
        public static final String POWER_ID = "IceCreamMachine_Description";

        public DescriptionPower() {
            this.ID = POWER_ID;
            this.name = ""; // 名称留空，只显示描述
            this.owner = IceCreamMachine.this;
            this.type = PowerType.BUFF;
            this.isTurnBased = false;

            // 使用透明贴图或留空
            this.region48 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(
                    ImageMaster.loadImage("AndoainResources/img/powers/LightUntoSufferers32.jpg"), 0, 0, 32, 32);
            this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(
                    ImageMaster.loadImage("AndoainResources/img/powers/LightUntoSufferers84.jpg"), 0, 0, 84, 84);
            updateDescription();
        }

        @Override
        public void updateDescription() {
            IceCreamMachine machine = (IceCreamMachine) owner;
            int index = machine.isAlly() ? 1 : 0;
            this.description = monsterStrings.MOVES[index];
        }


    }
    private boolean hasOtherActiveMachine() {
        return AbstractDungeon.getMonsters().monsters.stream()
                .anyMatch(m ->
                        m instanceof IceCreamMachine &&
                                !m.isDeadOrEscaped() &&
                                m != this // 排除自身检查（如果已加入列表）
                );

}}