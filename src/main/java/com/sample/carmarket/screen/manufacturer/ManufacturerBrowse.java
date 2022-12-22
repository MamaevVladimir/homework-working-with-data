package com.sample.carmarket.screen.manufacturer;

import com.sample.carmarket.app.CarService;
import com.sample.carmarket.entity.Car;
import com.sample.carmarket.entity.EngineType;
import com.sample.carmarket.entity.Manufacturer;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Table;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@UiController("Manufacturer.browse")
@UiDescriptor("manufacturer-browse.xml")
@LookupComponent("table")
public class ManufacturerBrowse extends MasterDetailScreen<Manufacturer> {
    @Autowired
    private CarService carService;
    @Autowired
    private Notifications notifications;
    @Autowired
    private Table<Manufacturer> table;

    @Subscribe("calculateCarsBtn")
    public void onCalculateCarsBtnClick(Button.ClickEvent event) {
        EnumMap<EngineType, List<Car>> typeByCar = carService
                .groupEngineTypeByCar(Objects.requireNonNull(table.getSingleSelected()).getName());

        int countElectric = 0;
        int countGasoline = 0;

        for (Map.Entry<EngineType, List<Car>> entry : typeByCar.entrySet()) {
            int cars = typeByCar.get(entry.getKey()).size();
            if (entry.getKey().getId().equals("E")) {
                countElectric = cars;
            } else {
                countGasoline = cars;
            }
        }
        notifications.create()
                .withType(Notifications.NotificationType.TRAY)
                .withCaption("Electric cars: " + countElectric + ", " + "Gasoline cars: " + countGasoline).show();
    }
}