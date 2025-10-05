package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class TrialAndError extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("TrialAndError");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int BASE_MULTIPLIER = 1;
    private int currentMultiplier;

    public TrialAndError() {
        super(
                ID,
                CARD_STRINGS.NAME,
                "AndoainResources/img/cards/TrialAndError.png",
                2,
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.UNCOMMON,
                CardTarget.SELF
        );
        this.currentMultiplier = BASE_MULTIPLIER;
        this.exhaust = true;
        this.updateDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(1);
            this.updateDescription();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        TrialAndError card = (TrialAndError) super.makeStatEquivalentCopy();
        card.currentMultiplier = this.currentMultiplier; // 复制当前倍数
        return card;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 如果有弹药能力，应用效果并递增倍数
        if (p.hasPower(AmmunitionPower.POWER_ID)) {
            AmmunitionPower ammo = (AmmunitionPower) p.getPower(AmmunitionPower.POWER_ID);

            // 获得力量（基于当前倍数）
            int strengthGain = ammo.amount * currentMultiplier;
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, strengthGain), strengthGain));

            // 安全削减弹药上限（不低于0）
            int newMax = Math.max(0, ammo.currentMax - 1);
            ammo.setMax(newMax);

            // 耗尽弹药

            // 创建递增倍数的副本
            TrialAndError newCard = (TrialAndError) this.makeStatEquivalentCopy();
            newCard.currentMultiplier = currentMultiplier + 1; // 倍数+1
            newCard.initializeDescription();

            // 延迟添加到牌堆
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractDungeon.player.discardPile.removeCard(TrialAndError.this);
                    AbstractDungeon.actionManager.addToBottom(
                            new MakeTempCardInDrawPileAction(newCard, 1, true, true));
                    this.isDone = true;
                }
            });
        } else {
            // 无弹药能力时，创建原版副本（不递增倍数）
            TrialAndError newCard = (TrialAndError) this.makeStatEquivalentCopy();
            newCard.initializeDescription();

            // 延迟添加到牌堆
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractDungeon.player.discardPile.removeCard(TrialAndError.this);
                    AbstractDungeon.actionManager.addToBottom(
                            new MakeTempCardInDrawPileAction(newCard, 1, true, true));
                    this.isDone = true;
                }
            });
        }
    }
    @Override
    public void applyPowers() {
        if (AbstractDungeon.player != null &&
                AbstractDungeon.player.hasPower(AmmunitionPower.POWER_ID)) {
            this.baseMagicNumber = AbstractDungeon.player.getPower(AmmunitionPower.POWER_ID).amount * currentMultiplier;
        } else {
            this.baseMagicNumber = 0;
        }
        super.applyPowers();
        this.updateDescription();
    }
    
    private void updateDescription() {
        this.rawDescription = (upgraded ? CARD_STRINGS.UPGRADE_DESCRIPTION : CARD_STRINGS.DESCRIPTION);
        
        // 只有当前倍数大于1时才显示扩展描述
        if (currentMultiplier > 1) {
            if (CARD_STRINGS.EXTENDED_DESCRIPTION != null && CARD_STRINGS.EXTENDED_DESCRIPTION.length > 0) {
                this.rawDescription += String.format(CARD_STRINGS.EXTENDED_DESCRIPTION[0], currentMultiplier);
            }
        }
        
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = (upgraded ? CARD_STRINGS.UPGRADE_DESCRIPTION : CARD_STRINGS.DESCRIPTION);
        this.initializeDescription();
    }}