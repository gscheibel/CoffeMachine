package jug.test;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
public enum Drink {
   TEA("T",0.4d), CHOCOLAT("H",0.5d), COFFEE("C",0.6D), ORANGE("O", 0.6D){
      @Override
      public boolean hasSugar() {
         return false;
      }

      @Override
      public boolean canBeExtraHot() {
         return false;
      }
   };

   private final String code;
   private final Double price;

   private Drink(final String code, double price) {
      this.code = code;
      this.price = price;
   }

   public String getCode() {
      return code;
   }

   public Double getPrice() {
      return price;
   }

   public boolean hasSugar() {
      return true;
   }

   public boolean canBeExtraHot() {
      return true;
   }
}
