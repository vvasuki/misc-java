package knightMoves;

import java.util.logging.Logger;

public class ProblemSolvedException extends Exception {
	private Square solutionSquare;
	
	protected int solutionCost=0; 
	private static Logger logger = Logger.getLogger(ProblemSolvedException.class.getName());
	
	ProblemSolvedException(Square square){
		solutionSquare = square;
		solutionCost=solutionSquare.countAncestors();
	}
	
	public void printSolution(){
		logger.info("solution cost:"+solutionCost);
	}
	

}
