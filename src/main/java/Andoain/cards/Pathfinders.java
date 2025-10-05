package Andoain.cards;

import Andoain.actions.ShuffleOtherChoicesAction;
import Andoain.helpers.ModHelper;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class Pathfinders extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Pathfinders");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Pathfinders");
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    private final ArrayList<AbstractCard> previewCards = new ArrayList<>();
    private int previewIndex = 0;
    private float previewTimer = 0.0F;

    public Pathfinders() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;

        // 初始化预览卡牌
        previewCards.add(new PathfindersDessertChef());
        previewCards.add(new PathfindersSorcerer());
        previewCards.add(new PathfindersTearer());
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
            this.exhaust = false;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 创建三张寻路者牌的实例
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new PathfindersDessertChef());
        choices.add(new PathfindersSorcerer());
        choices.add(new PathfindersTearer());

        // 如果升级，升级所有选项
        if (upgraded) {
            choices.forEach(AbstractCard::upgrade);
        }

        // 设置选择后的行为
        choices.forEach(card -> {
            card.purgeOnUse = true; // 使用后消失
        });

        // 添加到选择动作
        addToBot(new ChooseOneAction(choices));

    }


    @Override
    public void update() {
        super.update();

        // 悬停时切换预览
        if (this.hb.hovered) {
            if (previewTimer <= 0.0F) {
                previewTimer = 3.0F; // 3秒切换一次
                this.cardsToPreview = previewCards.get(previewIndex);
                previewIndex = (previewIndex + 1) % previewCards.size();
            } else {
                previewTimer -= Gdx.graphics.getDeltaTime();
            }
        } else {
            previewTimer = 0.0F;
            previewIndex = 0;
            this.cardsToPreview = null;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Pathfinders();
    }
}