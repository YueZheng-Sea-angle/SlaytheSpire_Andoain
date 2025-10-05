package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Adaptation extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Adaptation");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Adaptation");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    private static final int BASE_BLOCK_PER_ATTACK = 3;
    private static final int UPGRADE_BLOCK_PER_ATTACK = 1;
    private static final int LIGHT_STACKS = 1;
    private static final int UPGRADE_LIGHT_STACKS = 1;

    public Adaptation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = LIGHT_STACKS;
        this.block = this.baseBlock = BASE_BLOCK_PER_ATTACK;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK_PER_ATTACK);
            upgradeMagicNumber(UPGRADE_LIGHT_STACKS);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 0.5f : 1.0f;
        addToBot(new WaitAction(delay));

        // 统计攻击意图的敌人数量
        int attackIntentCount = 0;
        boolean hasNonAttackIntent = false;

        // 遍历所有敌人
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.isDeadOrEscaped()) continue;

            // 检查意图类型
            if (isAttackIntent(monster.intent)) {
                attackIntentCount++;
            } else {
                hasNonAttackIntent = true;
            }
        }

        // 1. 为每个攻击意图的敌人获得格挡
        int totalBlock = attackIntentCount * (BASE_BLOCK_PER_ATTACK + (upgraded ? UPGRADE_BLOCK_PER_ATTACK : 0));
        if (totalBlock > 0) {
            addToBot(new GainBlockAction(p, p, totalBlock));
        }

        // 2. 如果有敌人是非攻击意图，获得能量和光赐于苦
        if (hasNonAttackIntent) {
            addToBot(new GainEnergyAction(1));
            addToBot(new ApplyPowerAction(
                    p, p,
                    new LightUntoSufferers(p, this.magicNumber),
                    this.magicNumber
            ));
        }
    }

    // 判断是否为攻击意图
    private boolean isAttackIntent(Intent intent) {
        return intent == Intent.ATTACK ||
                intent == Intent.ATTACK_BUFF ||
                intent == Intent.ATTACK_DEBUFF ||
                intent == Intent.ATTACK_DEFEND;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        // 动态更新描述文本
        int attackIntentCount = 0;
        boolean hasNonAttackIntent = false;

        // 遍历所有敌人
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.isDeadOrEscaped()) continue;

            // 检查意图类型
            if (isAttackIntent(monster.intent)) {
                attackIntentCount++;
            } else {
                hasNonAttackIntent = true;
            }
        }


        initializeDescription();
    }


    @Override
    public AbstractCard makeCopy() {
        return new Adaptation();
    }
}