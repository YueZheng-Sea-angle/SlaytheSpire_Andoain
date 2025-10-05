package Andoain.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.DamageInfo;

public abstract class AbstractAndoainMonster extends AbstractMonster {
    protected float hbXOffset;
    protected float hbYOffset;
    protected float hbWidth;
    protected float hbHeight;

    public AbstractAndoainMonster(String name, String id, int maxHealth,
                                  float hbX, float hbY, float hbW, float hbH,
                                  String imgUrl, float x, float y) {

        super(name, id, maxHealth, hbX, hbY, hbW, hbH, imgUrl, x, y);
        this.hbXOffset = hbX;
        this.hbYOffset = hbY;
        this.hbWidth = hbW;
        this.hbHeight = hbH;
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
    }

    @Override
    public void die() {
        super.die();
    }

    @Override
    public abstract void takeTurn();

    @Override
    protected abstract void getMove(int num);

    public final void loadStandardAnimation(String atlasPath, String skeletonPath) {
        this.loadAnimation(atlasPath, skeletonPath, 1.0f);
        this.stateData.setMix("Idle", "Attack", 0.2f);
        this.stateData.setMix("Attack", "Idle", 0.2f);
    }
}