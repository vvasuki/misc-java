package knightMoves;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChessBoard {

	private static int BOARD_SIZE = 5;

	private static Logger logger = Logger.getLogger(ChessBoard.class.getName());
	static {
		logger.setLevel(Level.ALL);
	}

	private static List lstNodesExpandedRecord = new ArrayList(),
			lstRuntimeRecord = new ArrayList(),
			lstSolutionCostRecord = new ArrayList();

	private static Random randGen = new Random();

	private Square initSquare = new Square(0, 0, null);

	protected Square destSquare;

	protected static ChessBoard chessBoard;

	private List lstFringeSquares = new ArrayList();

	private int iSquaresExpanded = 0;

	private long runTime = 0;

	public ChessBoard() {
		lstFringeSquares.add(initSquare);
	}

	private void generateProblem() {
		int iX = randGen.nextInt() % BOARD_SIZE;
		int iY = randGen.nextInt() % BOARD_SIZE;
		// iX = 2;
		// iY = 2;
		if (iX < 0)
			iX = -iX;
		if (iY < 0)
			iY = -iY;
		destSquare = new Square(iX, iY, null);
		logger.info("iX and iY are " + iY + " and " + iX);
	}

	private void insertSquare(Square square) throws ProblemSolvedException {
		int insertionPosition = 0;
		int iListElements = lstFringeSquares.size();
		Square squareToTest;
		for (int i = 0; i < iListElements; i++) {
			squareToTest = (Square) lstFringeSquares.get(i);
			if (squareToTest.getF() >= square.getF()) {
				insertionPosition = i;
				break;
			}
		}
		lstFringeSquares.add(insertionPosition, square);

	}

	private void insertSquares(List lstSuccessors)
			throws ProblemSolvedException {
		Iterator successorsIterator = lstSuccessors.iterator();
		while (successorsIterator.hasNext()) {
			insertSquare((Square) successorsIterator.next());
		}
	}

	private void solveProblem() throws ProblemSolvedException {
		List lstSuccessors, lstSquaresToExpand;
		Square currSquare;
		if (initSquare.equals(destSquare))
			throw new ProblemSolvedException(initSquare);
		while (true) {
			lstSquaresToExpand = (List) ((ArrayList) lstFringeSquares).clone();
			Iterator expandSquaresIterator = lstSquaresToExpand.iterator();
			while (expandSquaresIterator.hasNext()) {
				currSquare = (Square) expandSquaresIterator.next();
				if (currSquare.equals(destSquare)) {
					throw new ProblemSolvedException(currSquare);
				}
				iSquaresExpanded++;
				// logger.info(currSquare.toString());
				lstFringeSquares.remove(currSquare);
				lstSuccessors = currSquare.getSuccessors();
				insertSquares(lstSuccessors);
			}
			// lstSuccessors=
		}
	}

	private static void getMetrics() {
		chessBoard = new ChessBoard();
		chessBoard.generateProblem();
		long initTime = System.currentTimeMillis();
		try {
			chessBoard.solveProblem();
		} catch (ProblemSolvedException e) {
			// e.printSolution();
			long endTime = System.currentTimeMillis();
			chessBoard.runTime = endTime - initTime;
			// logger.info("Solution cost:"+e.solutionCost);
			// logger.info("Runtime:"+chessBoard.runTime);
			// logger.info("Nodes expanded:"+chessBoard.iSquaresExpanded);
			lstNodesExpandedRecord.add(new Long(chessBoard.iSquaresExpanded));
			lstRuntimeRecord.add(new Long(chessBoard.runTime));
			lstSolutionCostRecord.add(new Long(e.solutionCost));
			// e.printStackTrace();
		}

	}

	private static String toMatlabFormat(List record) {
		String strRecords = "";
		for (int i = 0; i < record.size(); i++) {
			long recordItem;
			Long recordObject = (Long) record.get(i);
			recordItem = recordObject.longValue();
			strRecords += recordItem;
			strRecords += " ";
		}
		return strRecords;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			getMetrics();
		}
		logger.info("SolCost:"+toMatlabFormat(lstSolutionCostRecord) + "\n");
		logger.info("SqrExp:"+toMatlabFormat(lstNodesExpandedRecord) + "\n");
		logger.info("Runtime:"+toMatlabFormat(lstRuntimeRecord) + "\n");

	}

}
