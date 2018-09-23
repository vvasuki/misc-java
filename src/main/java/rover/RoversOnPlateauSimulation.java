import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/*
 * Note from Vishvas Vasuki to thoughtWorks.
 * This is a part of 3 file solution to the rovers on martian plateau problem.
 * The files are:
 * RoversOnPlateauSimulation.java
 * RoversOnPlateauSimulationTest.java
 * roverSimDesignComments.txt
 */

/**
 * Ours is a end-position and heading computing program.. to tell houston what
 * will happen if the plateau is thus, the rovers are situated thus and thus,
 * and the movement instructions given are thus. in the above conditions, where
 * will the rover be positioned in the end? what will their headings be? this
 * the simulation program says.
 * 
 * An object of RoversOnPlateauSimulation class represents a scenario simulated.
 * 
 */
public class RoversOnPlateauSimulation
{
	/**
	 * A list of rovers on the plateau.
	 */
	private List rovers = null;

	/**
	 * The plateau being simulated.
	 */
	private Plateau plateau;

	/**
	 * The controller object represents an entity which sends movement
	 * instructions to a certain rover in a certain plateau.
	 */
	private Controller houstonControl;

	/**
	 * If this variable is true, after each letter in the instruction sent to a
	 * rover, (eg: M in the line LMR) the modified position and heading is shown
	 * during simulation.
	 */
	private boolean showStepByStep = false;

	/**
	 * A list of instructions meant for the corresponding rovers on the plateau.
	 */
	private List instructionLines = null;
	

	/**
	 * This method reads the input supplied, initializes the plateau object, the
	 * rover objects, and records the instructions supplied for these rover
	 * objects.
	 * 
	 * @param input
	 * 
	 * An example of a valid argument to this method is: 5 5 1 2 N LMLMLMLMM
	 * 
	 * 
	 */
	public RoversOnPlateauSimulation(String input)
			throws InvalidInputException
	{
		if (input == null)
			throw new InvalidInputException();
		StringTokenizer inputTokens = new StringTokenizer(input, "\n");
		//System.out.println(inputTokens.countTokens());
		try
		{
			plateau = new Plateau(inputTokens.nextToken());
			//System.out.println(plateau.toString());
		}
		catch (NoSuchElementException e)
		{
			//e.printStackTrace();
			throw new InvalidInputException();
		}
		rovers = new ArrayList(10);
		instructionLines = new ArrayList(10);
		Rover rover;
		while (inputTokens.hasMoreTokens())
		{
			//System.out.println(inputTokens.countTokens());
			rover = new Rover(inputTokens.nextToken());
			rovers.add(rover);
			//System.out.println(rover.toString());
			try
			{
				try
				{
					isThereCollision(rover);
					isOffThePlateau(rover);
				}
				catch (RoverCollisionException e)
				{
					//e.printStackTrace();
					rover.setOperationalStatus(false);
					throw new RoverInoperationalException(e);
				}
				catch (OffPlateauException e)
				{
					//e.printStackTrace();
					rover.setOperationalStatus(false);
					throw new RoverInoperationalException(e);
				}
			}
			catch (RoverInoperationalException e)
			{
				//e.printStackTrace();
				/* If nasa lands one rover on top of another, or off the plateau; 
				 * what is to happen next?
				 * We shall assume that nasa continues 
				 * on the exploration agenda using the remaining rovers.
				 * Hence, this exception is ignored. 
				 */
			}
			//System.out.println(inputTokens.countTokens());
			if (inputTokens.hasMoreTokens())
				instructionLines.add(inputTokens.nextToken());
		}
		//System.out.println(rovers);
		//System.out.println(instructionLines);

	}

	/**
	 * The simulation parameters have already been ascertained using the constructor.
	 * This method simulates: 
	 * what happens next, after movement instrucions are transmitted?
	 * where will the rovers end up?
	 * @return
	 * It returns the final positions and headings of all the operational rovers.
	 * The output will be:
1 3 N
5 1 E

if the simulation conditions were given as:
5 5
1 2 N
LMLMLMLMM
3 3 E
MMRMMRMRRM
 
	 */
	public String simulate()
	{
		StringBuffer finalPositions = new StringBuffer(40);
		Iterator roverIterator = rovers.listIterator();
		Iterator instructionIterator = instructionLines.listIterator();
		houstonControl = new Controller();
		Rover rover;
		RoverPosition curPosition ;

		while(roverIterator.hasNext())
		{
			rover = (Rover)roverIterator.next();
			if(instructionIterator.hasNext())
				houstonControl.instructRover(this,rover,(String)instructionIterator.next());
			curPosition = rover.getCurPosition();
			finalPositions.append(curPosition.toString());
			finalPositions.append('\n');
		}
		return finalPositions.toString();
	}
	
