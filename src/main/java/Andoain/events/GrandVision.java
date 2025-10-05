package Andoain.events;

import Andoain.cards.Obsessed;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class GrandVision extends AbstractImageEvent {
    public static final String ID = "Andoain:GrandVision";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private int screenNum = 0; // 添加屏幕状态跟踪

    public GrandVision() {
        super(NAME, DESCRIPTIONS[0], "AndoainResources/img/events/GrandVision.png");

        // 设置选项和卡牌预览
        AbstractCard obsessedPreview = new Obsessed();
        obsessedPreview.upgrade();
        AbstractCard regretPreview = new Regret();

        this.imageEventText.setDialogOption(OPTIONS[0], obsessedPreview);
        this.imageEventText.setDialogOption(OPTIONS[1], regretPreview);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screenNum) {
            case 0: // 初始屏幕
                switch (buttonPressed) {
                    case 0: // 我需要知晓更多！
                        AbstractCard card = new Obsessed();
                        card.upgrade();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        break;
                    case 1: // 听蕾缪安的
                        AbstractDungeon.player.increaseMaxHp(7, true);
                        AbstractDungeon.player.heal(38);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Regret(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        break;
                }

                // 更新屏幕状态
                this.screenNum = 1;
                // 移除所有选项，只留下继续
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[2]);
                break;

            case 1: // 结果屏幕
                // 任何按钮都会结束事件
                this.openMap();
                break;
        }
    }
}