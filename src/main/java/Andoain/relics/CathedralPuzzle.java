package Andoain.relics;

import Andoain.helpers.ModHelper;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CathedralPuzzle extends CustomRelic {
    public static final String ID = ModHelper.makePath("CathedralPuzzle");
    private static final String IMG_PATH = "AndoainResources/img/relics/CathedralPuzzle.png";
    private static final int ENERGY_GAIN = 1;

    public CathedralPuzzle() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RelicTier.BOSS, LandingSound.MAGICAL);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void atTurnStart() {
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }
    @Override
    public AbstractRelic makeCopy() {
        return new CathedralPuzzle();
    }
}