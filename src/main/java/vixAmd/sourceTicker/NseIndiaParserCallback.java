package vixAmd.sourceTicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import sun.misc.Queue;

class NseIndiaParserCallback extends ParserCallback {
	static Logger logger = Logger
			.getLogger(NseIndiaParserCallback.class.getName());
	
	boolean bParsingIndicatesDataRetrievedSuccessfuly=false;

	Queue detailKeyQueue = new Queue();

	// No records found for selected security.
	/*
	 * <tr><th class=specialhead> Price & Turnover Information</th> </tr>
	 * </table>
	 * 
	 * <TABLE BORDER="0" ALIGN="CENTER" CELLPADDING="4" CELLSPACING="1"
	 * bgcolor="#FFFFFF" width="100%"> <tr><th class=specialhead2>Prev. Close</th><th class=specialhead2>Open</th><th class=specialhead2>High</th><th class=specialhead2>Low</th><th class=specialhead2>Average
	 * Price</th></tr><tr><td class=t1>151.05</td><td class=t1>154.00</td><td class=t1>156.70</td><td class=t1>146.05</td><td class=t1>151.13</td></tr></table>
	 * 
	 * <TABLE BORDER="0" ALIGN="CENTER" CELLPADDING="4" CELLSPACING="1"
	 * bgcolor="#FFFFFF" width="100%"> <tr><th class=specialhead2>Last Price</th><th class=specialhead2>Change
	 * from prev close</th><th class=specialhead2>% Change from prev close</th><th class=specialhead2>Total
	 * traded quantity</th><th class=specialhead2>Turnover in Rs.Lakhs</th></tr><tr><td class=t1>146.95</td><td class=t1>-4.10</td><td class=t1>-2.71</td><td class=t1>88789</td><td class=t1>134.19</td></tr></table>
	 * 
	 */
	Map detailsMap;

	List detailKeyList, detailKeyListRow1;

	/**
	 * sample contents: VISINDUS 20-Jan-2006 13:35 Last Trade 149.25 Change 9.15 %
	 * Change 6.53 Open 140.1 Previous Close (19-Jan-2006) 140.1 Low 143.00 High
	 * 152.70 52 Week Low 113.95 52 Week High 171.3 Best Buy 149.25 Best Sell
	 * 149.80 Best Buy Quantity 77 Best Sell Quantity 191 Volume 111,163 --
	 */
	Float detailValue;

	String quoteTime;

	boolean bCollectData = false;

	boolean bSecondRowStarted = false;

	NseIndiaParserCallback() {
		logger.info("entering");
		detailsMap = new HashMap();
		detailKeyList = new ArrayList();
		detailKeyListRow1 = new ArrayList();
	}

	public void handleText(char[] data, int pos) {
		logger.info("entering");
		String htmlText = new String(data);
		htmlText = htmlText.trim();
		if (htmlText.length() == 0)
			return;
		logger.info(htmlText);
		//if(htmlText.indexOf("No records found for selected contract. Another search.....")>=0)
		if (htmlText.indexOf("Price ") >= 0
				&& htmlText.indexOf("Information") >= 0) {
			bCollectData = true;
			bParsingIndicatesDataRetrievedSuccessfuly=true;
			return;
		}
		if (!bCollectData) {
			// As on 27-JAN-2006 16:00:10 Hours IST
			int i = htmlText.indexOf("As on ");
			if (i >= 0) {
				htmlText = htmlText.substring(i + "As on ".length(), i
						+ "As on 27-JAN-2006 16:00:10".length() + 1);
				quoteTime = htmlText;
			}
			return;
		}
		if (htmlText.indexOf("Intraday price chart") >= 0) {
			bCollectData = false;
			detailKeyList.addAll(detailKeyListRow1);
			return;
		}
		logger.info(htmlText);
		String detailKey;
		try {
			detailValue = new Float(Float.parseFloat(htmlText));
			detailKey = (String) detailKeyQueue.dequeue();
			detailsMap.put(detailKey, detailValue);
			if (bSecondRowStarted)
				detailKeyList.add(detailKey);
			else
				detailKeyListRow1.add(detailKey);
		} catch (NumberFormatException e) {
			// e.printStackTrace();
			if (htmlText.equals("Last Price")) {
				detailKey = StockQuote.DETAIL_LAST_TRADE;
				bSecondRowStarted = true;
			} else
				detailKey = htmlText;
			detailKeyQueue.enqueue(detailKey);
			// logger.info(detailKey);
		} catch (InterruptedException e) {
			StockTicker.printAlertProgrammerMessage();
			e.printStackTrace();
		}
	}
}
