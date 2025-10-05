/*    */ package Andoain.cards;
/*    */ import Andoain.helpers.ModHelper;
/*    */ import Andoain.patches.CardColorEnum;
/*    */ import basemod.abstracts.CustomCard;
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */
/*    */ public class Defend extends AbstractAndoainCard {
    /* 13 */   public static final String ID = ModHelper.makePath("Defend");
    /* 14 */   private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    /* 15 */   private static final String NAME = CARD_STRINGS.NAME;
    /* 16 */   private static final String IMG_PATH = ModHelper.getCardImagePath("Defend");
    /*    */   private static final int COST = 1;
    /* 18 */   private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    /* 19 */   private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    /* 20 */   private static final AbstractCard.CardColor COLOR = CardColorEnum.Andoain_Blue;
    /* 21 */   private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.BASIC;
    /* 22 */   private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;
    /*    */
    /*    */   public Defend() {
        /* 25 */     super(ID, NAME, IMG_PATH, 1, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        /* 26 */     this.block = this.baseBlock = 6;
        /* 27 */     this.tags.add(AbstractCard.CardTags.STARTER_DEFEND);
        /*    */   }
    /*    */
    /*    */
    /*    */   public void upgrade() {
        /* 32 */     if (!this.upgraded) {
            /* 33 */       upgradeName();
            /* 34 */       upgradeBlock(3);
            /* 35 */       initializeDescription();
            /*    */     }
        /*    */   }
    /*    */
    /*    */
    /*    */
    /*    */   public void use(AbstractPlayer p, AbstractMonster m) {
        /* 42 */     addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        /*    */   }
    /*    */ }


/* Location:              C:\Users\24430\Desktop\mostima-mod.jar!\Andoain\cards\Defend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */