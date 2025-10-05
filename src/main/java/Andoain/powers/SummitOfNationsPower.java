package Andoain.powers;

import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;
import java.util.List;

import static Andoain.patches.CardColorEnum.Andoain_Blue;

public class SummitOfNationsPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("SummitOfNations");
    public static final String UPGRADED_POWER_ID = ModHelper.makePath("SummitOfNationsUpgraded");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final float COMMON_UNCOMMON_RATIO = 0.7f;
    private static final float UNCOMMON_RARE_RATIO = 0.7f;

    // 使用静态缓存以提高性能
    private static List<AbstractCard> allOtherColorCards = null;

    // 标记是否为升级版能力
    public final boolean isUpgraded;

    public SummitOfNationsPower(AbstractCreature owner, int amount, boolean upgraded) {
        this.name = powerStrings.NAME;
        this.ID = upgraded ? UPGRADED_POWER_ID : POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isUpgraded = upgraded;
        this.priority = 10; // 确保在其他能力之前执行

        // 使用LightUntoSufferers的美术资源作为临时资源
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/wanguofenghui84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/wanguofenghui32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (isUpgraded) {
            this.description = powerStrings.DESCRIPTIONS[2] + this.amount + powerStrings.DESCRIPTIONS[3];
        } else {
            this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        for (int i = 0; i < this.amount; i++) {
            generateCard();
        }
    }

    private void generateCard() {
        if (allOtherColorCards == null) {
            initializeCardPool();
        }

        if (allOtherColorCards.isEmpty()) {
            return;
        }

        AbstractCard.CardRarity targetRarity = selectRarity();
        List<AbstractCard> candidates = new ArrayList<>();

        // 筛选符合稀有度的卡牌
        for (AbstractCard c : allOtherColorCards) {
            if (c.rarity == targetRarity) {
                candidates.add(c);
            }
        }

        // 如果没有找到匹配的稀有度，放宽条件
        if (candidates.isEmpty()) {
            candidates = allOtherColorCards;
        }

        // 随机选择一张卡片
        AbstractCard selectedCard = candidates.get(AbstractDungeon.cardRandomRng.random(candidates.size() - 1)).makeCopy();
        UnlockTracker.markCardAsSeen(selectedCard.cardID);
        selectedCard.setCostForTurn(0); // 本回合费用为0

        // 添加到手牌
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(selectedCard));
    }

    private AbstractCard.CardRarity selectRarity() {
        float roll = AbstractDungeon.cardRandomRng.random();

        if (isUpgraded) {
            // 升级版：70% 罕见，30% 稀有
            if (roll < UNCOMMON_RARE_RATIO) {
                return AbstractCard.CardRarity.UNCOMMON;
            } else {
                return AbstractCard.CardRarity.RARE;
            }
        } else {
            // 基础版：70% 普通，30% 罕见
            if (roll < COMMON_UNCOMMON_RATIO) {
                return AbstractCard.CardRarity.COMMON;
            } else {
                return AbstractCard.CardRarity.UNCOMMON;
            }
        }
    }

    private void initializeCardPool() {
        allOtherColorCards = new ArrayList<>();

        // 遍历所有卡牌
        for (AbstractCard c : CardLibrary.getAllCards()) {
            // 过滤条件：
            // 1. 非当前角色颜色的卡牌
            // 2. 非特殊卡牌类型（诅咒、状态等）
            // 3. 非基础卡牌
            if (c.color != Andoain_Blue &&
                    c.color != AbstractCard.CardColor.COLORLESS &&
                    c.type != AbstractCard.CardType.CURSE &&
                    c.type != AbstractCard.CardType.STATUS &&
                    c.rarity != AbstractCard.CardRarity.BASIC) {

                allOtherColorCards.add(c);
            }
        }
    }
}