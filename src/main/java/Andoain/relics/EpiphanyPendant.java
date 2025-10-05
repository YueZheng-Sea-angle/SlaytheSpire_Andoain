package Andoain.relics;

import Andoain.helpers.ModHelper;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EpiphanyPendant extends CustomRelic {
    public static final String ID = ModHelper.makePath("EpiphanyPendant");
    private static final String IMG_PATH = "AndoainResources/img/relics/EpiphanyPendant.png";
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public EpiphanyPendant() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void atBattleStart() {
        // 战斗开始时显示特效
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // "触发弹射时，伤害不再衰减。"
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EpiphanyPendant();
    }
}