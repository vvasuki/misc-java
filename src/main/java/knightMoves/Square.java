package knightMoves;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Square {
	private int x;

	private int y;

	private int f = 0, g = 0, h = 0;

	private Square parentSquare = null;

	private static Logger logger = Logger.getLogger(Square.class.getName());

	public void setXY(int iX, int iY) {
		x = iX;
		y = iY;

	}

	// public void setFGH(int )

	Square(int iX, int iY, Square parent) {
		setXY(iX, iY);
		this.parentSquare = parent;
		if (x == 0 && y == 0) {
			g = 0;
			//h = f = calculateHeuristic(ChessBoard.chessBoard.destSquare);
		} else if (parent != null) {
			g = parentSquare.g + 1;
			h = calculateHeuristic(ChessBoard.chessBoard.destSquare);
			f = g + h;
		}
	}

	private int getHVDdistance(Square dest) {
		int hvdDistance = 0;
		if (equals(dest))
			return hvdDistance;
		int xDiff = Math.abs(dest.x - x);
		int yDiff = Math.abs(dest.y - y);
		if (xDiff <= yDiff) {
			xDiff = yDiff;
		} else {
			yDiff = xDiff;
		}
		return xDiff;

	}

	public boolean equals(Object obj) {
		if (obj instanceof Square) {
			Square destSqr = (Square) obj;
			if (x == destSqr.x && y == destSqr.y)
				return true;
			else
				return false;
		} else
			return super.equals(obj);
	}

	private int calculateHeuristic(Square dest) {
		int iHeuristic = 0;
		if (equals(dest))
			return iHeuristic;
		double hvdDist = getHVDdistance(dest);
		iHeuristic = (int) Math.ceil(hvdDist / 2);
		if (getColor() != dest.getColor()) {
			iHeuristic++;
		}
		return iHeuristic;
	}

	private int getColor() {
		if ((x + y) % 2 == 0)
			return 0;
		return 1;
	}

	protected List getSuccessors() {
		List lstSuccessors = new ArrayList();
		Square sqr;
		sqr = new Square(x + 2, y + 1, this);
		lstSuccessors.add(sqr);

			sqr = new Square(x - 2, y + 1, this);
			lstSuccessors.add(sqr);

		sqr = new Square(x + 1, y + 2, this);
		lstSuccessors.add(sqr);

			sqr = new Square(x + 1, y - 2, this);
			lstSuccessors.add(sqr);

			sqr = new Square(x + 2, y - 1, this);
			lstSuccessors.add(sqr);
				sqr = new Square(x - 2, y - 1, this);
				lstSuccessors.add(sqr);
			sqr = new Square(x - 1, y + 2, this);
			lstSuccessors.add(sqr);
				sqr = new Square(x - 1, y - 2, this);
				lstSuccessors.add(sqr);

		return lstSuccessors;

	}

	public String toString() {
		return "X=" + x + " Y=" + y + "\n";
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public Square getParentSquare() {
		return parentSquare;
	}

	public void setParentSquare(Square parentSquare) {
		this.parentSquare = parentSquare;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int countAncestors(){
		Square square = this;
		int iAncestors=0;
		while(square!=null){
			//logger.info(square.toString());
			square=square.parentSquare;
			iAncestors++;
			
		}
		return iAncestors-1;
	}

}
