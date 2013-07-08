package jug.tests;

import jug.test.BeverageQuantityChecker;
import jug.test.Drink;
import jug.test.EmailNotifier;
import jug.test.OrderMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class CofferMakerTest {

   @Mock
   BeverageQuantityChecker quantityChecker;

   @Mock
   EmailNotifier emailNotifier;

   @Test
   public void addTea() {
      final OrderMaker orderMaker = new OrderMaker(1.5, Drink.TEA);
      assertEquals("T::", orderMaker.getOrder());
   }

   @Test
   public void addTeaWithSugar() {
      final OrderMaker orderMaker = new OrderMaker(2d, Drink.TEA, 2);
      assertEquals("T:2:0", orderMaker.getOrder());
   }

   @Test
   public void addChocolate() {
      final OrderMaker orderMaker = new OrderMaker(3d, Drink.CHOCOLAT, 1);
      assertEquals("H:1:0", orderMaker.getOrder());
   }

   @Test(expected = IllegalStateException.class)
   public void testNull() {
      final OrderMaker orderMaker = new OrderMaker(null, null);

   }


   @Test
   public void addChocolateWithNotEnoughMoney() {
      final OrderMaker orderMaker = new OrderMaker(0d, Drink.CHOCOLAT, 1);
      assertEquals("M:money lacking", orderMaker.getOrder());
   }

   @Test
   public void addChocolateWithoutMoney() {
      final OrderMaker orderMaker = new OrderMaker(null, Drink.CHOCOLAT, 1);
      assertEquals("M:money lacking", orderMaker.getOrder());
   }

   @Test
   public void addOrangeJuice() {
      OrderMaker orderMaker = new OrderMaker(0.6D, Drink.ORANGE);
      assertEquals("O::", orderMaker.getOrder());
   }


   @Test
   public void addOrangeJuiceNeverAddSugar() {
      OrderMaker orderMaker = new OrderMaker(0.6D, Drink.ORANGE, 2);
      assertEquals("O::", orderMaker.getOrder());
   }

   @Test
   public void addExtraHotChocolate() {
      final OrderMaker orderMaker = new OrderMaker(3d, Drink.CHOCOLAT, 1, true);
      assertEquals("Hh:1:0", orderMaker.getOrder());
   }

   @Test
   public void testExtraHotSweetOrange() {
      final OrderMaker orderMaker = new OrderMaker(1D, Drink.ORANGE, 2, true);
      assertEquals("O::", orderMaker.getOrder());
   }


   @Test
   public void testOneOrangeReport() {
      final CoffeeMachine coffeeMachine = new CoffeeMachine(quantityChecker, emailNotifier);
      String order = coffeeMachine.order(1D, Drink.ORANGE, 2, true);
      assertEquals("O::", order);
      assertEquals("ORANGE : 1\nTotal : 0.6 Eur", coffeeMachine.getReport());
   }

   @Test
   public void testNoMoreOrangeJuice() {
      when(quantityChecker.isEmpty(anyString())).thenReturn(true);
      final CoffeeMachine coffeeMachine = new CoffeeMachine(quantityChecker, emailNotifier);

      String order = coffeeMachine.order(1D, Drink.ORANGE, 2, true);

      assertNull(order);
      Mockito.verify(emailNotifier, atLeastOnce()).notifyMissingDrink(anyString());
   }


}
