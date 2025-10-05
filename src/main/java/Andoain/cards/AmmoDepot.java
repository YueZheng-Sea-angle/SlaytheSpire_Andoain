package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AmmoDepot extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("AmmoDepot");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public AmmoDepot() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("AmmoDepot"),
                1,
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.COMMON,
                CardTarget.SELF
        );
        this.exhaust = true;
        this.baseBlock = 0;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void applyPowers() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null && p.hasPower(AmmunitionPower.POWER_ID)) {
            AmmunitionPower power = (AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID);
            // 模拟添加弹药后的状态
            int simulatedAmmo = power.amount + power.currentMax;
            int overflow = simulatedAmmo - power.currentMax;
            this.baseBlock = Math.max(0, overflow) * 3; // 确保不会出现负数
        } else {
            this.baseBlock = 0;
        }
        super.applyPowers();
        // 更新描述
        this.rawDescription = (upgraded ? CARD_STRINGS.UPGRADE_DESCRIPTION : CARD_STRINGS.DESCRIPTION) +
                CARD_STRINGS.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applyPowers(); // 确保在计算伤害时也更新格挡值
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = upgraded ? CARD_STRINGS.UPGRADE_DESCRIPTION : CARD_STRINGS.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 先应用弹药效果
        if (p.hasPower(AmmunitionPower.POWER_ID)) {
            AmmunitionPower power = (AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID);
            power.replenish(power.currentMax);
        } else {
            addToBot(new ApplyPowerAction(p, p, new AmmunitionPower(p, 3, 3)));
        }
        
        // 然后应用格挡
        addToBot(new GainBlockAction(p, p, this.block));
        
        // 计算溢出抽牌数（每点溢出值抽1张牌）
        int overflowDraws = Math.floorDiv(this.block, 3);
        if (overflowDraws > 0) {
            addToBot(new DrawCardAction(p, overflowDraws));
        }
    }
}