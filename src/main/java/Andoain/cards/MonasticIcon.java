package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.DreamPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MonasticIcon extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("MonasticIcon");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("MonasticIcon");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int DAMAGE = 14;
    private static final int DREAM_GAIN = 3;

    public MonasticIcon() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = DREAM_GAIN;
        this.hasRicochet = true;
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
        // 检查是否有弹药，有弹药才播放攻击动画
        boolean hasAmmo = p.hasPower(AmmunitionPower.POWER_ID) &&
                ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).amount > 0;
        
        if (hasAmmo) {
            andoain.onAttack();
            float delay = Settings.FAST_MODE ? 0.7f : 1.4f;
            addToBot(new WaitAction(delay));
        }

        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL)));
        addToBot(new ApplyPowerAction(p, p, new DreamPower(p, this.magicNumber), this.magicNumber));

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                List<AbstractCard> attackCards = new ArrayList<>();
                List<Integer> modifiedCosts = new ArrayList<>();

                // 1. 收集手牌中所有攻击牌
                for (AbstractCard card : p.hand.group) {
                    if (card.type == AbstractCard.CardType.ATTACK) {
                        attackCards.add(card);
                        int cost = card.cost;

                        // 2. 如果是升级版，先减1费
                        if (upgraded && cost > 0) {
                            cost -= 1;
                        }
                        modifiedCosts.add(cost);
                    }
                }

                if (!attackCards.isEmpty()) {
                    // 3. 随机打乱费用
                    Collections.shuffle(modifiedCosts, new Random(AbstractDungeon.cardRandomRng.randomLong()));

                    // 4. 应用新费用
                    for (int i = 0; i < attackCards.size(); i++) {
                        AbstractCard card = attackCards.get(i);
                        int newCost = modifiedCosts.get(i);

                        card.cost = newCost;
                        card.costForTurn = newCost;
                        card.isCostModified = true;

                        // 特殊处理X费用卡牌
                        if (card.cost == -1) {
                            card.energyOnUse = 0;
                        }
                    }
                }

                this.isDone = true;
            }
        });

        handleRicochet(p, m);
        float delay = Settings.FAST_MODE ? 0.7f : 1.4f;
        addToBot(new WaitAction(delay));
    }
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("andoainmod:迷梦");
    }
}