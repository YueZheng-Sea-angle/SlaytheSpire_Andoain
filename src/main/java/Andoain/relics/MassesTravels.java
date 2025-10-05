package Andoain.relics;

import Andoain.helpers.ModHelper;
import Andoain.powers.AmmunitionPower;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MassesTravels extends CustomRelic {
    public static final String ID = ModHelper.makePath("MassesTravels");
    private static final String IMG_PATH = "AndoainResources/img/relics/MassesTravels.png";
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int AMMO_GAIN = 3;
    private static final int UPGRADE_COUNT = 2;

    public MassesTravels() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }


    @Override
    public void atTurnStartPostDraw() {
        if (GameActionManager.turn == 1) {
            System.out.println("[MassesTravels] 回合 1 开始，尝试添加弹药...");
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            AbstractPlayer p = AbstractDungeon.player;

            // 先添加 Power（如果不存在）
            if (!p.hasPower(AmmunitionPower.POWER_ID)) {
                System.out.println("[MassesTravels] 玩家没有 AmmunitionPower，正在添加...");
                addToTop(new ApplyPowerAction(p, p, new AmmunitionPower(p)));
            }

            // 延迟执行弹药补充（确保 Power 已应用）
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    // 再次检查 Power 是否存在
                    if (!p.hasPower(AmmunitionPower.POWER_ID)) {
                        System.out.println("[MassesTravels] WARNING: AmmunitionPower 仍未添加！");
                        isDone = true;
                        return;
                    }

                    AmmunitionPower ammo = (AmmunitionPower) p.getPower(AmmunitionPower.POWER_ID);
                    if (ammo != null) {
                        System.out.println("[MassesTravels] 当前弹药量: " + ammo.amount + " / " + ammo.currentMax);
                        ammo.replenish(AMMO_GAIN);
                        System.out.println("[MassesTravels] 补充后弹药量: " + ammo.amount + " / " + ammo.currentMax);
                        System.out.println("[MassesTravels] 升级 2 张随机卡牌...");
                        addToBot(new UpgradeRandomCardAction());
                        addToBot(new UpgradeRandomCardAction());
                    } else {
                        System.out.println("[MassesTravels] ERROR: AmmunitionPower 获取失败！");
                    }
                    isDone = true;
                }
            });

            // 升级卡牌


        }
    }
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MassesTravels();
    }
}