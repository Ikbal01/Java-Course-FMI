package bg.fmi.mjt.lab.coffee_machine.supplies;

public class Cappuccino implements Beverage {
    private double water;
    private double milk;
    private double coffee;
    private double cacao;

    public Cappuccino(){
        water = 0;
        milk = 150;
        coffee = 18;
        cacao = 0;
    }

    @Override
    public String getName() {
        return "Cappuccino";
    }

    @Override
    public double getMilk() {
        return milk;
    }

    @Override
    public double getCoffee() {
        return coffee;
    }

    @Override
    public double getWater() {
        return water;
    }

    @Override
    public double getCacao() {
        return cacao;
    }
}
