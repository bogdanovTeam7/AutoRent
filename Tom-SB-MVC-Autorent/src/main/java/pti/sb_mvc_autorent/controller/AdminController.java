package pti.sb_mvc_autorent.controller;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import pti.sb_mvc_autorent.db.Database;
import pti.sb_mvc_autorent.model.Car;
import pti.sb_mvc_autorent.model.Rent;

@Controller
public class AdminController {
	@GetMapping("/admin")
	public String admin() {
		return "admin.html";
	}

	@GetMapping("/rents")
	public String rents(Model model) {
		Database db = new Database();
		List<Rent> rents = db.getAllRents();
		db.close();
		model.addAttribute("rents", rents);
		return "rents.html";
	}

	@GetMapping("/cars")
	public String cars(Model model) {
		Database db = new Database();
		List<Car> cars = db.getAllRCars();
		db.close();
		model.addAttribute("cars", cars);
		return "cars.html";
	}

	@GetMapping("/carSaveForm")
	public String carSaveForm() {
		return "carSave.html";
	}

	@GetMapping("/carSave")
	public String carSave(Model model, @RequestParam(name = "type") String type,
			@RequestParam(name = "dayPrice") int dayPrice, @RequestParam(name = "available") boolean available) {
		Car car = new Car(type, dayPrice, available);
		Database db = new Database();
		db.saveCar(car);
		db.close();
		String feedback = "sikeres mentés - " + car;
		model.addAttribute("feedback", feedback);
		return "admin.html";
	}

	@GetMapping("/carUpdateForm/{carId}")
	public String carUpdateForm(Model model, @PathVariable int carId) {
		Database db = new Database();
		Car car = db.getCarById(carId);
		db.close();
		model.addAttribute("car", car);
		return "carUpdate.html";
	}

	@GetMapping("/carUpdate")
	public String carUpdate(Model model, @RequestParam(name = "id") int id, @RequestParam(name = "type") String type,
			@RequestParam(name = "dayPrice") int dayPrice, @RequestParam(name = "available") boolean available) {
		String feedback = null;
		Database db = new Database();
		List<Rent> rents = db.getAllNotFinishedRentsByCarId(id);
		if (!available && !rents.isEmpty()) {
			feedback = "sikertelen modositás - meglévő foglalás!";
		} else {
			Car car = db.getCarById(id);
			car.setType(type);
			car.setDayPrice(dayPrice);
			car.setAvailable(available);
			db.updateCar(car);
			feedback = "sikeres modositás - " + car;
		}

		db.close();
		model.addAttribute("feedback", feedback);
		return "carUpdate.html";
	}
}
