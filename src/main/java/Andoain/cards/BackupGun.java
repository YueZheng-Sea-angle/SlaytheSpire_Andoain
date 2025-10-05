package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.lwjgl.Sys;

public class BackupGun extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("BackupGun");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("BackupGun");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int DAMAGE = 7;
    private static final int UPGRADE_DAMAGE = 3;

    public BackupGun() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.hasRicochet = true;
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // 将全部逻辑放入一个顶层Action
        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                // 1. 确保弹药能力存在并补充1弹药
                if (!p.hasPower(AmmunitionPower.POWER_ID)) {
                    // 直接添加能力并初始化弹药（同步操作）
                    AmmunitionPower ammo = new AmmunitionPower(p, AmmunitionPower.MAX_AMMO, 1);
                    p.powers.add(ammo);
                    ammo.updateDescription();
                    System.out.println("[同步] 添加弹药能力，当前弹药: " + ammo.amount);
                } else {
                    // 补充弹药
                    AmmunitionPower ammo = (AmmunitionPower) p.getPower(AmmunitionPower.POWER_ID);
                    ammo.replenish(1);
                    System.out.println("[同步] 补充弹药，当前弹药: " + ammo.amount);
                }

                // 2. 播放攻击动画（如果有弹药）
                boolean hasAmmoNow = p.hasPower(AmmunitionPower.POWER_ID) &&
                        ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).amount > 0;
                if (hasAmmoNow) {
                    andoain.onAttack();
                    addToBot(new WaitAction(Settings.FAST_MODE ? 0.7f : 1.4f));
                }

                // 3. 造成主伤害
                addToTop(new DamageAction(
                        m,
                        new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                        AttackEffect.FIRE
                ));

                // 4. 强制触发弹射（此时弹药已存在）
                if (hasAmmoNow) {
                    System.out.println("[同步] 准备弹射，当前弹药: " +
                            ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).amount);
                    handleRicochet(p, m);
                }

                this.isDone = true;
            }
        });
    }}