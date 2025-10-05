package Andoain.relics;

import Andoain.helpers.ModHelper;
import Andoain.powers.AmmunitionPower;
import Andoain.powers.LightUntoSufferers;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ChurchMealVoucher extends CustomRelic {
    public static final String ID = ModHelper.makePath("ChurchMealVoucher");
    private static final String IMG_PATH = "AndoainResources/img/relics/ChurchMealVoucher.png";
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;
    private static final int BLOCK_PER_CONDITION = 3;
    private static final int LOW_HP_THRESHOLD = 5;

    public ChurchMealVoucher() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }


   @Override
    public void onPlayerEndTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        int conditionsMet = 0;

        // 条件1: 弹药数为0或没有弹药能力
        boolean noAmmo = checkNoAmmoCondition(p);
        if (noAmmo) conditionsMet++;

        // 条件2: 没有"光赐于苦"能力
        boolean noLightPower = !p.hasPower(LightUntoSufferers.POWER_ID);
        if (noLightPower) conditionsMet++;

        // 条件3: 生命值不高于5
        boolean lowHP = p.currentHealth <= LOW_HP_THRESHOLD;
        if (lowHP) conditionsMet++;

        if (conditionsMet > 0) {
            int blockAmount = BLOCK_PER_CONDITION * conditionsMet;
            flash();
            addToTop(new RelicAboveCreatureAction(p, this));
            addToTop(new GainBlockAction(p, p, blockAmount));
        }
    }

    private boolean checkNoAmmoCondition(AbstractPlayer p) {
        // 检查是否有弹药能力
        AbstractPower ammoPower = p.getPower(AmmunitionPower.POWER_ID);
        if (ammoPower == null) {
            return true; // 没有弹药能力
        }
        // 有弹药能力但弹药数为0
        return ((AmmunitionPower)ammoPower).getammo() <= 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BLOCK_PER_CONDITION + DESCRIPTIONS[1] +
                LOW_HP_THRESHOLD + DESCRIPTIONS[2];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ChurchMealVoucher();
    }
}