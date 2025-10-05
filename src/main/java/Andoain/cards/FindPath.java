package Andoain.cards;

import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import Andoain.powers.FindPathCooldownPower;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class FindPath extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("FindPath");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("FindPath");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    // 三条路卡
    private static final AbstractCard MYTH = new Myth();
    private static final AbstractCard OBSESSION = new Obsession();
    private static final AbstractCard CROSSING = new Crossing();
    private ArrayList<AbstractCard> previewCards = new ArrayList<>();
    private int previewIndex = 0;
    private float previewTimer = 0.0F;

    public FindPath() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;

        // 初始化预览卡牌列表（必须使用新实例）
        previewCards.add(new Myth());
        previewCards.add(new Obsession());
        previewCards.add(new Crossing());
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
            this.selfRetain = true;
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else if (p.hasPower(FindPathCooldownPower.POWER_ID)) {
            this.cantUseMessage = "前路未定，茫茫泅渡……";
            return false;
        } else {
            return canUse;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new Myth().makeCopy());
        choices.add(new Obsession().makeCopy());
        choices.add(new Crossing().makeCopy());

        // 为每个选项添加生成到手牌的逻辑
        choices.forEach(card -> {
            card.purgeOnUse = true; // 使用后消失
        });

        addToBot(new ChooseOneAction(choices));
    }

    @Override
    public void update() {
        super.update();

        // 只在悬停时切换预览
        if (this.hb.hovered) {
            if (previewTimer <= 0.0F) {
                previewTimer = 3.0F; // 3秒切换一次
                this.cardsToPreview = previewCards.get(previewIndex);
                previewIndex = (previewIndex + 1) % previewCards.size(); // 循环索引
            } else {
                previewTimer -= Gdx.graphics.getDeltaTime(); // 帧数无关的计时器
            }
        } else {
            // 不悬停时重置预览和计时器
            previewTimer = 0.0F;
            previewIndex = 0;
            this.cardsToPreview = null;
        }
    }
}