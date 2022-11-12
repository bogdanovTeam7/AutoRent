package pti.sb_mvc_autorent.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import pti.sb_mvc_autorent.model.Car;
import pti.sb_mvc_autorent.model.Rent;
import pti.sb_mvc_autorent.model.User;

public class Database {
	private SessionFactory factory;

	public Database() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}

	public void close() {
		factory.close();
	}

	public List<Car> getAvailableCars(Date dateFrom, Date dateTo) {
		List<Car> cars = null;
		List<Car> resultCars = new ArrayList<>();

		cars = getAllAvailableCars();
		for (Car car : cars) {
			List<Rent> rents = getAllRentsByCarId(car.getId());
			boolean available = true;
			for (Rent rent : rents) {
				boolean isConcurrence = rent.fallsIn(dateFrom) || rent.fallsIn(dateTo);
				if (isConcurrence) {
					available = false;
					break;
				}
			}
			if (available) {
				resultCars.add(car);
			}
		}

		return resultCars;
	}

	public List<Rent> getAllRentsByCarId(int id) {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT r FROM Rent r WHERE r.autoId=?1", Rent.class);
		query.setParameter(1, id);
		List<Rent> rents = query.getResultList();
		transaction.commit();
		session.close();
		return rents;
	}

	private List<Car> getAllAvailableCars() {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT c FROM Car c WHERE c.available=?1", Car.class);
		query.setParameter(1, Boolean.TRUE);
		List<Car> cars = query.getResultList();
		transaction.commit();
		session.close();
		return cars;
	}

	public Car getCarById(int carId) {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		Car car = session.get(Car.class, carId);
		transaction.commit();
		session.close();
		return car;
	}

	public Car getCarAvailableToRent(int carId, Date from, Date to) {
		List<Car> cars = getAllAvailableCars();
		Car car = null;
		for (Car c : cars) {
			if (c.getId() == carId) {
				car = c;
			}
		}
		return car;
	}

	public void saveUser(User user) {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(user);
		transaction.commit();
		session.close();

	}

	public void saveRent(Rent rent) {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(rent);
		transaction.commit();
		session.close();

	}

	public List<Rent> getAllRents() {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT r FROM Rent r", Rent.class);
		List<Rent> rents = query.getResultList();
		transaction.commit();
		session.close();
		return rents;
	}

	public List<Car> getAllRCars() {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT c FROM Car c", Car.class);
		List<Car> cars = query.getResultList();
		transaction.commit();
		session.close();
		return cars;
	}

	public void saveCar(Car car) {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(car);
		transaction.commit();
		session.close();
	}

	public List<Rent> getAllNotFinishedRentsByCarId(int id) {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT r FROM Rent r WHERE r.autoId=?1 AND r.dateTo<?2", Rent.class);
		query.setParameter(1, id);
		query.setParameter(2, new Date());
		List<Rent> rents = query.getResultList();
		transaction.commit();
		session.close();
		return rents;
	}

	public void updateCar(Car car) {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		session.update(car);
		transaction.commit();
		session.close();
	}
}
