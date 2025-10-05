package Andoain.actions;

import Andoain.cards.*;
import Andoain.powers.SquadCooldownPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;

public class ChooseSquadCardAction extends AbstractGameAction {
    private ArrayList<AbstractCard> choices;

    public ChooseSquadCardAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.choices = new ArrayList<>();
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            choices.clear();

            // 创建可选卡牌列表
            for (final AbstractCard originalCard : OnceLivedForever.SQUAD_CARDS) {
                // 使用makeStatEquivalentCopy确保数值正确复制
                AbstractCard choiceCard = originalCard.makeStatEquivalentCopy();

                // 创建匿名子类覆盖onChoseThisOption
                AbstractCard wrappedCard = new AbstractCard(
                        choiceCard.cardID,
                        choiceCard.name,
                        choiceCard.assetUrl,
                        choiceCard.cost,
                        choiceCard.rawDescription,
                        choiceCard.type,
                        choiceCard.color,
                        choiceCard.rarity,
                        choiceCard.target
                ) {
                    @Override
                    public void use(AbstractPlayer p, AbstractMonster m) {}

                    @Override
                    public void onChoseThisOption() {
                        // 应用冷却效果
                        if (!AbstractDungeon.player.hasPower(SquadCooldownPower.POWER_ID + "_" + originalCard.cardID)) {
                            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
                                    AbstractDungeon.player,
                                    AbstractDungeon.player,
                                    new SquadCooldownPower(AbstractDungeon.player, originalCard.cardID, 4),
                                    4
                            ));
                        }

                        // 创建实际加入手牌的卡牌
                        AbstractCard cardToAdd = originalCard.makeStatEquivalentCopy();
                        cardToAdd.setCostForTurn(0);
                        cardToAdd.purgeOnUse = true;
                        cardToAdd.isEthereal = true;

                        // 显示效果并加入手牌
                        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(cardToAdd.makeStatEquivalentCopy()));
                        AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(cardToAdd, true));
                    }

                    @Override
                    public void upgrade() {}

                    @Override
                    public AbstractCard makeCopy() {
                        return this;
                    }
                };

                // 复制所有数值属性
                wrappedCard.baseDamage = choiceCard.baseDamage;
                wrappedCard.baseBlock = choiceCard.baseBlock;
                wrappedCard.baseMagicNumber = choiceCard.baseMagicNumber;
                wrappedCard.magicNumber = choiceCard.magicNumber;
                wrappedCard.damage = choiceCard.damage;
                wrappedCard.block = choiceCard.block;

                // 设置卡牌属性
                wrappedCard.portrait = choiceCard.portrait;
                wrappedCard.setCostForTurn(0);
                wrappedCard.purgeOnUse = true;
                wrappedCard.isEthereal = true;
                wrappedCard.initializeDescription();

                // 检查冷却状态
                if (!AbstractDungeon.player.hasPower(SquadCooldownPower.POWER_ID + "_" + originalCard.cardID)) {
                    choices.add(wrappedCard);
                }
            }

            // 如果全部冷却，则允许选择任意牌
            if (choices.isEmpty()) {
                for (final AbstractCard originalCard : OnceLivedForever.SQUAD_CARDS) {
                    AbstractCard choiceCard = originalCard.makeStatEquivalentCopy();

                    AbstractCard wrappedCard = new AbstractCard(
                            choiceCard.cardID,
                            choiceCard.name,
                            choiceCard.assetUrl,
                            choiceCard.cost,
                            choiceCard.rawDescription,
                            choiceCard.type,
                            choiceCard.color,
                            choiceCard.rarity,
                            choiceCard.target
                    ) {
                        @Override
                        public void use(AbstractPlayer p, AbstractMonster m) {}

                        @Override
                        public void onChoseThisOption() {
                            // 创建实际加入手牌的卡牌
                            AbstractCard cardToAdd = originalCard.makeStatEquivalentCopy();
                            cardToAdd.setCostForTurn(0);
                            cardToAdd.purgeOnUse = true;
                            cardToAdd.isEthereal = true;

                            // 显示效果并加入手牌
                            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(cardToAdd.makeStatEquivalentCopy()));
                            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(cardToAdd, true));
                        }

                        @Override
                        public void upgrade() {}

                        @Override
                        public AbstractCard makeCopy() {
                            return this;
                        }
                    };

                    // 复制所有数值属性
                    wrappedCard.baseDamage = choiceCard.baseDamage;
                    wrappedCard.baseBlock = choiceCard.baseBlock;
                    wrappedCard.baseMagicNumber = choiceCard.baseMagicNumber;
                    wrappedCard.magicNumber = choiceCard.magicNumber;
                    wrappedCard.damage = choiceCard.damage;
                    wrappedCard.block = choiceCard.block;

                    wrappedCard.portrait = choiceCard.portrait;
                    wrappedCard.setCostForTurn(0);
                    wrappedCard.purgeOnUse = true;
                    wrappedCard.isEthereal = true;
                    wrappedCard.initializeDescription();

                    choices.add(wrappedCard);
                }
            }

            // 显示选择界面
            addToTop(new ChooseOneAction(choices));
        }
        tickDuration();
    }
}