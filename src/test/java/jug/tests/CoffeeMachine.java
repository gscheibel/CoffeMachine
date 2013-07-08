package jug.tests;

import jug.test.BeverageQuantityChecker;
import jug.test.Drink;
import jug.test.EmailNotifier;
import jug.test.OrderMaker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
public class CoffeeMachine {

   private final Map<Drink, Double> report;

   private final BeverageQuantityChecker quantityChecker;
   private final EmailNotifier notifier;

   {
      report = new HashMap<>(Drink.values().length);
      for (Drink drink : Drink.values()) {
         report.put(drink, 0d);
      }

   }

   public CoffeeMachine(BeverageQuantityChecker quantityChecker, EmailNotifier notifier) {
      if (quantityChecker == null) {
         throw new IllegalStateException("no quantity check provided");
      }

      if (notifier == null) {
         throw new IllegalStateException("no notifier provided");
      }

      this.quantityChecker = quantityChecker;
      this.notifier = notifier;
   }

   public String order(Double money, Drink drink, int sugar, boolean extraHot) {
      if (quantityChecker.isEmpty(drink.name())) {
         notifier.notifyMissingDrink(drink.name());
         return null;
      }

      final OrderMaker orderMaker = new OrderMaker(money, drink, sugar, extraHot);
      if (orderMaker.isOrderValid()) {
         Double current = report.get(drink);
         current = current + 1;
         report.put(drink, current);
      }
      return orderMaker.getOrder();
   }

   public String getReport() {
      final StringBuilder sb = new StringBuilder(128);
      Double total = 0D;

      for (Drink drink : Drink.values()) {
         Double numberOfDrinks = report.get(drink);
         if (numberOfDrinks > 0) {
            sb.append(drink.name());
            sb.append(" : ");
            sb.append(numberOfDrinks.intValue());
            sb.append("\n");
            total += numberOfDrinks * drink.getPrice();
         }

      }

      sb.append("Total : ");
      sb.append(total);
      sb.append(" Eur");
      return sb.toString();
   }
}
