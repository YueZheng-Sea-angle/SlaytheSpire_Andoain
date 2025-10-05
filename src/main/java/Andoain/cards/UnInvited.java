package Andoain.cards;

import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.DreamPower;
import Andoain.powers.FreeNextCardPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Arrays;
import java.util.List;

public class UnInvited extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("UnInvited");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("UnInvited");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.NONE;

    private static final int DREAM_GAIN = 4;
    private static final int DAMAGE_AMOUNT = 4;
    public static final int LIGHT_GAIN = 1;
    public static final int UPGRADE_LIGHT_GAIN = 2;

    // 定义小队牌列表
    public static final List<String> TEAM_CARD_IDS = Arrays.asList(
            "Mostima", "Fiammetta", "Lemuen", "MyLateran"
    );
    public static int getDamageAmount() { return DAMAGE_AMOUNT; }
    public static int getLightGain(boolean upgraded) {
        return upgraded ? UPGRADE_LIGHT_GAIN : LIGHT_GAIN;
    }

    public UnInvited() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = DREAM_GAIN;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onSkill(1);
        float delay = Settings.FAST_MODE ? 0.5f : 1.0f;

        addToBot(new ApplyPowerAction(p, p, new DreamPower(p, this.magicNumber), this.magicNumber));

        // 移除现有同名能力（避免叠加）
        AbstractPower existingPower = p.getPower(FreeNextCardPower.POWER_ID);
        if (existingPower != null) {
            addToBot(new RemoveSpecificPowerAction(p, p, existingPower));
        }

        // 添加新能力（简化版，不再需要传递升级状态）
        addToBot(new ApplyPowerAction(p, p, new FreeNextCardPower(p, 1), 1));
    }

    // 公共静态方法：判断是否是小队牌
    public static boolean isTeamCard(AbstractCard card) {
        if (card == null || card.cardID == null) return false;

        // 提取基础卡牌名称（移除mod前缀和升级后缀）
        String rawID = card.cardID;

        // 移除mod前缀（如"AndoainMod:"）
        int colonIndex = rawID.indexOf(':');
        if (colonIndex != -1) {
            rawID = rawID.substring(colonIndex + 1);
        }

        // 移除升级后缀（如"_Upgrade"）
        int underscoreIndex = rawID.indexOf('_');
        if (underscoreIndex != -1) {
            rawID = rawID.substring(0, underscoreIndex);
        }

        return TEAM_CARD_IDS.contains(rawID);
    }
    // 用于调试的卡牌ID处理方法
    public static String getProcessedCardId(String cardId) {
        if (cardId == null) return "null";

        String processed = cardId;

        // 移除mod前缀
        int colonIndex = processed.indexOf(':');
        if (colonIndex != -1) {
            processed = processed.substring(colonIndex + 1);
        }

        // 移除升级后缀
        int underscoreIndex = processed.indexOf('_');
        if (underscoreIndex != -1) {
            processed = processed.substring(0, underscoreIndex);
        }

        return processed;
    }

}