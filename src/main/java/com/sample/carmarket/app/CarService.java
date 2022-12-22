package com.sample.carmarket.app;

import com.sample.carmarket.entity.Car;
import com.sample.carmarket.entity.EngineType;
import com.sample.carmarket.entity.Status;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

@Service
public class CarService {
    @Autowired
    private DataManager dataManager;

    private List<Car> modelsByManufacturerName(String manufacturerName) {
        return dataManager
                .load(Car.class)
                .query("select c from Car c join c.model m join m.manufacturer mf  " +
                        "where mf.name = :manufacturer_name order by m.engineType")
                .parameter("manufacturer_name", manufacturerName).list();
    }

    public EnumMap<EngineType, List<Car>> groupEngineTypeByCar(String manufacturerName) {
        EnumMap<EngineType, List<Car>> typeByCar = new EnumMap<>(EngineType.class);
        List<Car> cars = modelsByManufacturerName(manufacturerName);

        for (Car car : cars) {
            EngineType type = car.getModel().getEngineType();
            if (typeByCar.containsKey(type)) {
                typeByCar.get(type).add(car);
            } else {
                List<Car> newCars = new ArrayList<>();
                newCars.add(car);
                typeByCar.put(type, newCars);
            }
        }
        return typeByCar;
    }

    public void setChangesInCar(UUID id) {
        Car carFromDB = dataManager.load(Car.class).id(id).one();
        carFromDB.setStatus(Status.SOLD);
        carFromDB.setDateOfSale(LocalDate.now());

        dataManager.save(carFromDB);
    }
}