package vixAmd.sourceTicker;

import java.awt.Container;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

class StockTickerGUI extends JFrame
{
	private static final long serialVersionUID = 1L;

	Container contentPane = getContentPane();

	JTabbedPane tabbedPane = new JTabbedPane();

	JTextArea textArea;

	JTable table;

	JScrollPane scrollPane;

	StockValuesTable stockValuesTable;

	static Logger logger = Logger
	.getLogger(StockTickerGUI.class.getName());

	StockTickerGUI(StockTicker stockTicker)
	{
		logger.info("entering");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// contentPane.setLayout(new FlowLayout(FlowLayout.LEADING));
		setTitle("Stock Ticker");
		initTextArea();
		initTable(stockTicker);
		contentPane.add(tabbedPane);
		pack();
		// setSize(minimumSize());
	}

	private void initTextArea()
	{
		logger.info("entering");
		textArea = new JTextArea(20, 50);
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		tabbedPane.addTab("textView", scrollPane);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	}

	private void initTable(StockTicker stockTicker)
	{
		logger.info("entering");
		stockValuesTable = new StockValuesTable(stockTicker);
		table = new JTable(stockValuesTable);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane = new JScrollPane(table);
		tabbedPane.addTab("tableView", scrollPane);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	}

	void showStocks(StockTicker stockTicker)
	{
		logger.info("entering");
		long curTime;
		Calendar curDate = Calendar.getInstance(StockTicker.TIMEZONE);
		StringBuffer strCurDate;
		while (true)
		{
			stockTicker.getHTTPStockQuotes();
			curTime = System.currentTimeMillis();
			curDate.setTimeInMillis(curTime);
			strCurDate = new StringBuffer(10);
			strCurDate.append(curDate.get(Calendar.HOUR_OF_DAY));
			strCurDate.append(':');
			strCurDate.append(curDate.get(Calendar.MINUTE));
			strCurDate.append(':');
			strCurDate.append(curDate.get(Calendar.SECOND));
			textArea.setToolTipText("refreshed" + strCurDate);
			textArea.setText(stockTicker.toString());
			stockValuesTable.setTableValues();
			table.setToolTipText("refreshed" + strCurDate);
			table.repaint();
			StockTicker.threadSleep();

		}

		// initTable(stockTicker);
	}

	public static void main(String[] args)
	{
		logger.info("entering");
		StockTickerGUI stockTickerGUI = new StockTickerGUI(null);
		stockTickerGUI.show();
	}
}
