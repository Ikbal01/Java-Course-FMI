package bg.fmi.mjt.lab.coffee_machine;

import bg.fmi.mjt.lab.coffee_machine.container.Container;
import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public class PremiumCoffeeMachine implements CoffeeMachine{
    private double water;
    private double coffee;
    private double milk;
    private double cacao;

    private boolean autoRefill;

    private String[] luck;
    private int luckIndex;


    Container thisContainer = new Container() {
        @Override
        public double getCurrentWater() {
            return water;
        }

        @Override
        public double getCurrentMilk() {
            return milk;
        }

        @Override
        public double getCurrentCoffee() {
            return coffee;
        }

        @Override
        public double getCurrentCacao() {
            return cacao;
        }
    };

    public PremiumCoffeeMachine(){
        water = 1000;
        coffee = 1000;
        milk = 1000;
        cacao = 300;

        autoRefill = false;

        initializeLuck();
        luckIndex = 0;
    }

    /**
     * @param autoRefill - if true, it will automatically refill the container
     * if there are not enough ingredients to make the coffee drink
     */
    public PremiumCoffeeMachine(boolean autoRefill){
        water = 1000;
        coffee = 1000;
        milk = 1000;
        cacao = 300;

        this.autoRefill = autoRefill;

        initializeLuck();
        luckIndex = 0;
    }

    /**
     * If quantity is <= 0 or the quantity is not supported for
     * the particular Coffee Machine the method returns null
     */
    public Product brew(Beverage beverage, int quantity){
        Product product = null;

        if (quantity != 1 && quantity != 3){
            return null;
        }

        if (autoRefill){
            product = new Product(beverage.getName(), quantity, luck[(luckIndex++) % 4]);
        } else if (quantity * beverage.getWater() <= water && quantity * beverage.getCacao() <= cacao && quantity * beverage.getCoffee() <= coffee && quantity * beverage.getMilk() <= milk){
            water -= quantity * beverage.getWater();
            coffee -= quantity * beverage.getCoffee();
            milk -= quantity * beverage.getMilk();
            cacao -= quantity * beverage.getCacao();

            product = new Product(beverage.getName(), quantity, luck[(luckIndex++) % 4]);
        }
        return product;
    }

    @Override
    public Product brew(Beverage beverage) {
        Product product = null;

        if (autoRefill){
            product = new Product(beverage.getName(), 1, luck[(luckIndex++) % 4]);
        } else if (beverage.getWater() <= water && beverage.getCacao() <= cacao && beverage.getCoffee() <= coffee && beverage.getMilk() <= milk){
            water -= beverage.getWater();
            coffee -= beverage.getCoffee();
            milk -= beverage.getMilk();
            cacao -= beverage.getCacao();

            product = new Product(beverage.getName(), 1, luck[(luckIndex++) % 4]);
        }
        return product;
    }

    @Override
    public Container getSupplies() {
        return thisContainer;
    }

    @Override
    public void refill() {
        water = 1000;
        coffee = 1000;
        milk = 1000;
        cacao = 300;
    }

    public void initializeLuck(){
        luck = new String[4];

        luck[0] = "If at first you don't succeed call it version 1.0.";
        luck[1] = "Today you will make magic happen!";
        luck[2] = "Have you tried turning it off and on again?";
        luck[3] = "Life would be much more easier if you had the source code.";
    }
}
