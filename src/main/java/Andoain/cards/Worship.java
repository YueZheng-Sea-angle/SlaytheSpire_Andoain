package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

public class Worship extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Worship");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    // 定义周序枚举
    public enum DayOfWeek {
        MONDAY("礼拜一", 1),
        TUESDAY("礼拜二", 2),
        WEDNESDAY("礼拜三", 3),
        THURSDAY("礼拜四", 4),
        FRIDAY("礼拜五", 5),
        SATURDAY("礼拜六", 6),
        SUNDAY("礼拜日", 7);

        public final String displayName;
        public final int multiplier;

        DayOfWeek(String displayName, int multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public DayOfWeek next() {
            return values()[(this.ordinal() + 1) % values().length];
        }
    }

    // 当前周序（从Mod全局获取）
    private DayOfWeek currentDay;

    public Worship() {
        super(
                ID,
                CARD_STRINGS.NAME,
                ModHelper.getCardImagePath("Worship"),
                3,
                CARD_STRINGS.DESCRIPTION,
                CardType.SKILL,
                CardColorEnum.Andoain_Blue,
                CardRarity.UNCOMMON,
                CardTarget.ENEMY
        );
        this.retain = true;
        this.exhaust = true;
        this.baseDamage = 0;
        this.baseBlock = 0;
        this.baseMagicNumber = 0;
        this.magicNumber = this.baseMagicNumber;

        // 初始化时从Mod获取当前周序（如果不存在则从周一开始）
        this.currentDay = ModHelper.getWeekDay();
        updateDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(2);
            updateDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 计算伤害和格挡
        int damage = currentDay.multiplier * 4;
        int block = currentDay.multiplier * 4;

        // 造成伤害
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));

        // 获得格挡
        addToBot(new GainBlockAction(p, p, block));

        // 检查特殊日子的额外效果
        switch (currentDay) {
            case WEDNESDAY:
                if (p.hasPower(AmmunitionPower.POWER_ID)) {
                    ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).replenish(1);
                } else {
                    addToBot(new ApplyPowerAction(p, p, new AmmunitionPower(p, 3, 1)));
                }
                break;
            case SATURDAY:
                addToBot(new com.megacrit.cardcrawl.actions.common.GainEnergyAction(1));
                break;
            case SUNDAY:
                addToBot(new ApplyPowerAction(
                        p, p,
                        new LightUntoSufferers(p, 1),
                        1));
                break;
        }

        // 推进周序并保存到全局
        advanceDay();
    }

    @Override
    public void onRetained() {
        advanceDay();
    }

    private void advanceDay() {
        currentDay = currentDay.next();
        ModHelper.setWeekDay(currentDay); // 保存到Mod全局
        updateDescription();
    }

    private void updateDescription() {
        this.rawDescription = CARD_STRINGS.DESCRIPTION + " NL 当前周序：" + currentDay.displayName + "。";
        initializeDescription();
    }

    @Override
    public void atTurnStart() {
        this.retain = true;
    }
}