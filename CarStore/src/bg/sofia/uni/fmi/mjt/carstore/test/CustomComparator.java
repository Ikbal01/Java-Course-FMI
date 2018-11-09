package bg.sofia.uni.fmi.mjt.carstore.test;

import bg.sofia.uni.fmi.mjt.carstore.car.Car;

import java.util.Comparator;

public class CustomComparator implements Comparator<Car> {
    @Override
    public int compare(Car car1, Car car2) {
        return Integer.compare(car1.getPrice(), car2.getPrice());
    }
}