	/**
	 * this method accepts a position and checks it against its list of current
	 * rover positions to detect collision.
	 * 
	 * @param rover
	 * @throws RoverCollisionException
	 */
	void isThereCollision(Rover rover) throws RoverCollisionException
	{
		Iterator roverIterator = rovers.listIterator();
		Rover listedRover;
		RoverPosition listedRoverCurPosition, curPosition ;
		curPosition = rover.getCurPosition();

		while(roverIterator.hasNext())
		{
			listedRover = (Rover)roverIterator.next();
			if(rover == listedRover)
				continue;
			listedRoverCurPosition = listedRover.getCurPosition();
			if(curPosition.getX()==listedRoverCurPosition.getX())
				if(curPosition.getY()==listedRoverCurPosition.getY())
					throw new RoverCollisionException(rover, listedRover);
		}
		
	}

	/**
	 * This method checks to see if the rover object is on or off the plateau.
	 * 
	 * @param rover
	 * @return
	 * @throws OffPlateauException
	 */
	void isOffThePlateau(Rover rover) throws OffPlateauException
	{
		plateau.isOffThePlateau(rover);

	}

	/**
	 * @return
	 */
	boolean isShowStepByStep()
	{
		return showStepByStep;
	}

	/**
	 * @param showStepByStep
	 */
	public void setShowStepByStep(boolean showStepByStep)
	{
		this.showStepByStep = showStepByStep;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(RoversOnPlateauSimulationTest.class);
		
	}

}

/**
 * Object of this class represents a rectangular plateu. The reason this class
 * and object is made is because there may concievably be multiple plateaus and
 * controllers. thus, future extensiblity is taken care of.
 * 
 * 
 */
class Plateau
{
	/**
	 * These coordinates are meant to store a plateau dimentions. It is assumed
	 * that the double format suffices to store these values.
	 * 
	 */
	private double xMax, yMax;

	private Plateau(double x, double y)
	{
		xMax = x;
		yMax = y;
	}

	/**
	 * This method instantiates a plateau object based on string input of the
	 * format: 5 5
	 * 
	 * @param plateauData
	 */
	Plateau(String plateauData) throws InvalidInputException
	{
		StringTokenizer plateauLineTokens = new StringTokenizer(plateauData,
				" ");
		try
		{
			xMax = Double.parseDouble(plateauLineTokens.nextToken());
			yMax = Double.parseDouble(plateauLineTokens.nextToken());
		}
		catch (NumberFormatException e)
		{
			//e.printStackTrace();
			throw new InvalidInputException(plateauData);
		}
		catch (NoSuchElementException e)
		{
			//e.printStackTrace();
			throw new InvalidInputException(plateauData);
		}
	}

	/**
	 * This method checks to see if the rover object is on or off the plateau.
	 * 
	 * @param rover
	 * @return
	 * @throws OffPlateauException
	 */
	void isOffThePlateau(Rover rover) throws OffPlateauException
	{
		RoverPosition curPosition = rover.getCurPosition();

		double x =curPosition.getX() ; 
		double y =curPosition.getY() ; 

		if ((x< 0) || (x > xMax)
				|| (y < 0) || (y > yMax))
			throw new OffPlateauException(rover);
	}

	public String toString()
	{
		return String.valueOf(xMax) + " " + String.valueOf(yMax);
	}

}

/**
 * Object of this class encapsulates a rover's position and heading. 
 *
 */
class RoverPosition
{
	/**
	 * These coordinates are meant to store a rover's position on the plateau.
	 * It is assumed that the double format suffices to store these values.
	 * 
	 */
	private double x, y;

	/**
	 * is the rover facing N, S, E or W? One of these values are meant to be
	 * stored here.
	 */
	private char heading;

	RoverPosition(double xInit, double yInit, char headingInit)
	{
		x = xInit;
		y = yInit;
		heading = headingInit;
	}

