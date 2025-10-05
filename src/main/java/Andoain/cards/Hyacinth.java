package Andoain.cards;

import Andoain.actions.SummonIceCreamMachineAction;
import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
import Andoain.monster.IceCreamMachine;
import Andoain.patches.CardColorEnum;
import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

public class Hyacinth extends AbstractAndoainCard {
    public static final String ID = ModHelper.makePath("Hyacinth");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ModHelper.getCardImagePath("Hyacinth");
    private static final int BASE_COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

    private static final int STUN_TURNS = 2;

    public Hyacinth() {
        super(ID, NAME, IMG_PATH, BASE_COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
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
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        andoain.onSkill(1);

        if (canSpawn()) {
            float playerCenterX = p.hb.cX;
            float playerCenterY = p.hb.cY;
            float spawnOffset = 180f * Settings.scale;
            float ftargetX = playerCenterX + spawnOffset;
            float ftargetY = playerCenterY + 20f;
            float maxX = Settings.WIDTH - 200f * Settings.scale;
            if (ftargetX > maxX) {
                ftargetX = maxX;
            }

            // 先添加召唤动作
            addToBot(new SummonIceCreamMachineAction(
                    ftargetX,
                    ftargetY,
                    false,
                    false,
                    false
            ));

            // 然后添加一个延迟动作来击晕
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    // 查找最新召唤的冰淇淋机
                    AbstractMonster target = null;
                    for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                        if (mon instanceof IceCreamMachine && !mon.isDeadOrEscaped()) {
                            target = mon;
                            break;
                        }
                    }

                    if (target != null) {
                        addToTop(new StunMonsterAction(target, p, STUN_TURNS));
                    }

                    isDone = true;
                }
            });

          //  AbstractDungeon.effectList.add(new SpeechBubble(
                //    p.hb.cX,
                   // p.hb.cY + 120f * Settings.scale,
                  //  "冰冷警告！",
                  //  true
        //    ));
        }
    }

    private boolean canSpawn() {
        return !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()
                && AbstractDungeon.getMonsters().monsters.stream()
                .noneMatch(mon -> mon instanceof IceCreamMachine);
    }
}