package com.sample.carmarket.screen.car;

import com.sample.carmarket.app.CarService;
import com.sample.carmarket.entity.Car;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@UiController("Car.browse")
@UiDescriptor("car-browse.xml")
@LookupComponent("carsTable")
public class CarBrowse extends StandardLookup<Car> {
    @Autowired
    private CarService carService;
    @Autowired
    private Notifications notifications;
    @Autowired
    private GroupTable<Car> carsTable;

    @Subscribe("markAsSoldBtn")
    public void onMarkAsSoldBtnClick(Button.ClickEvent event) {
        if (Objects.requireNonNull(carsTable.getSingleSelected()).getStatus().toString().equals("Sold")) {
            notifications.create()
                    .withType(Notifications.NotificationType.TRAY)
                    .withCaption("Already Sold").show();
        } else {
            carService.setChangesInCar(Objects.requireNonNull(carsTable.getSingleSelected()).getId());

            notifications.create()
                    .withType(Notifications.NotificationType.TRAY)
                    .withCaption("Done").show();
        }
    }
}
