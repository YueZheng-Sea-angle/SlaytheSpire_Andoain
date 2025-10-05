package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Funeral extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Funeral");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Funeral");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private final int doubtCards = 1;

    public Funeral() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 22; // 基础伤害22，升级后28
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(6);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害 - 使用计算后的伤害而不是固定伤害
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        ));

        // 添加回调检查是否斩杀
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                // 检查敌人是否存活
                if (!m.isDeadOrEscaped()) {
                    // 未斩杀，洗入疑虑卡
                    addToTop(new MakeTempCardInDiscardAction(new Doubt(), doubtCards));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        // 更新描述文本
        this.rawDescription = upgraded ?
                CARD_STRINGS.UPGRADE_DESCRIPTION :
                CARD_STRINGS.DESCRIPTION;
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
        return new Funeral();
    }
}