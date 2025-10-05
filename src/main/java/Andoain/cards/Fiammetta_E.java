package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Fiammetta_E extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Fiammetta_E");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Fiammetta");
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL;

    private static final int BASE_DAMAGE = 27;
    private static final int UPGRADE_DAMAGE = 35;
    private static final int SELF_DAMAGE = 8;
    private AbstractCard previewCard;
    private float previewTimer = 0.0F;
    private static final float PREVIEW_INTERVAL = 3.0F; // 3秒预览一次

    public Fiammetta_E() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.damage = this.baseDamage = BASE_DAMAGE;
        this.magicNumber = this.baseMagicNumber = SELF_DAMAGE;
        this.isMultiDamage = true;
        this.isEthereal = true;
        this.exhaust = true;
        this.previewCard = new YouMustRepay();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE - BASE_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 0.5f : 1.0f;

        // 群体伤害
        addToBot(new DamageAllEnemiesAction(
                (AbstractCreature)p,
                this.multiDamage,
                DamageInfo.DamageType.NORMAL,
                AbstractGameAction.AttackEffect.FIRE
        ));

        // 自伤效果
        AbstractCard debtCard = new YouMustRepay();
        addToBot(new MakeTempCardInHandAction(debtCard, 1));

        addToBot(new WaitAction(delay));
    }
    @Override
    public void update(){
        super.update();
        // 只在悬停时显示预览
        if (this.hb.hovered) {
            if (previewTimer <= 0.0F) {
                previewTimer = PREVIEW_INTERVAL;
                this.cardsToPreview = previewCard;
            } else {
                previewTimer -= Gdx.graphics.getDeltaTime();
            }
        } else {
            // 不悬停时重置预览和计时器
            previewTimer = 0.0F;
            this.cardsToPreview = null;
        }
}}