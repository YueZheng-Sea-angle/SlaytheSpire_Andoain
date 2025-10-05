/*     */ package org.apache.logging.log4j.core.time;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.time.temporal.ChronoField;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.time.temporal.TemporalAccessor;
/*     */ import java.time.temporal.TemporalField;
/*     */ import java.time.temporal.TemporalQueries;
/*     */ import java.time.temporal.TemporalQuery;
/*     */ import java.time.temporal.UnsupportedTemporalTypeException;
/*     */ import java.time.temporal.ValueRange;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class MutableInstant
/*     */   implements Instant, Serializable, TemporalAccessor
/*     */ {
/*     */   private static final int MILLIS_PER_SECOND = 1000;
/*     */   private static final int NANOS_PER_MILLI = 1000000;
/*     */   private static final int NANOS_PER_SECOND = 1000000000;
/*     */   private long epochSecond;
/*     */   private int nanoOfSecond;
/*     */   
/*     */   public long getEpochSecond() {
/*  59 */     return this.epochSecond;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNanoOfSecond() {
/*  64 */     return this.nanoOfSecond;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getEpochMillisecond() {
/*  69 */     int millis = this.nanoOfSecond / 1000000;
/*  70 */     long epochMillisecond = this.epochSecond * 1000L + millis;
/*  71 */     return epochMillisecond;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNanoOfMillisecond() {
/*  76 */     int millis = this.nanoOfSecond / 1000000;
/*  77 */     int nanoOfMillisecond = this.nanoOfSecond - millis * 1000000;
/*  78 */     return nanoOfMillisecond;
/*     */   }
/*     */   
/*     */   public void initFrom(Instant other) {
/*  82 */     this.epochSecond = other.getEpochSecond();
/*  83 */     this.nanoOfSecond = other.getNanoOfSecond();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initFromEpochMilli(long epochMilli, int nanoOfMillisecond) {
/*  92 */     validateNanoOfMillisecond(nanoOfMillisecond);
/*  93 */     this.epochSecond = epochMilli / 1000L;
/*  94 */     this.nanoOfSecond = (int)(epochMilli - this.epochSecond * 1000L) * 1000000 + nanoOfMillisecond;
/*     */   }
/*     */   
/*     */   private void validateNanoOfMillisecond(int nanoOfMillisecond) {
/*  98 */     if (nanoOfMillisecond < 0 || nanoOfMillisecond >= 1000000) {
/*  99 */       throw new IllegalArgumentException("Invalid nanoOfMillisecond " + nanoOfMillisecond);
/*     */     }
/*     */   }
/*     */   
/*     */   public void initFrom(Clock clock) {
/* 104 */     if (clock instanceof PreciseClock) {
/* 105 */       ((PreciseClock)clock).init(this);
/*     */     } else {
/* 107 */       initFromEpochMilli(clock.currentTimeMillis(), 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initFromEpochSecond(long epochSecond, int nano) {
/* 117 */     validateNanoOfSecond(nano);
/* 118 */     this.epochSecond = epochSecond;
/* 119 */     this.nanoOfSecond = nano;
/*     */   }
/*     */   
/*     */   private void validateNanoOfSecond(int nano) {
/* 123 */     if (nano < 0 || nano >= 1000000000) {
/* 124 */       throw new IllegalArgumentException("Invalid nanoOfSecond " + nano);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void instantToMillisAndNanos(long epochSecond, int nano, long[] result) {
/* 137 */     int millis = nano / 1000000;
/* 138 */     result[0] = epochSecond * 1000L + millis;
/* 139 */     result[1] = (nano - millis * 1000000);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSupported(TemporalField field) {
/* 144 */     if (field instanceof ChronoField) {
/* 145 */       return (field == ChronoField.INSTANT_SECONDS || field == ChronoField.NANO_OF_SECOND || field == ChronoField.MICRO_OF_SECOND || field == ChronoField.MILLI_OF_SECOND);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 150 */     return (field != null && field.isSupportedBy(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(TemporalField field) {
/* 155 */     if (field instanceof ChronoField) {
/* 156 */       switch ((ChronoField)field) { case NANO_OF_SECOND:
/* 157 */           return this.nanoOfSecond;
/* 158 */         case MICRO_OF_SECOND: return (this.nanoOfSecond / 1000);
/* 159 */         case MILLI_OF_SECOND: return (this.nanoOfSecond / 1000000);
/* 160 */         case INSTANT_SECONDS: return this.epochSecond; }
/*     */       
/* 162 */       throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
/*     */     } 
/* 164 */     return field.getFrom(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueRange range(TemporalField field) {
/* 169 */     return super.range(field);
/*     */   }
/*     */ 
/*     */   
/*     */   public int get(TemporalField field) {
/* 174 */     if (field instanceof ChronoField) {
/* 175 */       switch ((ChronoField)field) { case NANO_OF_SECOND:
/* 176 */           return this.nanoOfSecond;
/* 177 */         case MICRO_OF_SECOND: return this.nanoOfSecond / 1000;
/* 178 */         case MILLI_OF_SECOND: return this.nanoOfSecond / 1000000;
/* 179 */         case INSTANT_SECONDS: ChronoField.INSTANT_SECONDS.checkValidIntValue(this.epochSecond); break; }
/*     */       
/* 181 */       throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
/*     */     } 
/* 183 */     return range(field).checkValidIntValue(field.getFrom(this), field);
/*     */   }
/*     */ 
/*     */   
/*     */   public <R> R query(TemporalQuery<R> query) {
/* 188 */     if (query == TemporalQueries.precision()) {
/* 189 */       return (R)ChronoUnit.NANOS;
/*     */     }
/*     */     
/* 192 */     if (query == TemporalQueries.chronology() || query == 
/* 193 */       TemporalQueries.zoneId() || query == 
/* 194 */       TemporalQueries.zone() || query == 
/* 195 */       TemporalQueries.offset() || query == 
/* 196 */       TemporalQueries.localDate() || query == 
/* 197 */       TemporalQueries.localTime()) {
/* 198 */       return null;
/*     */     }
/* 200 */     return query.queryFrom(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 205 */     if (object == this) {
/* 206 */       return true;
/*     */     }
/* 208 */     if (!(object instanceof MutableInstant)) {
/* 209 */       return false;
/*     */     }
/* 211 */     MutableInstant other = (MutableInstant)object;
/* 212 */     return (this.epochSecond == other.epochSecond && this.nanoOfSecond == other.nanoOfSecond);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 217 */     int result = 17;
/* 218 */     result = 31 * result + (int)(this.epochSecond ^ this.epochSecond >>> 32L);
/* 219 */     result = 31 * result + this.nanoOfSecond;
/* 220 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 225 */     StringBuilder sb = new StringBuilder(64);
/* 226 */     formatTo(sb);
/* 227 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 232 */     buffer.append("MutableInstant[epochSecond=").append(this.epochSecond).append(", nano=").append(this.nanoOfSecond).append("]");
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\time\MutableInstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */