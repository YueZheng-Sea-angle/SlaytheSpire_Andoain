package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Answer extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Answer");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Answer() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("Answer"),
                0,
                CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK,
                CardColor.COLORLESS,
                CardRarity.SPECIAL,
                CardTarget.ENEMY // 单体攻击
        );
        this.baseDamage = 0; // 基础伤害设为0，实际伤害由效果决定
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 计算消耗堆内的诅咒牌和状态牌数量
        int statusAndCurseCount = countExhaustedStatusAndCurses();
        int baseDamagePerShot = statusAndCurseCount * 4;

        // 获取弹药数
        AbstractPower ammoPower = p.getPower(AmmunitionPower.POWER_ID);
        int ammoCount = ammoPower != null ? ((AmmunitionPower)ammoPower).getammo() : 0;

        // 如果没有弹药，直接返回
        if (ammoCount <= 0) return;

        // 造成X次伤害（X为弹药数）
        for (int i = 0; i < ammoCount; i++) {
            addToBot(new DamageAction(
                    m,
                    new DamageInfo(p, baseDamagePerShot, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.FIRE));
        }

        // 耗尽所有弹药
        for(int i = 0; i < ammoCount; i++){
            ((AmmunitionPower) ammoPower).spend(1);
        }

    }

    private int countExhaustedStatusAndCurses() {
        int count = 0;
        // 检查消耗堆
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c.type == CardType.STATUS || c.color == CardColor.CURSE) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}