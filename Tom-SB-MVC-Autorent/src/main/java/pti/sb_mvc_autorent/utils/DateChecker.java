package pti.sb_mvc_autorent.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateChecker {
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final Date from;
	private final Date to;

	public DateChecker(String fromAsString, String toAsString) {
		from = getDate(fromAsString);
		to = getDate(toAsString);
	}

	private Date getDate(String dateAsString) {
		Date date = null;
		if (dateAsString != null) {
			try {
				date = dateFormat.parse(dateAsString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public Date getFrom() {
		return from;
	}

	public Date getTo() {
		return to;
	}

	public String getFromAsString() {
		return dateFormat.format(from);
	}

	public String getToAsString() {
		return dateFormat.format(to);
	}

	public boolean isDateRangeInputCorrect() {
		boolean isCorrect = true;

		if (from == null || to == null) {
			isCorrect = false;
		} else if (from.getTime() > to.getTime()) {
			isCorrect = false;
		} else {
			String fromAsString = dateFormat.format(from);
			if (LocalDate.parse(fromAsString).isBefore(LocalDate.now())) {
				isCorrect = false;
			}
		}

		return isCorrect;
	}

	public Long getDays() {
		Long days = null;
		if (isDateRangeInputCorrect()) {
			long interval = to.getTime() - from.getTime();
			days = TimeUnit.DAYS.convert(interval, TimeUnit.MILLISECONDS) + 1;
		}
		return days;
	}

}
