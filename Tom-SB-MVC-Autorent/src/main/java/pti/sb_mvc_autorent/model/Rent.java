package pti.sb_mvc_autorent.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "rents")
public class Rent {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "auto_id")
	private int autoId;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "date_from")
	@Temporal(TemporalType.DATE)
	private Date dateFrom;
	@Column(name = "date_to")
	@Temporal(TemporalType.DATE)
	private Date dateTo;

	public Rent(int autoId, int userId, Date dateFrom, Date dateTo) {
		super();
		this.autoId = autoId;
		this.userId = userId;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}

	public Rent() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAutoId() {
		return autoId;
	}

	public void setAutoId(int autoId) {
		this.autoId = autoId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	@Override
	public String toString() {
		return "Rent [id=" + id + ", autoId=" + autoId + ", userId=" + userId + ", dateFrom=" + dateFrom + ", dateTo="
				+ dateTo + "]";
	}

}
