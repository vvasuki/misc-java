package vixAmd.sourceTicker;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author guest
 * 
 * 
 */
class StockQuote
{
	static Logger logger = Logger
	.getLogger(StockQuote.class.getName());

	
	private String name;

	private String quoteTime;

	private Calendar buyDate;
	
	private boolean isDerivative;

	private float buyPrice;

	private int buyQuantity;

	private float profitNow;

	private float brokerageRate;

	private float serviceTaxRate;

	private float turnOverFeeRate;

	private float securitiesTransactionRate;

	private List detailKeyList;

	/**
	 * 
	 */
		/**
	 * INFO: {% Change=6.53,Previous Close(19-Jan-2006)=140.1, Best Sell
	 * Quantity=191.0, Low=143.0, Best Sell=149.8, Best Buy=149.25, Open=140.1,
	 * 52 Week High=171.3, Last Trade=149.25, High=152.7, Best Buy
	 * Quantity=77.0, Change=9.15, 52 Week Low=113.95 Volume=117340.0}
	 */
		private Map detailsMap;

	static final String DETAIL_LAST_TRADE = "Last Trade";

	StockQuote(String stockName)
	{
		logger.info("entering");
		this.name = stockName;
	}

	StockQuote(String stockName, Calendar buyDate, float buyPrice,
			int buyQuantity, float brokerageRate, float serviceTaxRate,
			float turnOverFeeRate, float securitiesTransactionRate, boolean isDerivative)
	{
		logger.info("entering");
		this.name = stockName;
		this.buyDate = buyDate;
		this.buyPrice = buyPrice;
		this.buyQuantity = buyQuantity;
		this.brokerageRate = brokerageRate;
		this.serviceTaxRate = serviceTaxRate;
		this.turnOverFeeRate = turnOverFeeRate;
		this.securitiesTransactionRate = securitiesTransactionRate;
		this.isDerivative=isDerivative;
	}

	public Object clone() throws CloneNotSupportedException
	{
		logger.info("entering");
		StockQuote stockQuote = new StockQuote(name, buyDate, buyPrice,
				buyQuantity, brokerageRate, serviceTaxRate, turnOverFeeRate,
				securitiesTransactionRate,isDerivative);
		return stockQuote;
	}

	public String toString()
	{
		logger.info("entering");
		StringBuffer strQuote = new StringBuffer(100);
		Iterator keyListIter = detailKeyList.iterator();
		String detailName;
		strQuote.append(name);
		strQuote.append(": ");
		strQuote.append(quoteTime);
		if (buyDate != null)
		{
			strQuote.append(" buyDate:");
			strQuote.append(getBuyDateString());
			strQuote.append(" buyPrice:");
			strQuote.append(buyPrice);
			strQuote.append(" buyQuantity:");
			strQuote.append(buyQuantity);
			strQuote.append(" brokerage:");
			strQuote.append(brokerageRate);
			strQuote.append(" profitNow:");
			strQuote.append(profitNow);
			strQuote.append(" isDerivative:");
			strQuote.append(isDerivative);
		}
		strQuote.append("\n");
		while (keyListIter.hasNext())
		{
			detailName = (String)keyListIter.next();
			strQuote.append(detailName);
			strQuote.append(": ");
			strQuote.append(detailsMap.get(detailName));
			strQuote.append(" ");
		}

		return strQuote.toString();
	}

	StringBuffer getBuyDateString()
	{
		logger.info("entering");
		StringBuffer strBuf = new StringBuffer(15);
		strBuf.append(buyDate.get(Calendar.DAY_OF_MONTH));
		strBuf.append("/");
		strBuf.append(buyDate.get(Calendar.MONTH) + 1);
		strBuf.append("/");
		strBuf.append(buyDate.get(Calendar.YEAR));
		return strBuf;
	}

	public void setDetailsMap(Map detailsMap)
	{
		this.detailsMap = detailsMap;
	}

	private float calculateTransactionCost(float price)
	{
		logger.info("entering");
		float brokerage, cost;
		cost = 0f;
		brokerage = brokerageRate * price;
		cost += brokerage;
		cost += brokerage * serviceTaxRate;
		cost += turnOverFeeRate * price;
		cost += securitiesTransactionRate * price;

		return cost;
	}

	void calculateProfit()
	{
		logger.info("entering");
		Float flCurrentPrice = (Float)getDetail(DETAIL_LAST_TRADE);
		if (flCurrentPrice == null)
			return;
		float currentPrice = flCurrentPrice.floatValue();
		float buyingCost = calculateTransactionCost(buyPrice);
		float sellingCost = calculateTransactionCost(currentPrice);
		profitNow = (currentPrice - buyPrice - sellingCost - buyingCost)
				* buyQuantity;
	}

	public List getDetailKeyList()
	{
		logger.info("entering");
		return detailKeyList;
	}

	public void setDetailKeyList(List detailKeyList)
	{
		logger.info("entering");
		this.detailKeyList = detailKeyList;
	}

	public String getName()
	{
		logger.info("entering");
		return name;
	}

	public String getQuoteTime()
	{
		logger.info("entering");
		return quoteTime;
	}

	public void setQuoteTime(String quoteTime)
	{
		logger.info("entering");
		this.quoteTime = quoteTime;
	}

	Object getDetail(String detailName)
	{
		logger.info("entering");
		return detailsMap.get(detailName);
	}

	public Calendar getBuyDate()
	{
		logger.info("entering");
		return buyDate;
	}

	public float getBuyPrice()
	{
		logger.info("entering");
		return buyPrice;
	}

	public int getBuyQuantity()
	{
		logger.info("entering");
		return buyQuantity;
	}

	public float getProfitNow()
	{
		logger.info("entering");
		return profitNow;
	}

	public float getBrokerageRate()
	{
		logger.info("entering");
		return brokerageRate;
	}

	public boolean isDerivative() {
		logger.info("entering");
		return isDerivative;
	}

}

