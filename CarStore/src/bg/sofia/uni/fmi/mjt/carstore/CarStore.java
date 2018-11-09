package bg.sofia.uni.fmi.mjt.carstore;

import bg.sofia.uni.fmi.mjt.carstore.car.Car;
import bg.sofia.uni.fmi.mjt.carstore.comparator.YearComparator;
import bg.sofia.uni.fmi.mjt.carstore.enums.Model;
import bg.sofia.uni.fmi.mjt.carstore.exception.CarNotFoundException;

import java.util.*;

public class CarStore {
    private List<Car> cars;
    private Comparator<Car> carComparator;


    public CarStore() {
        cars = new LinkedList<>();
    }
    /**
     * Adds the specified car in the store.
     * @return true if the car was added successfully to the store
     */
    public boolean add(Car car) {
        if (!cars.contains(car)) {
            return cars.add(car);
        }
        return false;
    }

    /**
     * Adds all of the elements of the specified collection in the store.
     * @return true if the store cars are changed after the execution (i.e. at least one new car is added to the store)
     */
    public boolean addAll(Collection<Car> cars) {
        boolean changed = false;
        for (Car car : cars) {
            if (!this.cars.contains(car)) {
                this.cars.add(car);
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Removes the specified car from the store.
     * @return true if the car is successfully removed from the store
     */
    public boolean remove(Car car) {
        return cars.remove(car);
    }

    /**
     * Returns all cars of a given model.
     * The cars need to be sorted by year of manufacture (in ascending order).
     */
    public Collection<Car> getCarsByModel(Model model) {
        List<Car> temp = new LinkedList<>();

        for (Car car : cars) {
            if (car.getModel().equals(model)) {
                temp.add(car);
            }
        }

        temp.sort(new YearComparator());

        return temp;
    }

    /**
     * Finds a car from the store by its registration number.
     * @throws CarNotFoundException if a car with this registration number is not found in the store
     **/
    public Car getCarByRegistrationNumber(String registrationNumber) {
        Car foundCar = null;

        for (Car car : cars) {
            if (car.getRegistrationNumber().equals(registrationNumber)) {
                foundCar = car;
            }
        }

        if (foundCar == null) {
            throw new CarNotFoundException();
        }

        return foundCar;
    }

    /**
     * Returns all cars sorted by their default order*
     **/
    public Collection<Car> getCars() {
        Collections.sort(cars);
        return cars;
    }
    /**
     * Returns all cars sorted according to the order induced by the specified comparator.
     */
    public Collection<Car> getCars(Comparator<Car> comparator) {
        cars.sort(comparator);
        return cars;
    }

    /**
     * Returns all cars sorted according to the given comparator and boolean flag for order.
     * @param isReversed if true the cars should be returned in reversed order
     */
    public Collection<Car> getCars(Comparator<Car> comparator, boolean isReversed) {
        cars.sort(comparator);
        if (isReversed) {
            Collections.reverse(cars);
        }
        return cars;
    }

    /**
     * Returns the total number of cars in the store.
     */
    public int getNumberOfCars() {
        return cars.size();
    }

    /**
     * Returns the total price of all cars in the store.
     */
    public int getTotalPriceForCars() {
        int totalPrice = 0;
        for (Car car : cars) {
            totalPrice += car.getPrice();
        }
        return totalPrice;
    }
}
