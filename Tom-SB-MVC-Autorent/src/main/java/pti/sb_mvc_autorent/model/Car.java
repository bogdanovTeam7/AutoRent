package pti.sb_mvc_autorent.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cars")
public class Car {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "type")
	private String type;
	@Column(name = "day_price")
	private int dayPrice;
	@Column(name = "available")
	private boolean available;

	public Car() {
		super();
	}

	public Car(String type, int dayPrice, boolean available) {
		id = 0;
		this.type = type;
		this.dayPrice = dayPrice;
		this.available = available;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDayPrice() {
		return dayPrice;
	}

	public void setDayPrice(int dayPrice) {
		this.dayPrice = dayPrice;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "Car [id=" + id + ", type=" + type + ", dayPrice=" + dayPrice + ", available=" + available + "]";
	}

}
