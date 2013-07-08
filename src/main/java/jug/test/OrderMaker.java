package jug.test;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
public class OrderMaker {
   private final Double money;
   private final boolean extraHot;
   private Drink drink;
   private int sugar;

   public OrderMaker(Double money, Drink drink, int sugar) {
      this(money, drink, sugar, false);
   }

   public OrderMaker(Double money, Drink drink) {
      this(money, drink, 0);
   }

   public OrderMaker(Double money, Drink drink, int sugar, boolean extraHot) {
      if (drink == null) {
         throw new IllegalStateException("NO drink provided");
      }
      this.money = money;
      this.extraHot = extraHot;
      this.drink = drink;
      this.sugar = sugar;
   }


   public String getOrder() {
      if (!hasEnoughMoney()) {
         return message("money lacking");
      }
      StringBuilder builder = new StringBuilder();
      builder.append(drink.getCode());
      addExtraHot(builder);
      addSeparator(builder);

      if (hasSugar()) {
         builder.append(Math.min(2, sugar));
      }
      addSeparator(builder);
      if (hasSugar()) {
         builder.append("0");
      }
      return builder.toString();
   }

   private void addExtraHot(StringBuilder builder) {
      if (extraHot && drink.canBeExtraHot()) {
         builder.append("h");
      }
   }


   private boolean hasEnoughMoney() {
      return money != null && drink.getPrice() <= money;
   }

   private String message(String message) {
      final StringBuilder sb = new StringBuilder();
      sb.append("M");
      addSeparator(sb);
      sb.append(message);
      return sb.toString();
   }

   private void addSeparator(StringBuilder builder) {
      builder.append(":");
   }


   private boolean hasSugar() {
      return sugar > 0 && drink.hasSugar();
   }

   public boolean isOrderValid() {
      return hasEnoughMoney();
   }
}
