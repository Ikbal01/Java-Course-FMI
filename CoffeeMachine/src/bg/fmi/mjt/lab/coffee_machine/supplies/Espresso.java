package bg.fmi.mjt.lab.coffee_machine.supplies;

public class Espresso implements Beverage {
    private double water;
    private double milk;
    private double coffee;
    private double cacao;

    public Espresso(){
        water = 30;
        milk = 0;
        coffee = 10;
        cacao = 0;
    }

    @Override
    public String getName() {
        return "Espresso";
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
