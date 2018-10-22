package bg.fmi.mjt.lab.coffee_machine.supplies;

public class Mochaccino implements Beverage {
    private double water;
    private double milk;
    private double coffee;
    private double cacao;

    public Mochaccino(){
        water = 0;
        milk = 150;
        coffee = 18;
        cacao = 10;
    }

    @Override
    public String getName() {
        return "Mochaccino";
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
