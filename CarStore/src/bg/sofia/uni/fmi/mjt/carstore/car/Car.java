package bg.sofia.uni.fmi.mjt.carstore.car;

import bg.sofia.uni.fmi.mjt.carstore.enums.EngineType;
import bg.sofia.uni.fmi.mjt.carstore.enums.Model;
import bg.sofia.uni.fmi.mjt.carstore.enums.Region;

import java.util.Random;

public abstract class Car implements Comparable<Car> {
    private Model model;
    private int year;
    private int price;
    private EngineType engineType;
    private Region region;
    private String regisNumber;

    public Car(Model model, int year, int price, EngineType engineType, Region region) {
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

    /**
     * Returns the model of the car.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Returns the year of manufacture of the car.
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the price of the car.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Returns the engine type of the car.
     */
    public EngineType getEngineType() {
        return engineType;
    }

    /**
     * Returns the unique registration number of the car.
     */
    public String getRegistrationNumber() {
        return regisNumber;
    }

    @Override
    public int compareTo(Car car) {
        int flag = this.getModel().compareTo(car.getModel());
        if (flag == 0) {
            flag = this.getYear() - car.getYear();
        }
        return flag;
    }
}
