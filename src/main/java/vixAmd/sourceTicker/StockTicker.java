/*
 *
 *This program gives you stock quotations from the website 
 *http://www.myiris.com .
 *Given the stock listing code, purchase-price, purchase-quantity and brokerage; 
 *profit is also calculated. 
 *The quotation and profit are refreshed each 1.5 minutes. 
 *This program works over the http protocol.
 *Proxies may be set by altering the runtime environment variables.
 *jre 1.4 is required. (http://java.sun.com/j2se/1.4.2/download.html)
 *
 *For usage instructions, execute this:
 *java -jar stockTicker.jar
 *
 Copyright (C) 2006 Vishvas Vasuki (vishvas@gmail.com)

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, visit http://www.gnu.org/copyleft/gpl.html .

 
 */
package vixAmd.sourceTicker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.html.parser.ParserDelegator;

public class StockTicker {
	static long refreshInterval = 90;

	static Logger logger = Logger
			.getLogger(StockTicker.class.getName());

	private List stockQuotes = new ArrayList();

	private static final String REFRESH_INTERVAL_STRING = "refreshInterval=";

	private static final String RUNTIME_ARGUMENT_STOCKFILE = "stockDetailsFile=";

	private static final String RUNTIME_ARGUMENT_LOGGERLEVEL = "loggerLevel=";

	static Locale LOCALE = new Locale("en_IN");

	static TimeZone TIMEZONE = TimeZone.getTimeZone("Asia/Calcutta");
	
	static Level loggerLevel=Level.ALL;

	private static ParserDelegator parserDelegator = new ParserDelegator();

	/*
	 * Source for derivative quotes. printout = new DataOutputStream
	 * (urlConn.getOutputStream ());
	 * 
	 */
	private void getNseIndiaDerivativeQuote(StockQuote stockQuote)
			throws MalformedURLException, IOException {
		logger.info("entering");
		String strStock = stockQuote.getName();
		// to
		// send:"http://nseindia.com/marketinfo/fo/foquote.jsp?flag=1&key=FUTSTKINFOSYSTCH30MAR2006--10MAR2006&symbol=INFOSYSTCH"
		// INFO:
		// http://nseindia.com/marketinfo/fo/foquote.jsp?flag=1&key=FUTSTKINFOSYSTCH30MAR2006--12MAR2006&symbol=INFOSYSTCH
		String quoteLookupURL = "http://nseindia.com/marketinfo/fo/foquote.jsp?flag=1&key=FUTSTK";
		Calendar calLastTradingDay = StockTickerHelper.getLastTradingDay();
		String strLastTradingDay_oldGuess;
		String strNextLastThursdayOfAMonth = StockTickerHelper
				.calendar2String(StockTickerHelper.getNextLastThursdayOfMonth());
		BufferedReader in = null;
		NseIndiaParserCallback nseIndiaParserCallback = null;
		int numberOfTries = 4;
		for (int i = 0; i < numberOfTries; i++) {
			strLastTradingDay_oldGuess = StockTickerHelper
					.calendar2String(calLastTradingDay);
			quoteLookupURL = "http://nseindia.com/marketinfo/fo/foquote.jsp?flag=1&key=FUTSTK";
			quoteLookupURL += strStock;
			quoteLookupURL += strNextLastThursdayOfAMonth;
			quoteLookupURL += "--";
			quoteLookupURL += strLastTradingDay_oldGuess;
			quoteLookupURL += "&symbol=";
			quoteLookupURL += strStock;
			logger.info(quoteLookupURL);
			nseIndiaParserCallback = new NseIndiaParserCallback();
			URL url = new URL(quoteLookupURL);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			parserDelegator.parse(in, nseIndiaParserCallback, false);
			if (nseIndiaParserCallback.bParsingIndicatesDataRetrievedSuccessfuly)
				break;
			in.close();
			StockTickerHelper.rewindOneDay(calLastTradingDay);
			logger.info("run time exception here." + strLastTradingDay_oldGuess
					+ " "
					+ StockTickerHelper.calendar2String(calLastTradingDay));
		}

		stockQuote.setQuoteTime(nseIndiaParserCallback.quoteTime);
		stockQuote.setDetailKeyList(nseIndiaParserCallback.detailKeyList);
		stockQuote.setDetailsMap(nseIndiaParserCallback.detailsMap);
		stockQuote.calculateProfit();
		// logger.info(detailsMap.toString());

		in.close();

	}

	//

	private void getNseIndiaStockQuote(StockQuote stockQuote)
			throws MalformedURLException, IOException {
		logger.info("entering");

		String strStock = stockQuote.getName();
		String quoteLookupURL = "http://www.nseindia.com/marketinfo/equities/quotesearch.jsp?submit1=go&series=EQ&flag=0&companyname=";
		URL url = new URL(quoteLookupURL + strStock);
		BufferedReader in = new BufferedReader(new InputStreamReader(url
				.openStream()));
		NseIndiaParserCallback nseIndiaParserCallback = new NseIndiaParserCallback();
		new ParserDelegator().parse(in, nseIndiaParserCallback, false);

		stockQuote.setQuoteTime(nseIndiaParserCallback.quoteTime);
		stockQuote.setDetailKeyList(nseIndiaParserCallback.detailKeyList);
		stockQuote.setDetailsMap(nseIndiaParserCallback.detailsMap);
		stockQuote.calculateProfit();
		// logger.info(detailsMap.toString());

		in.close();
	}