	/**
	 * This method returns a RoverPosition object whose elements are initialised
	 * with data gleaned from a string argument passed to it.
	 * 
	 * @param roverPositionHeading
	 *            Example: 1 2 N
	 * @throws InvalidInputException
	 */
	RoverPosition(String roverPositionHeading) throws InvalidInputException
	{
		StringTokenizer coordinates = new StringTokenizer(roverPositionHeading,
				" ");
		try
		{
			x = Double.parseDouble(coordinates.nextToken());
			y = Double.parseDouble(coordinates.nextToken());
			heading = coordinates.nextToken().charAt(0);
		}
		catch (NumberFormatException e)
		{
			//e.printStackTrace();
			throw new InvalidInputException(roverPositionHeading);
		}
		catch (NoSuchElementException e)
		{
			//e.printStackTrace();
			throw new InvalidInputException(roverPositionHeading);
		}

	}

	public Object clone()
	{
		return new RoverPosition(x, y, heading);
	}

	public String toString()
	{
		return String.valueOf(x) + " " + String.valueOf(y) + " " + heading;
	}

	/**
	 * @return Returns the heading.
	 */
	public char getHeading()
	{
		return heading;
	}

	/**
	 * @param heading The heading to set.
	 */
	public void setHeading(char heading)
	{
		this.heading = heading;
	}

	/**
	 * @return Returns the x.
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * @param x The x to set.
	 */
	public void setX(double x)
	{
		this.x = x;
	}

	/**
	 * @return Returns the y.
	 */
	public double getY()
	{
		return y;
	}

	/**
	 * @param y The y to set.
	 */
	public void setY(double y)
	{
		this.y = y;
	}
	
}

/**
 * Objects of this class represent rovers.
 * 
 */
class Rover
{
	/**
	 * Rover's initial position.
	 */
	private RoverPosition initPosition;

	/**
	 * Rover's current position.
	 */
	private RoverPosition curPosition;

	/**
	 * Is this rover operational? Has it undergone some collision; or has it
	 * rolled off the plateau? This variable answers the above.
	 */
	private boolean isOperational = true;

	void setOperationalStatus(boolean status)
	{
		isOperational = status;
	}

	/**
	 * Instantiates rover object given input of the form: 1 2 N
	 * 
	 * @param string
	 */
	Rover(String roverPositionHeading) throws InvalidInputException
	{
		initPosition = new RoverPosition(roverPositionHeading);
		curPosition = (RoverPosition) initPosition.clone();
	}

	/**
	 * This method responds to a single comand letter, and recalculates position
	 * and heading. if simulation.showStepByStep is enabled, logs are printed
	 * after each movement. After each move, the simulation.isThereCollision
	 * method and the simulation.offThePlateau method are called. this method
	 * has no effect if the rover is not operational.
	 * 
	 * this position/ heading calculating mechanism belongs in the rover class,
	 * because they are most directly related to the rover.
	 * 
	 * @param sim
	 * @param instruction
	 * @throws RoverCollisionException
	 * @throws InvalidInstructionException
	 * @throws OffPlateauException
	 * 
	 * 
	 */
	void move(RoversOnPlateauSimulation sim, char instruction)
			throws InvalidInstructionException, RoverInoperationalException
	{
		if (!isOperational)
			throw new RoverInoperationalException();
		double x =curPosition.getX() ; 
		double y =curPosition.getY() ;
		char heading = curPosition.getHeading();
		
		switch (instruction)
		{
		case 'L':
			switch (heading)
			{
			case 'N':
				heading = 'W';
				break;
			case 'W':
				heading = 'S';
				break;
			case 'S':
				heading = 'E';
				break;
			case 'E':
				heading = 'N';
				break;
			}
			break;
		case 'R':
			switch (heading)
			{
			case 'N':
				heading = 'E';
				break;
			case 'E':
				heading = 'S';
				break;
			case 'S':
				heading = 'W';
				break;
			case 'W':
				heading = 'N';
				break;
			}
			break;
		case 'M':
			switch (heading)
			{
			case 'N':
				y++;
				break;
			case 'E':
				x++;
				break;
			case 'S':
				y--;
				break;
			case 'W':
				x--;
				break;
			}
			break;
		default:
			throw new InvalidInstructionException(instruction);
		}
		try
		{
			curPosition.setX(x);
			curPosition.setY(y);
			curPosition.setHeading(heading);
			sim.isThereCollision(this);
			sim.isOffThePlateau(this);
		}
		catch (RoverCollisionException e)
		{
			//e.printStackTrace();
			isOperational = false;
			throw new RoverInoperationalException(e);
		}
		catch (OffPlateauException e)
		{
			//e.printStackTrace();
			isOperational = false;
			throw new RoverInoperationalException(e);
		}
		finally
		{
			if (sim.isShowStepByStep())
				System.out.println("New position and heading is now : "
						+ curPosition.toString());
		}
	}

