package pti.sb_mvc_autorent.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

	@PostMapping("/carSave")
	public String carSave(Model model, @RequestParam("type") String type, @RequestParam("dayPrice") int dayPrice,
			@RequestParam("available") boolean available, @RequestParam("file") MultipartFile file) throws IOException {

		Car car = new Car(type, dayPrice, available);
		byte[] bFile = file.getBytes();
		car.setImage(bFile);

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

	@PostMapping("/carUpdate")
	public String carUpdate(Model model, @RequestParam("id") int id, @RequestParam("type") String type,
			@RequestParam("dayPrice") int dayPrice, @RequestParam("available") boolean available,
			@RequestParam("file") MultipartFile file) {
		String feedback = null;
		Database db = new Database();
		Car car = db.getCarById(id);
		List<Rent> rents = db.getAllNotFinishedRentsByCarId(id);
		if (!rents.isEmpty()) {
			feedback = "sikertelen modositás - meglévő foglalás!";
		} else {
			if (type.length() > 0) {
				car.setType(type);
			}
			System.out.println(dayPrice);
			if (dayPrice >= 0) {
				car.setDayPrice(dayPrice);
			}

			byte[] bFile = null;
			try {
				bFile = file.getBytes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(bFile);
			car.setImage(bFile);

			car.setAvailable(available);
			db.updateCar(car);
			feedback = "sikeres modositás - " + car;
		}

		db.close();
		model.addAttribute("car", car);
		model.addAttribute("feedback", feedback);
		return "carUpdate.html";
	}
}