	private void getHTTPStockQuote(StockQuote stockQuote)
			throws MalformedURLException, IOException {
		logger.info("entering");
		// getMyIrisStockQuote(stockQuote);
		if (stockQuote.isDerivative())
			getNseIndiaDerivativeQuote(stockQuote);
		else
			getNseIndiaStockQuote(stockQuote);
	}

	/**
	 * @param strStock
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void getMyIrisStockQuote(StockQuote stockQuote)
			throws MalformedURLException, IOException {
		logger.info("entering");
		String strStock = stockQuote.getName();
		String quoteLookupURL = "http://www.myiris.com/shares/company/quoteShow.php?select=0&icode=";
		URL url = new URL(quoteLookupURL + strStock);
		BufferedReader in = new BufferedReader(new InputStreamReader(url
				.openStream()));
		MyIrisParserCallback myIrisParserCallback = new MyIrisParserCallback();
		new ParserDelegator().parse(in, myIrisParserCallback, false);

		stockQuote.setQuoteTime(myIrisParserCallback.quoteTime);
		stockQuote.setDetailKeyList(myIrisParserCallback.detailKeyList);
		stockQuote.setDetailsMap(myIrisParserCallback.detailsMap);
		stockQuote.calculateProfit();
		// logger.info(detailsMap.toString());

		in.close();
	}

	/**
	 * @return
	 */
	void getHTTPStockQuotes() {
		logger.info("entering");
		try {
			for (int i = 0; i < stockQuotes.size(); i++) {
				getHTTPStockQuote((StockQuote) stockQuotes.get(i));
				// logger.info(stockQuotes[i].toString());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param stockQuotes
	 */
	private void printStockQuotes(String[] args) {
		logger.info("entering");
		while (true) {
			getHTTPStockQuotes();
			for (int i = 0; i < 100; i++)
				System.out.println(toString());
			StockTicker.threadSleep();

		}

	}

	static void threadSleep() {
		logger.info("entering");
		try {
			Thread.sleep((long) (1000 * refreshInterval));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public String toString() {
		logger.info("entering");
		if (stockQuotes == null)
			return "";
		StringBuffer strStockQuotes = new StringBuffer();
		for (int i = 0; i < stockQuotes.size(); i++) {
			strStockQuotes.append(stockQuotes.get(i).toString());
			strStockQuotes.append('\n');
		}
		return strStockQuotes.toString();

	}

	private void getRefreshInterval(String strLine) {
		logger.info("entering");
		strLine = strLine.replaceFirst(REFRESH_INTERVAL_STRING, "");
		try {
			refreshInterval = Integer.parseInt(strLine);
		} catch (NumberFormatException e) {
			String strMessage = "The refresh interval line is flawed. Please correct it and try again.";
			System.out.println(strMessage);
			System.exit(1);
		}
	}

	private void getStockDataFromFile(String strFile) {
		logger.info("entering");
		try {
			BufferedReader bufReaderFile = new BufferedReader(new FileReader(
					strFile));
			// bufReaderFile.readLine();
			StringTokenizer strTok;
			String stockName = null;
			String buyDate = null;
			Calendar buyDateCal = null;
			float buyPrice = 0f;
			int buyQuantity = 0;
			float brokerageRate = 0f;
			StringTokenizer strTokDate;
			int iDate, iMonth, iYear;
			float serviceTaxRate = 0f;
			float turnOverFeeRate = 0f;
			float securitiesTransactionRate = 0f;
			String strQuoteType;
			boolean isDerivative;
			for (String stockLine = bufReaderFile.readLine(); stockLine != null; stockLine = bufReaderFile
					.readLine()) {
				stockName = null;
				buyDate = null;
				buyPrice = 0f;
				buyQuantity = 0;
				iDate = 0;
				iMonth = 0;
				iYear = 0;
				isDerivative = false;
				stockLine = stockLine.trim();
				if (stockLine.length() == 0)
					continue;
				if (stockLine.startsWith("#"))
					continue;
				if (stockLine.startsWith(REFRESH_INTERVAL_STRING)) {
					getRefreshInterval(stockLine);
					continue;
				}
				strTok = new StringTokenizer(stockLine, " ");
				if (strTok.hasMoreTokens())
					stockName = strTok.nextToken();
				try {
					if (strTok.hasMoreTokens()) {
						buyDate = strTok.nextToken();
						strTokDate = new StringTokenizer(buyDate, "/");
						buyDateCal = Calendar.getInstance(TIMEZONE);
						if (strTokDate.hasMoreTokens())
							iDate = Integer.parseInt(strTokDate.nextToken());
						if (strTokDate.hasMoreTokens())
							iMonth = Integer.parseInt(strTokDate.nextToken()) - 1;
						if (strTokDate.hasMoreTokens())
							iYear = Integer.parseInt(strTokDate.nextToken());
						buyDateCal.set(iYear, iMonth, iDate);
					}
					if (strTok.hasMoreTokens())
						buyPrice = Float.parseFloat(strTok.nextToken());
					if (strTok.hasMoreTokens())
						buyQuantity = Integer.parseInt(strTok.nextToken());
					if (strTok.hasMoreTokens())
						brokerageRate = Float.parseFloat(strTok.nextToken()) / 100;
					if (strTok.hasMoreTokens())
						serviceTaxRate = Float.parseFloat(strTok.nextToken()) / 100;
					if (strTok.hasMoreTokens())
						turnOverFeeRate = Float.parseFloat(strTok.nextToken()) / 100;
					if (strTok.hasMoreTokens())
						securitiesTransactionRate = Float.parseFloat(strTok
								.nextToken()) / 100;
					if (strTok.hasMoreTokens()) {
						strQuoteType = strTok.nextToken();
						if (strQuoteType.equalsIgnoreCase("derivative"))
							isDerivative = true;
						else
							isDerivative = false;
					}

				} catch (NumberFormatException e) {
					System.out.println("The following line in the file "
							+ strFile + " is invalid.");
					System.out.println(stockLine);
					System.out
							.println("Proper format: stockCode BuyDate BuyPrice buyQuantity brokerage%.");
					System.out.println("Proper date format: day/mon/year");
					System.out.println("Please correct this and run again.");
					System.exit(1);
					// e.printStackTrace();
				}

				stockQuotes.add(new StockQuote(stockName, buyDateCal, buyPrice,
						buyQuantity, brokerageRate, serviceTaxRate,
						turnOverFeeRate, securitiesTransactionRate,
						isDerivative));

			}
			bufReaderFile.close();
		} catch (FileNotFoundException e) {
			System.out
					.println("the file " + strFile + " could not be located.");
			// e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	static void printUsageMessage() {
		logger.info("entering");
		System.out.println("Usage: (stuff in [] is optional)");
		System.out.println("java [-DproxySet=true -DproxyHost=192.168.124.1 -DproxyPort=3128] -jar stockTicker.jar "+RUNTIME_ARGUMENT_STOCKFILE+"c:\\stocks.txt ["+RUNTIME_ARGUMENT_LOGGERLEVEL+"true]");
		System.out.println("you get the general idea.");
		System.exit(1);
	}

	/**
	 * Depricated. use java -DproxySet=true -DproxyHost=192.168.124.1
	 * -DproxyPort=3128 -jar progs/java/stockTicker.jar /home/guest/stocks.txt
	 */
	private static void setUpFirewall() {
		logger.info("entering");
		// InetAddress addr = InetAddress.getLocalHost();
		// System.out.println(addr.toString());

		System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyHost", "192.168.124.1");
		System.getProperties().put("proxyPort", "3128");
		// System.out.println(System.getProperties().toString());

	}

	private static void getArguments(StockTicker stockTicker, String[] args) {
		logger.info("entering");
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith(RUNTIME_ARGUMENT_STOCKFILE)) {
				args[i] = args[i].replaceFirst(RUNTIME_ARGUMENT_STOCKFILE, "");
				stockTicker.getStockDataFromFile(args[i]);
			} else if (args[i].startsWith(RUNTIME_ARGUMENT_LOGGERLEVEL)) {
				args[i] = args[i].replaceFirst(RUNTIME_ARGUMENT_LOGGERLEVEL, "");
				if(args[i].equalsIgnoreCase("true"))
					loggerLevel = Level.ALL;
				else
					loggerLevel = Level.OFF;
				setLoggerLevels();
			}
		}

	}
	
	private static void setLoggerLevels(){
		logger.setLevel(loggerLevel);
		MyIrisParserCallback.logger.setLevel(loggerLevel);
		NseIndiaParserCallback.logger.setLevel(loggerLevel);
		StockQuote.logger.setLevel(loggerLevel);
		StockTickerGUI.logger.setLevel(loggerLevel);
		StockTickerHelper.logger.setLevel(loggerLevel);
		StockValuesTable.logger.setLevel(loggerLevel);

		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("entering");
		if (args.length == 0)
			printUsageMessage();
		StockTicker stockTicker = new StockTicker();
		getArguments(stockTicker, args);

		// stockTicker.printStockQuotes(args);
		// stockTicker.getStockQuotes(args);
		StockTickerGUI stockTickerGUI = new StockTickerGUI(stockTicker);
		stockTickerGUI.setVisible(true);
		stockTickerGUI.showStocks(stockTicker);

	}

	public List getStockQuotes() {
		logger.info("entering");
		return stockQuotes;
	}

	static void printAlertProgrammerMessage() {
		logger.info("entering");
		System.out.println("Please alert program maintainer.");
	}

}
