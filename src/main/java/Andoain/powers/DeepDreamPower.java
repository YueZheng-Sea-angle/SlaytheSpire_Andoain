package Andoain.powers;

import Andoain.cards.UnInvited;
import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DeepDreamPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("DeepDream");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int BLOCK_AMOUNT = 30;
    private static final String DREAM_WAKE_CARD_ID = ModHelper.makePath("DreamWake");
    public DeepDreamPower(AbstractCreature owner, int amount) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;

        // 美术资源
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/mimeng84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/mimeng32.png"), 0, 0, 32, 32);

        updateDescription();
        addToTop(new GainBlockAction(owner, owner, BLOCK_AMOUNT));

        // 触发进入迷梦效果（如果玩家有天使能力）
        if (owner.hasPower(AngelPower.POWER_ID)) {
            ((AngelPower) owner.getPower(AngelPower.POWER_ID)).onEnterDeepDream();
        }
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        // 特殊处理梦醒卡牌，即使在迷梦状态下没有天使能力也可以打出
        if (card.cardID != null && card.cardID.equals(DREAM_WAKE_CARD_ID)) {
            return true;
        }
        // 检查是否由"不速之客"效果触发的小队牌
        if (owner.hasPower(FreeNextCardPower.POWER_ID) &&
                UnInvited.isTeamCard(card)) {
            return true; // 允许无视限制
        }
        // 其他攻击牌的处理
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (!owner.hasPower(AngelPower.POWER_ID)) {
                card.cantUseMessage = powerStrings.DESCRIPTIONS[1];
                return false;
            }
        }
        return true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            // 在移除前触发退出迷梦效果
            if (owner.hasPower(AngelPower.POWER_ID)) {
                ((AngelPower) owner.getPower(AngelPower.POWER_ID)).onExitDeepDream();
            }
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
}