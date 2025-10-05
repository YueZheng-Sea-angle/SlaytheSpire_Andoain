package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import java.util.List;
import java.util.stream.Collectors;

public class CandyTime extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("CandyTime");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "AndoainResources/img/cards/CandyTime.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    // 用于切换阵营的伤害值
    private static final int SWITCH_DAMAGE = 9999;

    public CandyTime() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 升级效果：先获得1层人工制品
        if (upgraded) {
            addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
        }

        // 查找所有存活的冰淇淋机
        List<AbstractMonster> machines = AbstractDungeon.getMonsters().monsters.stream()
                .filter(mon -> mon instanceof IceCreamMachine && !mon.isDeadOrEscaped())
                .collect(Collectors.toList());

        // 对每个冰淇淋机造成9999点生命流失来触发阵营切换
        for (AbstractMonster machine : machines) {
            addToBot(new LoseHPAction(machine, p, SWITCH_DAMAGE, AbstractGameAction.AttackEffect.NONE));
        }
    }


}