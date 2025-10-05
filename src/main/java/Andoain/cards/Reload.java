package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Reload extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Reload");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int BASE_BONUS = 1; // 基础增益
    private static final int UPGRADE_BONUS = 2; // 升级后增益

    public Reload() {
        super(
                ID,
                CARD_STRINGS.NAME,
                "AndoainResources/img/cards/Reload.png",
                1,
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.COMMON,
                CardTarget.SELF
        );
        this.exhaust = true; // 消耗牌
        this.baseMagicNumber = BASE_BONUS;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_BONUS - BASE_BONUS);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 确保弹药能力存在
        if (!p.hasPower(AmmunitionPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, p, new AmmunitionPower(p, 3, 0)));
        }

        // 封装核心逻辑
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AmmunitionPower ammo = (AmmunitionPower) p.getPower(AmmunitionPower.POWER_ID);
                if (ammo != null) {
                    // 记录填充前的状态（仅用于日志）
                    int preAmount = ammo.amount;

                    // 填充弹药（根据卡牌等级）
                    ammo.replenish(magicNumber);

                    // 每有1弹药抽1张牌
                    int drawCards = ammo.amount;
                    if (drawCards > 0) {
                        addToTop(new DrawCardAction(p, drawCards));

                        // 调试日志
                        System.out.println(
                                "[Reload] 装弹完成! 当前:" + ammo.amount + "/" + ammo.currentMax +
                                        " => 抽" + drawCards + "张牌"
                        );
                    } else {
                        System.out.println(
                                "[Reload] 装弹后弹药为0: " + preAmount + "→" + ammo.amount +
                                        " (未抽牌)"
                        );
                    }
                }
                this.isDone = true;
            }
        });
    }

}