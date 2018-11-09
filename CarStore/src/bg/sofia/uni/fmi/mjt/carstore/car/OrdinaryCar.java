package bg.sofia.uni.fmi.mjt.carstore.car;

import bg.sofia.uni.fmi.mjt.carstore.enums.EngineType;
import bg.sofia.uni.fmi.mjt.carstore.enums.Model;
import bg.sofia.uni.fmi.mjt.carstore.enums.Region;

import java.util.Random;

public class OrdinaryCar extends Car {
    private Model model;
    private int year;
    private int price;
    private EngineType engineType;
    private Region region;
    private String regisNumber;

    public OrdinaryCar(Model model, int year, int price, EngineType engineType, Region region) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.engineType = engineType;
        this.region = region;

        Random random = new Random();
        char randomChar1 = (char)(random.nextInt(26) + 'A');
        char randomChar2 = (char)(random.nextInt(26) + 'A');

        regisNumber = this.region.getPrefix() + this.region.getNumber() + randomChar1 + randomChar2;
        region.increaseNumber();
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public EngineType getEngineType() {
        return engineType;
    }

    @Override
    public String getRegistrationNumber() {
        return regisNumber;
    }
}
