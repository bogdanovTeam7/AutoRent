package pti.sb_mvc_autorent.controller;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pti.sb_mvc_autorent.db.Database;
import pti.sb_mvc_autorent.model.Car;
import pti.sb_mvc_autorent.model.Rent;
import pti.sb_mvc_autorent.model.User;
import pti.sb_mvc_autorent.utils.DateChecker;

@Controller
public class AppController {

	@GetMapping("/")
	public String home() {
		return "home.html";
	}

	@GetMapping("/searchAvailableCars")
	public String searchAvailableCars(Model model, @RequestParam(name = "from") String from,
			@RequestParam(name = "to") String to) {

		String page = "home.html";
		String feedback = "hibás keresési adatok";

		DateChecker dateChecker = new DateChecker(from, to);
		if (dateChecker.isDateRangeInputCorrect()) {

			Database db = new Database();
			Date dateFrom = dateChecker.getFrom();
			Date dateTo = dateChecker.getTo();
			List<Car> cars = db.getAvailableCars(dateFrom, dateTo);
			db.close();
			model.addAttribute("cars", cars);
			model.addAttribute("from", from);
			model.addAttribute("to", to);
			feedback = "Szabad autók listája";
			page = "carsForRent.html";
		}

		model.addAttribute("feedback", feedback);
		return page;
	}

	@GetMapping("/carRentForm/{carId}/{from}/{to}")
	public String carRentForm(Model model, @PathVariable int carId, @PathVariable String from,
			@PathVariable String to) {
		Database db = new Database();
		Car car = db.getCarById(carId);
		db.close();
		DateChecker dateChecker = new DateChecker(from, to);
		model.addAttribute("car", car);
		model.addAttribute("dateChecker", dateChecker);
		return "carRentForm.html";
	}

	@PostMapping("/rentCar")
	public String rentCar(Model model, @RequestParam(name = "carId") int carId,
			@RequestParam(name = "name") String name, @RequestParam(name = "address") String address,
			@RequestParam(name = "email") String email, @RequestParam(name = "phone") String phone,
			@RequestParam(name = "from") String from, @RequestParam(name = "to") String to) {
		String feedback = "sikeres rendelés felvétel";
		String page = "home.html";

		DateChecker dateChecker = new DateChecker(from, to);
		Database db = new Database();
		Car car = null;
		if (isInputCorrect(name, email, address, phone, dateChecker)) {

			car = db.getCarAvailableToRent(carId, dateChecker.getFrom(), dateChecker.getTo());

			if (car == null) {
				feedback = "sikertelen rendelés felvétel - olyan kocsi nem bérelhető";
			} else {
				User user = new User(name, address, email, phone);
				db.saveUser(user);
				Rent rent = new Rent(carId, user.getId(), dateChecker.getFrom(), dateChecker.getTo());
				db.saveRent(rent);
			}
		} else {
			car = db.getCarById(carId);
			feedback = "sikertelen rendelés felvétel";
			model.addAttribute("car", car);
			model.addAttribute("dateChecker", dateChecker);
			page = "carRentForm.html";
		}

		db.close();
		model.addAttribute("feedback", feedback);
		return page;
	}

	private boolean isInputCorrect(String name, String email, String address, String phone, DateChecker dateChecker) {
		boolean correct = true;

		if (name == null || name.length() < 1) {
			correct = false;
		} else if (email == null || email.length() < 1) {
			correct = false;
		} else if (address == null || address.length() < 1) {
			correct = false;
		} else if (phone == null || phone.length() < 1) {
			correct = false;
		} else if (!dateChecker.isDateRangeInputCorrect()) {
			correct = false;
		}

		return correct;
	}

}
