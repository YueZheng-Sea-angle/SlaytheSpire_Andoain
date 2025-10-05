package Andoain.cards;
/*    */ import Andoain.character.andoain;
import Andoain.helpers.ModHelper;
/*    */ import Andoain.patches.CardColorEnum;
/*    */ import Andoain.powers.AmmunitionPower;
import basemod.abstracts.CustomCard;
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.DamageAction;
/*    */ import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.cards.DamageInfo;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */
/*    */ public class Strike extends AbstractAndoainCard {
    /* 15 */   public static final String ID = ModHelper.makePath("Strike");
    /* 16 */   private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    /*    */
    /* 18 */   private static final String NAME = CARD_STRINGS.NAME;
    /* 19 */   private static final String IMG_PATH = ModHelper.getCardImagePath("Strike");
    /*    */
    /*    */   private static final int COST = 1;
    /* 22 */   private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    /* 23 */   private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    /* 24 */   private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    /* 25 */   private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.BASIC;
    /* 26 */   private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;
               private static final Boolean BONCE = true;
    /*    */
    /*    */   public Strike() {
        /* 29 */     super(ID, NAME, IMG_PATH, 1, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        /* 30 */     this.damage = this.baseDamage = 5;
        /* 31 */     this.tags.add(AbstractCard.CardTags.STARTER_STRIKE);
        /* 32 */     this.tags.add(AbstractCard.CardTags.STRIKE);
        // 启用弹射功能
                    this.hasRicochet = true; // 使用父类的弹射开关
        /*    */   }
    /*    */
    /*    */
    /*    */   public void upgrade() {
        /* 37 */     if (!this.upgraded) {
            /* 38 */       upgradeName();
            /* 39 */       upgradeDamage(3);
            /* 40 */       initializeDescription();
            /*    */     }
        /*    */   }
    /*    */
    /*    */
    /*    */   public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查是否有弹药
        boolean hasAmmo = p.hasPower(AmmunitionPower.POWER_ID) &&
                ((AmmunitionPower)p.getPower(AmmunitionPower.POWER_ID)).amount > 0;

        // 如果有弹药才播放攻击动画
        if (hasAmmo) {
            andoain.onAttack();
            float delay = Settings.FAST_MODE ? 0.7f : 1.4f;
            addToBot(new WaitAction(delay));
        }
        /* 46 */     addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, DamageInfo.DamageType.NORMAL)));
        handleRicochet(p, m);
        /*    */   }
    /*    */ }


/* Location:              C:\Users\24430\Desktop\Andoain-mod.jar!\Andoain\cards\Strike.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */