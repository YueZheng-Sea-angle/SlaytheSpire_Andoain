package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.DreamPower;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.DoubleEnergyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class RainNight extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("RainNight");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("RainNight");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    public RainNight() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.cardsToPreview = new Regret();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1); // 升级后费用降为1
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
        
        // 1. 翻倍当前能量
        addToBot(new DoubleEnergyAction());

        // 2. 翻倍弹药
        if (p.hasPower(AmmunitionPower.POWER_ID)) {
            AmmunitionPower ammo = (AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID);
            ammo.replenish(ammo.amount); // 弹药能力自己处理超限问题
        }

        // 3. 翻倍格挡
        if (p.currentBlock > 0) {
            addToBot(new GainBlockAction(p, p, p.currentBlock));
        }

        // 4. 翻倍各种能力
        doublePower(p, LightUntoSufferers.POWER_ID);
        doublePower(p, WeakPower.POWER_ID);
        doublePower(p, VulnerablePower.POWER_ID);
        doublePower(p, FrailPower.POWER_ID);
        doublePower(p, DreamPower.POWER_ID);

        // 5. 添加2张悔恨卡
        addToBot(new MakeTempCardInHandAction(new Regret(), 2));
    }

    // 重构后的辅助方法：通过创建新实例翻倍能力
    private void doublePower(AbstractPlayer p, String powerId) {
        if (p.hasPower(powerId)) {
            AbstractPower power = p.getPower(powerId);
            int amount = power.amount;

            switch (powerId) {
                case "AndoainMod:LightUntoSufferers":
                    if(amount != 0){
                    addToBot(new ApplyPowerAction(
                            p, p,
                            new LightUntoSufferers(p, amount),
                            amount));}
                    break;
                case "AndoainMod:Dream":
                    if(amount != 0){
                    addToBot(new ApplyPowerAction(
                            p, p,
                            new DreamPower(p, amount),
                            amount));}
                    break;
                default:
                    // 默认处理方式
                    addToBot(new ApplyPowerAction(p, p, power, amount));
                    break;
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RainNight();
    }
    @Override
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("andoainmod:迷梦");
    }
}
