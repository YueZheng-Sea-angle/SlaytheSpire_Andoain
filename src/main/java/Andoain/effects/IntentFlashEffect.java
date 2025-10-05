package Andoain.effects;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class IntentFlashEffect extends AbstractGameEffect {
    private final AbstractMonster monster;
    private final String intentDesc;
    private boolean playedSound = false;

    public IntentFlashEffect(AbstractMonster monster, String intentDesc) {
        this.monster = monster;
        this.intentDesc = intentDesc;
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (!playedSound) {
            playedSound = true;
            CardCrawlGame.sound.play("INTENT_FLASH");
            // 确保意图显示
            monster.createIntent();
        }

        this.duration -= com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0f) {
            this.isDone = true;
        }
    }

    @Override
    public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch sb) {}

    @Override
    public void dispose() {}
}