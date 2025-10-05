package Andoain.cards;

import Andoain.actions.SummonIceCreamMachineAction;
import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

import java.util.List;
import java.util.stream.Collectors;

public class Iris extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Iris");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Iris");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.BASIC;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    private static final int BLOCK = 4;
    private static final int MAGICNUMBER = 6;

    public Iris() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BLOCK;
        this.magicNumber = MAGICNUMBER;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.isInnate = true;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
    @Override
    public void onChoseThisOption() {
        AbstractCard newCard = this.makeStatEquivalentCopy();
        newCard.purgeOnUse = true;
        addToBot(new MakeTempCardInHandAction(newCard));
    }

    // MODIFIED: 完全重写use方法，使用新的召唤系统
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 先获得格挡
        addToBot(new GainBlockAction(p, p, this.block));

        // 2. 效果处理
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                List<AbstractMonster> machines = AbstractDungeon.getMonsters().monsters.stream()
                        .filter(mon -> mon instanceof IceCreamMachine && !mon.isDeadOrEscaped())
                        .collect(Collectors.toList());

                if (!machines.isEmpty()) {
                    // 存有冰淇淋机时处理效果 - 抽3张牌
                    addToTop(new DrawCardAction(p, 3));
                } else {
                    // 不存在时尝试召唤
                    if (canSpawn()) {
                        float playerCenterX = p.hb.cX; // 玩家中心X坐标
                        float playerCenterY = p.hb.cY; // 玩家中心Y坐标
                        float spawnOffset = 260f * Settings.scale; // 右侧偏移量

                        // 计算召唤位置
                        float ftargetX = playerCenterX + spawnOffset;
                        float ftargetY = playerCenterY + 25f;

                        // 确保位置在屏幕内（可选保护逻辑）
                        float maxX = Settings.WIDTH - 200f * Settings.scale;
                        if (ftargetX > maxX) {
                            ftargetX = maxX;
                        }
                        AbstractDungeon.actionManager.addToTop(
                                new SummonIceCreamMachineAction(
                                        ftargetX,
                                        ftargetY,
                                        false,
                                        false,
                                        false
                                )
                        );
                        // 召唤提示
                       // AbstractDungeon.effectList.add(new SpeechBubble(
                              //  p.hb.cX,
                               // p.hb.cY + 120f * Settings.scale,
                              //  "拉特兰制冷！",
                               // true
                      //  ));
                    }
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


}