package csp;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sudoku extends CSP {
	private final static int PUZZLE_SIZE = 9;

	private static Logger logger = Logger.getLogger(Sudoku.class.getName());
	static {
		logger.setLevel(Level.ALL);
	}

	private static String FILE_PATH = "/nfs/rbo/home0/vvasuki/vishvas/work/ai/hw2/sudoku/";

	private static String[] FILES = { "puz-001.txt", "puz-002.txt",
			"puz-010.txt", "puz-015.txt", "puz-025.txt", "puz-026.txt",
			"puz-048.txt", "puz-051.txt", "puz-062.txt", "puz-076.txt",
			"puz-081.txt", "puz-082.txt", "puz-090.txt", "puz-095.txt",
			"puz-099.txt", "puz-100.txt" };

	private static List[] arrSudokuExamples = new ArrayList[4];

	private Variable[][] arrSquares = new Variable[PUZZLE_SIZE][PUZZLE_SIZE];

	private ArrayList lstValues = new ArrayList();

	private int iNumGuesses = 0;

	private Sudoku() {
		for (int i = 0; i < PUZZLE_SIZE; i++) {
			Value value = new Value(i + 1);
			lstValues.add(value);
		}
		for (int i = 0; i < PUZZLE_SIZE; i++) {
			for (int j = 0; j < PUZZLE_SIZE; j++) {
				Variable variable = new Square((ArrayList) lstValues.clone(),
						i, j);
				arrSquares[i][j] = variable;
				lstVariables.add(variable);
			}
		}
		addConstraints();

	}

	private Sudoku(InputStream in) throws IOException,
			InvalidAssignmentException {
		this();
		DataInputStream dataStream = new DataInputStream(in);
		char c;
		for (int i = 0; i < PUZZLE_SIZE; i++) {
			for (int j = 0; j < PUZZLE_SIZE; j++) {
				c = (char) dataStream.readByte();
				while (Character.isSpaceChar(c)) {
					c = (char) dataStream.readByte();

				}
				// logger.info("i:"+i+":j:"+j);
				if (c == '-') {
					// logger.info("char is " + c);
					continue;
				} else {
					Value value = (Value) lstValues.get(Integer
							.parseInt("" + c) - 1);
					// logger.info("value is "+value.toString());
					arrSquares[i][j].setValue(value);
				}
			}
			// read space
			c = (char) dataStream.readByte();
		}

	}

	private void addConstraints() {
		for (int i = 0; i < PUZZLE_SIZE; i++) {
			for (int j = 0; j < PUZZLE_SIZE; j++) {
				addRowConstraints(i, j);
				addColumnConstraints(i, j);
				addNeighborhoodConstraints(i, j);

			}
		}
		testConstraints();
	}

	private void testConstraints() {
		// logger.info("Number of constraints is " + lstConstraints.size());
		for (int i = 0; i < lstConstraints.size(); i++) {
			Constraint constraintA = (Constraint) lstConstraints.get(i);
			// logger.info(constraintA.toString());
			for (int j = i + 1; j < lstConstraints.size(); j++) {
				Constraint constraintB = (Constraint) lstConstraints.get(j);
				if (constraintA.equals(constraintB)) {
					logger.info("Alert! non unique constraints!");
					logger.info(constraintA.toString());
					logger.info(constraintB.toString());
				}

			}

		}

	}

	private void addRowConstraints(int x, int y) {
		for (int j = y + 1; j < PUZZLE_SIZE; j++) {
			addConstraint(arrSquares[x][y], arrSquares[x][j]);
		}

	}

	private void addColumnConstraints(int x, int y) {
		for (int i = x + 1; i < PUZZLE_SIZE; i++) {
			addConstraint(arrSquares[x][y], arrSquares[i][y]);
		}

	}

	private void addNeighborhoodConstraints(int x, int y) {
		int iNeighborhoodX = x / 3, iNeighborhoodY = y / 3;
		// logger.info("iNeighborhoodX is "+iNeighborhoodX );
		// logger.info("iNeighborhoodY is "+iNeighborhoodY );
		// logger.info("x is "+x);
		// logger.info("y is "+y);

		for (int i = x + 1; i < iNeighborhoodX * 3 + 3; i++) {
			for (int j = iNeighborhoodY * 3; j < iNeighborhoodY * 3 + 3; j++) {
				if (j == y)
					continue;
				addConstraint(arrSquares[x][y], arrSquares[i][j]);

			}

		}

	}

	private void addConstraint(Variable varA, Variable varB) {
		Constraint constraint = new Constraint(varA, varB);
		lstConstraints.add(constraint);
		varA.addConstraint(constraint);
		varB.addConstraint(constraint);
		// logger.info(constraint.toString());
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("\n");
		for (int i = 0; i < PUZZLE_SIZE; i++) {
			for (int j = 0; j < PUZZLE_SIZE; j++) {
				sb.append(arrSquares[i][j].toString());
				sb.append(' ');
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	private void plain_backtracking_search() throws ProblemSolvedException {
		List lstVars = (List) lstVariables.clone();
		iNumGuesses = 0;
		plain_try_assignment(lstVars);
	}

	private void plain_try_assignment(List lstVars)
			throws ProblemSolvedException {
		if (lstVars.isEmpty()) {
			throw new ProblemSolvedException();
		}
		Variable var = (Variable) lstVars.get(0);
		lstVars.remove(0);
		ArrayList lstOldVals = (ArrayList) var.lstDomain.clone();
		int iNumValues = lstOldVals.size();
		iNumGuesses += iNumValues - 1;
		for (int j = 0; j < iNumValues; j++) {
			Value value = (Value) lstOldVals.get(j);
			try {
				var.setValue(value);
			} catch (InvalidAssignmentException e) {
				continue;
			}
			try {
				plain_try_assignment(lstVars);
			} catch (ProblemSolvedException e) {
				// logger.info(toString());
				// var.lstDomain=lstOldVals;
				throw e;

			}

		}

		var.lstDomain = lstOldVals;
		lstVars.add(0, var);

	}

	private void mrv_backtracking_search() throws ProblemSolvedException {
		List lstVars = (List) lstVariables.clone();
		iNumGuesses = 0;
		plain_try_assignment(lstVars);
	}

	private void waterfall_try_assignment(List lstVars)
			throws ProblemSolvedException {
		if (lstVars.isEmpty()) {
			throw new ProblemSolvedException();
		}
		Collections.sort(lstVars);
		List lstOriginalDomains = saveOriginalDomains(lstVars);
		waterfall();
		Variable var = (Variable) lstVars.get(0);
		lstVars.remove(0);
		ArrayList lstOldVals = (ArrayList) var.lstDomain.clone();
		int iNumValues = lstOldVals.size();
		iNumGuesses += iNumValues - 1;
		for (int j = 0; j < iNumValues; j++) {
			Value value = (Value) lstOldVals.get(j);
			try {
				var.setValue(value);
			} catch (InvalidAssignmentException e) {
				continue;
			}
			try {
				waterfall_try_assignment((List) ((ArrayList) lstVars).clone());
			} catch (ProblemSolvedException e) {
				// logger.info(toString());
				// var.lstDomain=lstOldVals;
				throw e;

			}

		}

		var.lstDomain = lstOldVals;
		lstVars.add(0, var);
		restoreOriginalDomains(lstVars, lstOriginalDomains);

	}

	private List saveOriginalDomains(List lstVars) {
		List lstOriginalDomains = new ArrayList();
		for (int i = 0; i < lstVars.size(); i++) {
			Variable variable = (Variable) lstVars.get(i);
			lstOriginalDomains.add(variable.lstDomain.clone());
		}
		return lstOriginalDomains;
	}

	private void restoreOriginalDomains(List lstVars, List lstOriginalDomains) {

		for (int i = 0; i < lstVars.size(); i++) {
			Variable variable = (Variable) lstVars.get(i);
			variable.lstDomain = (ArrayList) lstOriginalDomains.get(i);
		}
	}

	private void waterfall_mrv_backtracking_search()
			throws ProblemSolvedException {
		List lstVars = (List) lstVariables.clone();
		Collections.sort(lstVars);
		iNumGuesses = 0;
		waterfall_try_assignment(lstVars);
		// logger.info(toString());
	}

	private void waterfall() {
		boolean bDomainsAltered = false;
		while (true) {
			ac3();
			if (pathConsistency()) {
				continue;
			}
			// if(fourConsistency()){
			// continue;
			// }
			break;
		}

	}

	private boolean ac3() {
		boolean bDomainsAltered = false;
		List lstVars = (List) lstVariables.clone();
		// logger.info("Entering ac3, vars number " + lstVars.size());
		while (!lstVars.isEmpty()) {
			Variable variable = (Variable) lstVars.get(0);
			// logger.info(variable.toString());
			List lstConstrainedVars = variable.lstConstraintVariables;
			int iNumConstrainedVars = lstConstrainedVars.size();
			for (int i = 0; i < iNumConstrainedVars; i++) {
				Variable otherVar = (Variable) lstConstrainedVars.get(i);
				if (ac3_removeInconsistentValues(otherVar, variable)
						&& !lstVars.contains(otherVar)) {
					lstVars.add(otherVar);
					bDomainsAltered = true;
				}

			}
			lstVars.remove(variable);
		}
		// logger.info("exiting ac3");
		return bDomainsAltered;

	}

	private boolean ac3_removeInconsistentValues(Variable v1, Variable v2) {
		if (v2.lstDomain.size() == 1 && v1.lstDomain.removeAll(v2.lstDomain)) {

			return true;
		} else
			return false;
	}

	private boolean pathConsistency() {
		boolean bDomainsAltered = false;
		List lstVars = (List) lstVariables.clone();
		// logger.info("Entering ac3, vars number " + lstVars.size());
		while (!lstVars.isEmpty()) {
			Variable variable = (Variable) lstVars.get(0);
			// logger.info(variable.toString());
			List lstConstrainedVars = variable.lstConstraintVariables;
			int iNumConstrainedVars = lstConstrainedVars.size();
			for (int i = 0; i < iNumConstrainedVars; i++) {
				Variable otherVar = (Variable) lstConstrainedVars.get(i);
				int iNumOtherConstrainedVars = otherVar.lstConstraintVariables
						.size();
				for (int j = 0; j < iNumOtherConstrainedVars; j++) {
					Variable otherOtherVar = (Variable) otherVar.lstConstraintVariables
							.get(j);
					if (pc_removeInconsistentValues(otherOtherVar, otherVar,
							variable)
							&& !lstVars.contains(otherOtherVar)) {
						lstVars.add(otherOtherVar);
						bDomainsAltered = true;
					}
				}

			}
			lstVars.remove(variable);
		}
		return bDomainsAltered;

	}

	private boolean pc_removeInconsistentValues(Variable v1, Variable v2,
			Variable v3) {
		if (v3.lstConstraintVariables.contains(v1) && v3.lstDomain.size() == 2
				&& v2.lstDomain.size() == 2
				&& v2.lstDomain.containsAll(v3.lstDomain)
				&& v1.lstDomain.removeAll(v3.lstDomain)) {

			return true;
		} else
			return false;

	}

	private boolean fourConsistency() {
		boolean bDomainsAltered = false;
		List lstVars = (List) lstVariables.clone();
		// logger.info("Entering ac3, vars number " + lstVars.size());
		while (!lstVars.isEmpty()) {
			Variable variable = (Variable) lstVars.get(0);
			// logger.info(variable.toString());
			List lstConstrainedVars = variable.lstConstraintVariables;
			int iNumConstrainedVars = lstConstrainedVars.size();
			for (int i = 0; i < iNumConstrainedVars; i++) {
				Variable otherVar = (Variable) lstConstrainedVars.get(i);
				int iNumOtherConstrainedVars = otherVar.lstConstraintVariables
						.size();
				for (int j = 0; j < iNumOtherConstrainedVars; j++) {
					Variable otherOtherVar = (Variable) otherVar.lstConstraintVariables
							.get(j);
					int iNumOtherOtherConstrainedVars = otherOtherVar.lstConstraintVariables
							.size();
					for (int k = 0; k < iNumOtherOtherConstrainedVars; k++) {
						Variable otherOtherOtherVar = (Variable) otherOtherVar.lstConstraintVariables
								.get(k);
						if (fc_removeInconsistentValues(otherOtherOtherVar,
								otherOtherVar, otherVar, variable)
								&& !lstVars.contains(otherOtherOtherVar)) {
							lstVars.add(otherOtherOtherVar);
							bDomainsAltered = true;
						}
					}
				}

			}
			lstVars.remove(variable);
		}
		return bDomainsAltered;

	}

	private boolean fc_removeInconsistentValues(Variable v1, Variable v2,
			Variable v3, Variable v4) {
		if (v4.lstConstraintVariables.contains(v1)
				&& v3.lstConstraintVariables.contains(v1)
				&& v3.lstDomain.size() == 3 && v2.lstDomain.size() == 3
				&& v4.lstDomain.size() == 4
				&& v2.lstDomain.containsAll(v3.lstDomain)
				&& v3.lstDomain.containsAll(v4.lstDomain)
				&& v1.lstDomain.removeAll(v4.lstDomain)) {

			return true;
		} else
			return false;

	}

	private static Sudoku make_sudoku_puzzle(InputStream in)
			throws IOException, InvalidAssignmentException {
		Sudoku sudoku = new Sudoku(in);
		// logger.info(sudoku.toString());
		return sudoku;
	}

	private static Sudoku make_sudoku_csp() {
		Sudoku sudoku = new Sudoku();
		return sudoku;
	}

	private static void solvePuzzle(String fileName) throws IOException,
			InvalidAssignmentException {
		Sudoku sudoku;
		// sudoku = make_sudoku_csp();
		FileInputStream fileStream = new FileInputStream(fileName);
		sudoku = make_sudoku_puzzle(fileStream);
		fileStream.close();
		try {
			// sudoku.plain_backtracking_search();
			// sudoku.mrv_backtracking_search();
			sudoku.waterfall_mrv_backtracking_search();
		} catch (ProblemSolvedException e) {
			// logger.info(sudoku.toString());
			// logger.info("fileName:" + fileName);
			logger.info("number of guesses:" + sudoku.iNumGuesses);
			// e.printStackTrace();
		}

	}

	private static void loadExamplePuzzles() throws IOException,
			InvalidAssignmentException {
		for (int i = 0; i < FILES.length; i++) {
			int iLevel = (Integer.parseInt(FILES[i].replaceAll("\\D", "")) - 1) / 25;
			// logger.info("level is " + iLevel);
			Sudoku sudoku;
			// sudoku = make_sudoku_csp();
			FileInputStream fileStream = new FileInputStream(FILE_PATH
					+ FILES[i]);
			sudoku = make_sudoku_puzzle(fileStream);
			fileStream.close();
			if (arrSudokuExamples[iLevel] == null) {
				arrSudokuExamples[iLevel] = new ArrayList();
			}
			arrSudokuExamples[iLevel].add(sudoku);

		}
	}

	private void randomizeNumbers() {
		Random generator = new Random(System.currentTimeMillis());
		// logger.info(lstValues.toString());
		Collections.shuffle(lstValues);
		// logger.info(lstValues.toString());
		for (int i = 0; i < PUZZLE_SIZE; i++) {
			Value value = (Value) lstValues.get(i);
			value.setValue(i + 1);
		}
	}

	private static Sudoku getRandomPuzzle(int iDifficulty) {
		Random generator = new Random(System.currentTimeMillis());
		int i = Math.abs(generator.nextInt())
				% arrSudokuExamples[iDifficulty].size();
		return (Sudoku) arrSudokuExamples[iDifficulty].get(i);

	}

	private void randomlyReflect() {

		Random generator = new Random(System.currentTimeMillis());
		int i = Math.abs(generator.nextInt()) % 4;
		if (i == 0) {
			for (int j = 0; j < lstVariables.size(); j++) {
				Square square = (Square) lstVariables.get(j);
				square.x = PUZZLE_SIZE - 1 - square.x;
			}

		} else if (i == 1) {
			for (int j = 0; j < lstVariables.size(); j++) {
				Square square = (Square) lstVariables.get(j);
				square.y = PUZZLE_SIZE - 1 - square.y;
			}
		} else if (i == 2) {
			for (int j = 0; j < lstVariables.size(); j++) {
				Square square = (Square) lstVariables.get(j);
				int iTmp = square.y;
				square.y = square.x;
				square.x = iTmp;
			}
		} else {

		}
		for (int j = 0; j < lstVariables.size(); j++) {
			Square square = (Square) lstVariables.get(j);
			arrSquares[square.x][square.y] = square;
		}

	}

	private static void makePuzzle(int iDifficulty) throws IOException,
			InvalidAssignmentException {
		loadExamplePuzzles();
		Sudoku sudoku = getRandomPuzzle(iDifficulty);
		// logger.info(sudoku.toString());
		sudoku.randomizeNumbers();
		// logger.info(sudoku.toString());
		sudoku.randomlyReflect();
		logger.info(sudoku.toString());
		try {
			// sudoku.plain_backtracking_search();
			// sudoku.mrv_backtracking_search();
			sudoku.waterfall_mrv_backtracking_search();
		} catch (ProblemSolvedException e) {
			logger.info(sudoku.toString());
			// logger.info("fileName:" + fileName);
			logger.info("number of guesses:" + sudoku.iNumGuesses);
			// e.printStackTrace();
		}

	}

	private static void runTests() throws IOException,
			InvalidAssignmentException {
		for (int i = 0; i < FILES.length; i++) {
			logger.info(FILES[i]);
			solvePuzzle(FILE_PATH + FILES[i]);
		}
	}

	public static void main(String args[]) throws IOException,
			InvalidAssignmentException {
		// runTests();
		for (int i = 0; i < 4; i++)
			makePuzzle(i);
	}

}
