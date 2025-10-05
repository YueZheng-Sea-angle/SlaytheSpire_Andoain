package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.powers.GuidingAheadPower;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PathfindersDessertChef extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("PathfindersDessertChef");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("PathfindersDessertChef");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = AbstractCard.CardColor.COLORLESS;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.SPECIAL;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int DAMAGE = 14;
    private static final int UPGRADE_DAMAGE = 4; // 只是占位，实际效果在升级中改变

    public PathfindersDessertChef() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.selfRetain = true;
        this.exhaust = true;

        // ===== 修改这里 =====
        // 创建预览卡片并检查是否需要升级
        PathfindersDessertCart previewCard = new PathfindersDessertCart();
        if (this.upgraded) {
            previewCard.upgrade();
        }
        this.cardsToPreview = previewCard;
        // ===================
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;

            // ===== 新增代码 =====
            // 更新预览卡片为升级版本
            if (this.cardsToPreview instanceof PathfindersDessertCart) {
                PathfindersDessertCart previewCard = (PathfindersDessertCart) this.cardsToPreview;
                if (!previewCard.upgraded) {
                    previewCard.upgrade();
                }
            }
            // ===================

            initializeDescription();
        }
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 基础攻击
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.FIRE
        ));
        if (p.hasPower(LightUntoSufferers.POWER_ID) &&
                p.hasPower(GuidingAheadPower.POWER_ID)) {
            p.gainEnergy(1);
            p.getPower(GuidingAheadPower.POWER_ID).flash();
        }
    }
        // 保留时添加甜品车
        @Override
        public void onRetained() {
        AbstractCard dessertCart = new PathfindersDessertCart();
            if (upgraded) dessertCart.upgrade();
            addToBot(new MakeTempCardInHandAction(dessertCart, 1));

        }



    // 当在Pathfinders中被选中时调用
    @Override
    public void onChoseThisOption() {
        AbstractCard copy = this.makeStatEquivalentCopy();
        // 确保被选中的牌也被正确升级
        if (upgraded) copy.upgrade();
        addToBot(new MakeTempCardInHandAction(copy));
    }
    @Override
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("andoainmod:浊燃损伤");
    }
    @Override
    public AbstractCard makeCopy() {
        return new PathfindersDessertChef();
    }
}