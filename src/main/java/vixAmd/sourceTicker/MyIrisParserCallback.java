package vixAmd.sourceTicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;

class MyIrisParserCallback extends ParserCallback
{
	/*
	 * INFO: {% Change=6.53,Previous Close(19-Jan-2006)=140.1, Best Sell
	 * Quantity=191.0, Low=143.0, Best Sell=149.8, Best Buy=149.25, Open=140.1,
	 * 52 Week High=171.3, Last Trade=149.25, High=152.7, Best Buy
	 * Quantity=77.0, Change=9.15, 52 Week Low=113.95 Volume=117340.0}
	 */
		Map detailsMap;

	List detailKeyList;

	String detailKey = "";

	Float detailValue;

	String quoteTime;

	static Logger logger = Logger
	.getLogger(MyIrisParserCallback.class.getName());

	boolean bGotTime = false;

	boolean bCollectData = false;

	MyIrisParserCallback()
	{
		logger.info("entering");
		detailsMap = new HashMap();
		detailKeyList = new ArrayList();
	}

	public void handleText(char[] data, int pos)
	{
		logger.info("entering");
		String htmlText = new String(data);
		htmlText = htmlText.trim();
		if (htmlText.length() == 0)
			return;
		if (htmlText.indexOf("Live NSE Quote") >= 0)
		{
			bCollectData = true;
			return;
		}
		if (!bCollectData)
			return;
		if (quoteTime == null)
		{
			quoteTime = htmlText;
			return;
		}
		if (htmlText.indexOf("--") >= 0)
		{
			bCollectData = false;
			return;
		}
		try
		{
			detailValue = new Float(Float.parseFloat(htmlText));
			detailsMap.put(detailKey, detailValue);
			detailKeyList.add(detailKey);
		}
		catch (NumberFormatException e)
		{
			// e.printStackTrace();
			if (detailKey.equals("Previous Close"))
				detailKey += htmlText;
			else
				detailKey = htmlText;
			// logger.info(detailKey);
		}

		// System.out.println(data);
	}
}
