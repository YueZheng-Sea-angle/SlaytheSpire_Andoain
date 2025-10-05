package Andoain.powers;

import Andoain.actions.NurserySanctumAction;
import Andoain.helpers.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.SlowPower;

import static Andoain.cards.NurserySanctum.DREAM_AMOUNT;

public class NurserySanctumPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("NurserySanctum");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private final boolean upgraded;
    private static final float UPGRADED_POSITIVE_CHANCE = 0.85f; // 升级后获得正面效果的概率
    private static final float BASE_POSITIVE_CHANCE = 0.65f; // 基础正面效果概率

    public NurserySanctumPower(AbstractCreature owner, boolean upgraded) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.upgraded = upgraded;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // 使用光赐于苦美术资源代替
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/yyst84.png"),
                0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("AndoainResources/img/powers/yyst32.png"),
                0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + DREAM_AMOUNT + powerStrings.DESCRIPTIONS[1];
        if (upgraded) {
            this.description += powerStrings.DESCRIPTIONS[2];
        }
    }

    @Override
    public void atStartOfTurn() {
        flash();

        AbstractPlayer p = (AbstractPlayer) owner;

        // 获得3层梦境
        addToBot(new ApplyPowerAction(p, p, new DreamPower(p, DREAM_AMOUNT), DREAM_AMOUNT));

        // 应用随机效果
        float playerChance = upgraded ? UPGRADED_POSITIVE_CHANCE : BASE_POSITIVE_CHANCE;
        float enemyChance = upgraded ? (1 - UPGRADED_POSITIVE_CHANCE) : (1 - BASE_POSITIVE_CHANCE); // 例如升级后15%正面概率


        // 为所有单位应用随机效果
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped()) {
                addToBot(new NurserySanctumAction(mon, p, enemyChance, false));
            }
        }
        // 为自己应用效果
        addToBot(new NurserySanctumAction(p, p, playerChance, true));
    }
}