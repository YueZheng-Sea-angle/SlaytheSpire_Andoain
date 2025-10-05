package Andoain.relics;

import Andoain.helpers.ModHelper;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AuraRelic extends CustomRelic {
    public static final String ID = ModHelper.makePath("Aura");
    private static final String IMG_PATH = "AndoainResources/img/relics/Aura.png";
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public AuraRelic() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // "友方冰淇淋机现在可以成为弹射的合法目标，但不易被攻击。"
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AuraRelic();
    }
}