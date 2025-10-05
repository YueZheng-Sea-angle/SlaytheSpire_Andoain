package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.powers.FindPathCooldownPower;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Crossing extends CustomCard {
    public static final String ID = ModHelper.makePath("Crossing");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = ModHelper.getCardImagePath("Crossing");

    public Crossing() {
        super(ID, CARD_STRINGS.NAME,IMG_PATH, 0, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        this.exhaust = true;
    }
    @Override
    public void upgrade(){
        upgradeName();
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        // 生成升级版寻路
        AbstractCard upgraded = new FindPath();
        upgraded.upgrade();
        addToBot(new MakeTempCardInHandAction(upgraded));

        // 洗入普通版寻路
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInDrawPileAction(new FindPath(), 1, true, true)
        );
        addToBot(new ApplyPowerAction(
                p, p,
                new LightUntoSufferers(p, 1),
                1));
        
        // 添加寻路冷却效果
        addToBot(new ApplyPowerAction(
                p, p,
                new FindPathCooldownPower(p),
                1));
    }
    // 新增选择回调
    @Override
    public void onChoseThisOption() {
        AbstractCard newCard = this.makeStatEquivalentCopy();
        newCard.purgeOnUse = true;
        addToBot(new MakeTempCardInHandAction(newCard));
    }
}