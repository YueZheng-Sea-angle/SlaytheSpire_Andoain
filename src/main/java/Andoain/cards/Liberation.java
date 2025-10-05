package Andoain.cards;

import Andoain.character.andoain;
import Andoain.actions.LiberationAction;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Liberation extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Liberation");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Liberation");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    public static final int BASE_DAMAGE = 8;
    public static final int DAMAGE_PER_UPGRADE = 4;

    public Liberation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = BASE_DAMAGE + this.misc; // 使用misc存储永久伤害加成
        this.shuffleBackIntoDrawPile = true;
        this.updateName(); // 初始化时更新名称
        ExhaustiveVariable.setBaseValue(this, 3); // 设置单次战斗最多使用3次
    }

    // 确保名称根据升级次数更新
    private void updateName() {
        if (this.timesUpgraded > 0) {
            this.name = NAME + "+" + this.timesUpgraded;
        } else {
            this.name = NAME;
        }
        this.initializeTitle();
    }

    // 处理百科全书中可能出现的空指针
    @Override
    public void applyPowers() {
        // 非战斗环境中避免计算
        if (AbstractDungeon.player == null) {
            this.baseDamage = BASE_DAMAGE + this.misc;
        } else {
            super.applyPowers();
        }
        this.initializeDescription();
    }

    // 支持无限升级
    @Override
    public boolean canUpgrade() {
        return true;
    }

    @Override
    public void upgrade() {
        this.misc += DAMAGE_PER_UPGRADE; // 永久伤害加成存储在misc
        this.timesUpgraded++;
        this.baseDamage = BASE_DAMAGE + this.misc; // 更新基础伤害
        this.updateName(); // 升级时更新名称
        // 调用父类方法设置upgraded标志
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onAttack();
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
            }
        });

        // 使用自定义Action处理伤害和升级
        addToBot(new LiberationAction(
                m,
                new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                this.uuid
        ));
    }

    @Override
    public AbstractCard makeCopy() {
        Liberation copy = new Liberation();
        copy.misc = this.misc; // 复制misc（永久伤害加成）
        copy.timesUpgraded = this.timesUpgraded; // 复制升级次数
        copy.baseDamage = BASE_DAMAGE + copy.misc; // 设置基础伤害
        copy.updateName(); // 复制时更新名称
        return copy;
    }
}