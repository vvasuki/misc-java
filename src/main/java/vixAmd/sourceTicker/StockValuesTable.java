package vixAmd.sourceTicker;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;


class StockValuesTable extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(StockValuesTable.class
			.toString());

	private Object[][] values;

	private static final int MAX_COLS = 16;

	private static final int ROWS_PER_STOCKS = 3;

	private static final int SPECULATION_PRICE_COLUMN = 7;

	private List stockQuotes;

	StockValuesTable(StockTicker stockTicker) {
		logger.info("entering");
		stockQuotes = stockTicker.getStockQuotes();
		values = new Object[stockQuotes.size() * ROWS_PER_STOCKS][MAX_COLS];


	}

	public int getRowCount() {
		logger.info("entering");
		return values.length;
	}

	public int getColumnCount() {
		logger.info("entering");
		// return values[0].length;
		return MAX_COLS;
	}

	public Object getValueAt(int row, int column) {
		logger.info("entering");
		return values[row][column];
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		logger.info("entering");
		if (rowIndex % ROWS_PER_STOCKS == 0
				&& columnIndex == SPECULATION_PRICE_COLUMN) {
			Float speculatedPrice;
			try {
				speculatedPrice = Float.valueOf((String) aValue);
			} catch (NumberFormatException e1) {
				// e1.printStackTrace();
				return;
			}
			values[rowIndex][columnIndex] = aValue;
			fireTableCellUpdated(rowIndex, columnIndex);
			int stockNumber = rowIndex / ROWS_PER_STOCKS;
			try {
				StockQuote stockQuote = (StockQuote) ((StockQuote) (stockQuotes
						.get(stockNumber))).clone();
				Map detailsMap = new HashMap();
				detailsMap.put(StockQuote.DETAIL_LAST_TRADE, speculatedPrice);
				stockQuote.setDetailsMap(detailsMap);
				stockQuote.calculateProfit();
				columnIndex++;
				values[rowIndex][columnIndex] = new Float(stockQuote
						.getProfitNow());
				fireTableCellUpdated(rowIndex, columnIndex);

			} catch (CloneNotSupportedException e) {
				StockTicker.printAlertProgrammerMessage();
				e.printStackTrace();
			}
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		logger.info("entering");
		if (rowIndex % ROWS_PER_STOCKS == 0
				&& columnIndex == SPECULATION_PRICE_COLUMN)
			return true;
		else
			return false;
	}

	public String getColumnName(int column) {
		logger.info("entering");
		return super.getColumnName(column);
		// return columnNames[column];
	}

	void setTableValues() {
		logger.info("entering");
		// values = new Object[stockQuotes.length * 3][MAX_COLS];
		Iterator keyListIter;
		String detailName;
		int rowNum = 0;
		int colNum = 0;
		List detailKeyList;
		StockQuote stockQuote;
		Calendar buyDate;
		for (int i = 0; i < stockQuotes.size(); i++) {
			stockQuote = (StockQuote) stockQuotes.get(i);
			detailKeyList = stockQuote.getDetailKeyList();
			keyListIter = detailKeyList.iterator();
			values[rowNum][colNum] = stockQuote.getName();
			colNum++;
			values[rowNum][colNum] = stockQuote.getQuoteTime();
			buyDate = stockQuote.getBuyDate();
			if (buyDate != null) {
				colNum++;
				values[rowNum][colNum] = "bd: " + stockQuote.getBuyDateString();
				colNum++;
				values[rowNum][colNum] = "bp:" + stockQuote.getBuyPrice();
				colNum++;
				values[rowNum][colNum] = "bq:" + stockQuote.getBuyQuantity();
				colNum++;
				values[rowNum][colNum] = "br:" + stockQuote.getBrokerageRate();
				colNum++;
				values[rowNum][colNum] = "rs:" + stockQuote.getProfitNow();
			}
			rowNum++;
			colNum = 0;
			while (keyListIter.hasNext()) {
				detailName = (String) keyListIter.next();
				values[rowNum][colNum] = detailName;
				values[rowNum + 1][colNum] = stockQuote.getDetail(detailName)
						.toString();
				colNum++;
			}
			rowNum += 2;
			colNum = 0;
		}
		logger.info(toString());

	}

	public String toString() {
		logger.info("entering");
		StringBuffer valuesString = new StringBuffer(100);
		for (int i = 0; i < values.length; i++) {
			valuesString.append("row:");
			valuesString.append(i);
			valuesString.append('\n');
			for (int j = 0; j < values[i].length; j++) {
				valuesString.append(values[i][j]);
				valuesString.append('|');
			}
			valuesString.append('\n');
		}
		return valuesString.toString();
	}

}