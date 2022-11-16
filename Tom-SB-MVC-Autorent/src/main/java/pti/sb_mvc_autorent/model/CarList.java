package pti.sb_mvc_autorent.model;

import java.util.List;

public class CarList {
	private List<Car> cars;

	public CarList(List<Car> cars) {
		super();
		this.cars = cars;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

}
