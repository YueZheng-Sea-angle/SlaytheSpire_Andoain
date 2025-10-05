package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.actions.ConfessAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Confess extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Confess");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Confess");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int BASE_DAMAGE = 9;
    private static final int UPGRADE_DAMAGE = 13;
    private static final float BASE_CHANCE = 0.10f; // 10%
    private static final float UPGRADE_CHANCE = 0.99f; // 20%

    public Confess() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = BASE_DAMAGE;
        this.exhaust = true; // 添加消耗属性
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE - BASE_DAMAGE);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查是否需要转阶段
        if (p instanceof Andoain.character.andoain && !((Andoain.character.andoain)p).isSecondStage()) {
            Andoain.character.andoain.onRevive();
            float delay = Settings.FAST_MODE ? 0.4f : 0.8f;
            addToBot(new WaitAction(delay));
        }

        // 使用自定义动作处理伤害和奖励
        float chance = upgraded ? UPGRADE_CHANCE : BASE_CHANCE;
        addToBot(new ConfessAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), chance));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Confess();
    }
}