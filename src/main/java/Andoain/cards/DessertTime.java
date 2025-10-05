package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DessertTime extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("DessertTime");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("DessertTime");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    // 使用自定义字段代替魔法数值属性
    private int playerHeal;
    private int machineHeal;

    public DessertTime() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.playerHeal = 4;
        this.machineHeal = 10;
        this.exhaust = true; // 消耗属性
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.playerHeal = 5; // 玩家治疗4→5
            this.machineHeal = 13; // 冰淇淋机治疗10→13
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 0.5f : 1.0f;
        addToBot(new WaitAction(delay));

        // 1. 治疗玩家
        addToBot(new HealAction(p, p, this.playerHeal));

        // 2. 获取场上唯一的冰淇淋机（如果有的话）
        IceCreamMachine machine = getFriendlyIceCreamMachine();

        // 3. 如果是友方冰淇淋机则进行治疗
        if (machine != null) {
            addToBot(new HealAction(machine, p, this.machineHeal));
        }
    }

    // 获取场上唯一的友方冰淇淋机（最多只有一个）
    private IceCreamMachine getFriendlyIceCreamMachine() {
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped() && mon instanceof IceCreamMachine) {
                IceCreamMachine machine = (IceCreamMachine) mon;
                if (machine.isAlly()) {
                    return machine;
                }
            }
        }
        return null;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        // 更新描述文本 - 简化版本
        IceCreamMachine machine = getFriendlyIceCreamMachine();
        this.rawDescription = upgraded ?
                CARD_STRINGS.UPGRADE_DESCRIPTION :
                CARD_STRINGS.DESCRIPTION;

        // 手动替换占位符
        this.rawDescription = this.rawDescription
                .replace("!M!", String.valueOf(this.playerHeal))
                .replace("!M2!", String.valueOf(this.machineHeal))
                .replace("!HAS_FRIENDLY!", machine != null ? "(是)" : "(否)");

        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = upgraded ?
                CARD_STRINGS.UPGRADE_DESCRIPTION :
                CARD_STRINGS.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new DessertTime();
    }
}