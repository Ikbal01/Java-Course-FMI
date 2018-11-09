package bg.sofia.uni.fmi.mjt.carstore.car;

import bg.sofia.uni.fmi.mjt.carstore.enums.EngineType;
import bg.sofia.uni.fmi.mjt.carstore.enums.Model;

public abstract class Car implements Comparable<Car> {
    /**
     * Returns the model of the car.
     */
    public abstract Model getModel();

    /**
     * Returns the year of manufacture of the car.
     */
    public abstract int getYear();

    /**
     * Returns the price of the car.
     */
    public abstract int getPrice();

    /**
     * Returns the engine type of the car.
     */
    public abstract EngineType getEngineType();

    /**
     * Returns the unique registration number of the car.
     */
    public abstract String getRegistrationNumber();

    @Override
    public int compareTo(Car car) {
        int flag = this.getModel().compareTo(car.getModel());
        if (flag == 0) {
            flag = this.getYear() - car.getYear();
        }
        return flag;
    }
}
