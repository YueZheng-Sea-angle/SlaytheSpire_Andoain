package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RecursiveSolution extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("RecursiveSolution");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int BASE_MULTIPLIER = 4;
    private static final int UPGRADE_MULTIPLIER = 6;
    private int currentMultiplier;

    public RecursiveSolution() {
        super(
                ID,
                CARD_STRINGS.NAME,
                "AndoainResources/img/cards/RecursiveSolution.png",
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
            this.currentMultiplier = UPGRADE_MULTIPLIER;
            this.updateDescription();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        RecursiveSolution card = (RecursiveSolution) super.makeStatEquivalentCopy();
        card.currentMultiplier = this.currentMultiplier; // 复制当前倍数
        return card;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 如果有弹药能力，应用效果
        if (p.hasPower(AmmunitionPower.POWER_ID)) {
            AmmunitionPower ammo = (AmmunitionPower) p.getPower(AmmunitionPower.POWER_ID);
            int blockAmount = ammo.amount * currentMultiplier;

            // 获得格挡
            if (blockAmount > 0) {
                addToBot(new GainBlockAction(p, p, blockAmount));
            }

            // 补满弹药
            ammo.replenish(ammo.currentMax);
        }

        // 无论是否有弹药能力，都创建递减倍数的副本
        RecursiveSolution newCard = (RecursiveSolution) this.makeStatEquivalentCopy();
        newCard.currentMultiplier = Math.max(1, currentMultiplier - 1); // 确保最小为1
        newCard.initializeDescription();

        // 延迟添加到牌堆
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.player.discardPile.removeCard(RecursiveSolution.this);
                AbstractDungeon.actionManager.addToBottom(
                        new MakeTempCardInDrawPileAction(newCard, 1, true, true));
                this.isDone = true;
            }
        });
    }
    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.hasPower(AmmunitionPower.POWER_ID)) {
            this.baseBlock = AbstractDungeon.player.getPower(AmmunitionPower.POWER_ID).amount * currentMultiplier;
        } else {
            this.baseBlock = 0;
        }
        super.applyPowers();
        this.updateDescription();
    }
    
    private void updateDescription() {
        this.rawDescription = (upgraded ? CARD_STRINGS.UPGRADE_DESCRIPTION : CARD_STRINGS.DESCRIPTION);
        
        // 只有当前倍数小于基础倍数时才显示扩展描述（因为递归解的倍数在递减）
        boolean shouldShowMultiplier = (upgraded && currentMultiplier < UPGRADE_MULTIPLIER) || 
                                     (!upgraded && currentMultiplier < BASE_MULTIPLIER);
        
        if (shouldShowMultiplier) {
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
    }
}