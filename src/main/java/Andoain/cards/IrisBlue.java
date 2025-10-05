package Andoain.cards;

import Andoain.actions.SpawnIceCreamMachineBlueAction;
import Andoain.actions.SummonIceCreamMachineAction;
import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

import java.util.List;
import java.util.stream.Collectors;

public class IrisBlue extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("IrisBlue");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = ModHelper.getCardImagePath("IrisBlue");
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final int COST = 1;
    private static final int BLOCK = 4;
    private static final int BASE_DRAW = 1; // 基础抽牌数
    private static final int UPGRADE_DRAW = 2; // 升级后抽牌数
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;


    public IrisBlue() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BLOCK;
        this.magicNumber = BASE_DRAW; // 初始化 magicNumber
        this.baseMagicNumber = BASE_DRAW; // 初始化 baseMagicNumber
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DRAW - BASE_DRAW); // 升级 magicNumber
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
    @Override
    public void onChoseThisOption() {
        AbstractCard newCard = this.makeStatEquivalentCopy();
        newCard.purgeOnUse = true;
        addToBot(new MakeTempCardInHandAction(newCard));
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 先获得格挡
        addToBot(new GainBlockAction(p, p, this.block));

        // 2. 抽牌（使用 magicNumber 控制抽牌数）
        addToBot(new DrawCardAction(p, this.magicNumber));

        // 3. 召唤友方冰淇淋机
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (canSpawn()) {
                    float playerCenterX = p.hb.cX;
                    float playerCenterY = p.hb.cY;
                    float spawnOffset = 180f * Settings.scale;

                    // 计算召唤位置
                    float ftargetX = playerCenterX + spawnOffset;
                    float ftargetY = playerCenterY + 20f;

                    // 确保位置在屏幕内
                    float maxX = Settings.WIDTH - 200f * Settings.scale;
                    if (ftargetX > maxX) {
                        ftargetX = maxX;
                    }

                    // 召唤友方冰淇淋机
                    AbstractDungeon.actionManager.addToTop(
                            new SummonIceCreamMachineAction(
                                    ftargetX,
                                    ftargetY,
                                    true,  // 设置为友方
                                    false,
                                    false
                            )
                    );

                    // 召唤提示
                   // AbstractDungeon.effectList.add(new SpeechBubble(
                           // p.hb.cX,
                          //  p.hb.cY + 120f * Settings.scale,
                          //  "蓝色制冷！",
                          //  true
                  //  ));
                }
                isDone = true;
            }
        });
    }

    private boolean canSpawn() {
        return !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()
                && AbstractDungeon.getMonsters().monsters.stream()
                .noneMatch(mon -> mon instanceof IceCreamMachine);
    }

    @Override
    public AbstractCard makeCopy() {
        return new IrisBlue();
    }
}