	/**
	 * The returned object is cloned for high object security.
	 * @return Returns the curPosition.
	 */
	RoverPosition getCurPosition()
	{
		return (RoverPosition)curPosition.clone();
	}

	/**
	 * The returned object is cloned for high object security.
	 * @return Returns the initPosition.
	 */
	RoverPosition getInitPosition()
	{
		return (RoverPosition)initPosition.clone();
	}
	
	public String toString()
	{
		return initPosition.toString() + "\n" + curPosition.toString(); 
	}
}

/**
 * The controller object represents an entity which sends movement instructions
 * to a certain rover in a certain plateau.
 * 
 * The reason this class and object is made is because there may concievably be
 * multiple plateaus and controllers. thus, future extensiblity is taken care
 * of.
 * 
 * 
 */
class Controller
{
	/**
	 * @param rover
	 * @param instructionLine
	 *            this method's business is to serially transmit instructions to
	 *            the target rover. that is, it splits the
	 *            movement-instruction-line into movement tokens. That is: if
	 *            the movement-instruction-line is LMLMLMLMM, this method sends
	 *            L, M, L etc serially to the rover, through its move method.
	 */
	void instructRover(RoversOnPlateauSimulation sim, Rover rover,
			String instructionLine)
	{
		int instructionSize = instructionLine.length();
		for (int i = 0; i < instructionSize; i++)
		{
			try
			{
				rover.move(sim, instructionLine.charAt(i));
			}
			catch (InvalidInstructionException e)
			{
				// this exception is ignored..
				// the next instruction is sent.
			}
			catch (RoverInoperationalException e)
			{
				// this exception is ignored..
				// The next rover is dealt with.
				return;
			}
		}
	}
}

/**
 * This exception is thrown when rover, in following its instructions, rolls off
 * the plateau.
 * 
 */
class OffPlateauException extends Exception
{
	OffPlateauException(Rover rover)
	{
		RoverPosition initPosition = rover.getInitPosition();

		String msg = "Rover with initial position: " + initPosition.getX()
				+ " " + initPosition.getY()
				+ "\n has fallen off the plateau.\n";
		System.out.println(msg);
	}
}

/**
 * This exception is thrown when rover, in following its instructions, collides
 * with another rover.
 * 
 */
class RoverCollisionException extends Exception
{
	RoverCollisionException(Rover rover1, Rover rover2)
	{
		RoverPosition initPosition1 = rover1.getInitPosition();
		RoverPosition initPosition2 = rover2.getInitPosition();

		String msg = "Rover with initial position: " + initPosition1.getX()
				+ " " + initPosition1.getY()+ "\n and \n"
				+ initPosition2.getX()+ " " + initPosition2.getY()
				+ "\n have collided.\n";
		System.out.println(msg);
	}
}

/**
 * This exception is thrown when rover, in following its instructions, collides
 * with another rover or falls off the plateau, rendering the rover
 * inoperational.
 * 
 */
class RoverInoperationalException extends Exception
{
	RoverInoperationalException(RoverCollisionException e)
	{
		String msg = "Both are now rendered inoperable.\n ";
		System.out.println(msg);
	}

	RoverInoperationalException(OffPlateauException e)
	{
		String msg = "This rover is now rendered inoperable.\n ";
		System.out.println(msg);
	}

	RoverInoperationalException()
	{
		super();
	}
}

/**
 * 
 * This exception is thrown when the input is invalid. An example of valid input
 * is:
 * 
 * 5 5 1 2 N LMLMLMLMM
 * 
 */
class InvalidInputException extends Exception
{
	InvalidInputException()
	{
		String msg = "The input provided for the simulation is invalid.\n";
		System.out.println(msg);

	}

	InvalidInputException(String c)
	{
		String msg = "The input: " + c + " is invalid.\n";
		System.out.println(msg);
	}

}

/**
 * This exception is thrown when an invalid instruction is supplied to the
 * rover. Valid instructions are M, L and R.
 * 
 */
class InvalidInstructionException extends Exception
{
	InvalidInstructionException(char c)
	{
		String msg = "The command: " + c + " is invalid.\n";
		System.out.println(msg);
	}

}
