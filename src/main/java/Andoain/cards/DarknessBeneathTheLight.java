package Andoain.cards;

import Andoain.actions.DarknessBeneathTheLightAction;
import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.DarknessBeneathTheLightPower;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DarknessBeneathTheLight extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("DarknessBeneathTheLight");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("DarknessBeneathTheLight");
    private static final int COST = -1; // X费
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    public DarknessBeneathTheLight() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
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
        
        // 获取玩家实际支付的费用
        int energyUsed = this.energyOnUse;
        if(this.upgraded){
            energyUsed += 1;
        }
        if (p.hasRelic("Chemical X")) {
            energyUsed += 2;
            p.getRelic("Chemical X").flash();
        }

        // 应用效果给所有符合条件的卡牌
        addToBot(new DarknessBeneathTheLightAction(p, false));

        // 添加出牌限制
        addToBot(new ApplyPowerAction(
                p, p,
                new DarknessBeneathTheLightPower(p, energyUsed),
                energyUsed
        ));

        if (!freeToPlay()) {
            p.energy.use(energyUsed);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DarknessBeneathTheLight();
    }
}