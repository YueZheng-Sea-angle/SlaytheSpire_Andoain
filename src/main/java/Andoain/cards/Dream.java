package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.DreamPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Dream extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Dream");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Dream() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("Dream"),
                1, // 1费
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.UNCOMMON,
                CardTarget.ALL_ENEMY
        );
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级效果保持原样
            this.upgradeBaseCost(0);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得7层梦境，达到阈值时自动进入迷梦
        addToBot(new ApplyPowerAction(
                p, p,
                new DreamPower(p, 7),
                7
        ));

        // 给所有敌人添加虚弱
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(
                        monster, p,
                        new WeakPower(monster, 1, false),
                        1
                ));
            }
        }
    }
}