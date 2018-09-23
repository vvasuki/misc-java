package vixAmd.sourceTicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

class StockTickerHelper {
	static Logger logger = Logger.getLogger(StockTickerHelper.class
			.getName());

	static void printArray(Object[] objects) {
		logger.info("entering");
		for (int i = 0; i < objects.length; i++)
			logger.info(objects[i].toString());
	}

	static Calendar getNextLastThursdayOfMonth() {
		logger.info("entering");
		// long curTimeInMillis = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance(StockTicker.TIMEZONE);
		Calendar calLastThursdayOfMonth = getLastThursdayOfMonth((Calendar) cal
				.clone());
		if (calLastThursdayOfMonth.before(cal)) {
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 7);
			calLastThursdayOfMonth = getLastThursdayOfMonth((Calendar) cal
					.clone());
		}
		// logger.info("Today : " +
		// sdf.format(calLastThursdayOfMonth.getTime()));
		return calLastThursdayOfMonth;

	}

	static Calendar getToday() {
		logger.info("entering");
		Calendar cal = Calendar.getInstance(StockTicker.TIMEZONE);
		return cal;

	}

	static Calendar getLastThursdayOfMonth(Calendar cal) {
		logger.info("entering");
		// long curTimeInMillis = System.currentTimeMillis();
		int maxDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + maxDayOfMonth
				- cal.get(Calendar.DAY_OF_MONTH));
		int lastThursdayOfTheMonth = 0;
		for (int i = maxDayOfMonth; lastThursdayOfTheMonth == 0; i--) {
			rewindOneDay(cal);
			// logger.info("Today : " + sdf.format(cal.getTime()));
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
				lastThursdayOfTheMonth = i;
		}

		return cal;

	}

	static String calendar2String(Calendar cal) {
		logger.info("entering");
		String DATE_FORMAT = "dMMMyyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(StockTicker.TIMEZONE);
		// logger.info("Today : " + sdf.format(cal.getTime()).toUpperCase());
		return sdf.format(cal.getTime()).toUpperCase();

	}

	static Calendar getLastTradingDay() {
		logger.info("entering");
		Calendar cal = Calendar.getInstance(StockTicker.TIMEZONE);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			while (true) {
				rewindOneDay(cal);
				// logger.info("Today : " + sdf.format(cal.getTime()));
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
					return cal;
			}
		else
			return cal;
	}

	static void rewindOneDay(Calendar cal) {
		logger.info("entering");
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
	}

	public static void main(String[] args) {
		// getLastThursdayOfMonth(Calendar.getInstance(TimeZone.getDefault()));
		// getNextLastThursdayOfMonth();
		// getNextLastThursdayOfMonth();
	}

}
