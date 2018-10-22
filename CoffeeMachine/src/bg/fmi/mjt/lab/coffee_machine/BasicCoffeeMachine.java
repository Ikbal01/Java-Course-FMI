package bg.fmi.mjt.lab.coffee_machine;

import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;
import bg.fmi.mjt.lab.coffee_machine.container.Container;

public class BasicCoffeeMachine implements CoffeeMachine{
    private double water;
    private double coffee;

    private Container thisContainer = new Container() {
        @Override
        public double getCurrentWater() {
            return water;
        }

        @Override
        public double getCurrentMilk() {
            return 0;
        }

        @Override
        public double getCurrentCoffee() {
            return coffee;
        }

        @Override
        public double getCurrentCacao() {
            return 0;
        }
    };

    public BasicCoffeeMachine(){
        water = 600;
        coffee = 600;
    }

    @Override
    public Product brew(Beverage beverage) {
        Product product = null;

        if (beverage.getName().equals("Espresso") && beverage.getCoffee() <= coffee && beverage.getWater() <= water){
            water -= beverage.getWater();
            coffee -= beverage.getCoffee();

            product = new Product(beverage.getName(), 1, null);
        }

        return product;
    }

    @Override
    public Container getSupplies() {
        return thisContainer;
    }

    @Override
    public void refill() {
        water = 600;
        coffee = 600;
    }
}
