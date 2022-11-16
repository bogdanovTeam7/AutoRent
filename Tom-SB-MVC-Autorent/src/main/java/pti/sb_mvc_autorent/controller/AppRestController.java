package pti.sb_mvc_autorent.controller;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import pti.sb_mvc_autorent.db.Database;
import pti.sb_mvc_autorent.model.Car;
import pti.sb_mvc_autorent.model.CarList;
import pti.sb_mvc_autorent.model.Rent;
import pti.sb_mvc_autorent.model.User;

@RestController
public class AppRestController {
	@GetMapping("/allCars")
	public CarList allCars() {
		Database db = new Database();
		List<Car> cars = db.getAllAvailableCars();
		db.close();
		CarList carList = new CarList(cars);
		return carList;
	}

	@GetMapping("/rentCarRest")
	public Rent rentForm() {
		Rent rent = null;
		Database db = new Database();

		User user = new User("restName", "restAddress", "restEmail", "restPhone");
		db.saveUser(user);

		int carId = 1;
		int userId = user.getId();
		Date dateFrom = new Date();
		Date dateTo = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));

		Car car = db.getAvailableCarByCarId(carId, dateFrom, dateTo);
		if (car != null) {
			rent = new Rent(carId, userId, dateFrom, dateTo);
			db.saveRent(rent);
		}

		db.close();
		return rent;
	}
}
