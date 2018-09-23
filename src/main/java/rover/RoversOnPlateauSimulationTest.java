import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
/*
 * Note from Vishvas Vasuki to thoughtWorks.
 * This is a part of 3 file solution to the rovers on martian plateau problem.
 * The files are:
 * RoversOnPlateauSimulation.java
 * RoversOnPlateauSimulationTest.java
 * roverSimDesignComments.txt
 */
/**
 * @author guest
 *This class contains junit test methods for 
 *methods in the classes in the file RoversOnPlateauSimulation.java .
 */
public class RoversOnPlateauSimulationTest extends TestCase
{

	/*
	 * Test method for 'RoversOnPlateauSimulation.RoversOnPlateauSimulation(String)'
	 */
	public void testRoversOnPlateauSimulation()
	{
		try
		{
			String simInput = 
				"5 5\n"+
				"1 2 N\n"+
				"LMLMLMLMM\n"+
				"3 3 E\n"+
				"MMRMMRMRRM\n"
				;
			String expectedOutput = 
				"1.0 3.0 N\n"
				+"5.0 1.0 E\n";
			RoversOnPlateauSimulation sim = null;
			sim = new RoversOnPlateauSimulation(simInput);
			String simOutput = sim.simulate ();
			assertEquals(expectedOutput,simOutput);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	/*
	 * Test method for 'RoversOnPlateauSimulation.isThereCollision(Rover)'
	 */
	public void testIsThereCollision()
	{
		try
		{
			String simInput = 
				"5 5\n"+
				"1 2 N\n"+
				"LMLMLMLMM\n"+
				"3 3 E\n"+
				"MMRMMRMRRM\n"
				;
			String expectedOutput = 
				"1.0 3.0 N\n"
				+"5.0 1.0 E\n";
			RoversOnPlateauSimulation sim = null;
			sim = new RoversOnPlateauSimulation(simInput);
			Rover rover = new Rover("5 56.33 N");
			try
			{
				sim.isThereCollision(rover);
			}
			catch (RoverCollisionException e)
			{
				e.printStackTrace();
				fail();
			}
			rover = new Rover("3 3 N");
			try
			{
				sim.isThereCollision(rover);
				fail();
			}
			catch (RoverCollisionException e)
			{
				//this is expected.
			}
		}
		catch (InvalidInputException e)
		{
			e.printStackTrace();
			fail();
		}

	}

	/*
	 * Test method for 'Plateau.Plateau(String)'
	 */
	public void testPlateauString()  
	{
		try
		{
			Plateau plateau = new Plateau("5 a");
		}
		catch (InvalidInputException e)
		{
			//this is expected.
		}
		try
		{
			Plateau plateau = new Plateau("5 34.6676");
		}
		catch (InvalidInputException e)
		{
			e.printStackTrace();
			fail();
		}

	}

	/*
	 * Test method for 'Plateau.isOffThePlateau(Rover)'
	 */
	public void testIsOffThePlateau() 
	{
		try
		{
			Plateau plateau = new Plateau("5 34.6676");
			Rover rover = new Rover("4 23.44 N");
			try
			{
				plateau.isOffThePlateau(rover);
				rover = new Rover("4 34.6676 N");
				plateau.isOffThePlateau(rover);
			}
			catch (OffPlateauException e)
			{
				e.printStackTrace();
				fail();
			}
			rover = new Rover("4 35.44 N");
			try
			{
				plateau.isOffThePlateau(rover);
				fail(); 
			}
			catch (OffPlateauException e)
			{
				//expected
			}
			rover = new Rover("4 -35.44 N");
			try
			{
				plateau.isOffThePlateau(rover);
				fail(); 
			}
			catch (OffPlateauException e)
			{
				//expected
			}
			
		}
		catch (InvalidInputException e)
		{
			e.printStackTrace();
			fail();
		}
	}

	/*
	 * Test method for 'Plateau.toString()'
	 */
	public void testPlateauToString()
	{
		try
		{
			Plateau plateau = new Plateau("5 34.6676");
			assertEquals("5.0 34.6676",plateau.toString());
		}
		catch (InvalidInputException e)
		{
			e.printStackTrace();
			fail();
		}

	}

	/*
	 * Test method for 'RoverPosition.RoverPosition(String)'
	 */
	public void testRoverPositionString() 
	{
		try
		{
			new RoverPosition("5 56.33 N");
		}
		catch (InvalidInputException e)
		{
			e.printStackTrace();
			fail();
		}
		try
		{
			new RoverPosition("5 56.33 n");
		}
		catch (InvalidInputException e)
		{
			//this is expected.
		}
		
	}

	/*
	 * Test method for 'RoverPosition.clone()'
	 */
	public void testClone()
	{
		try
		{
			RoverPosition pos=new RoverPosition("5 56.33 N");
			assertNotSame(pos,pos.clone());
		}
		catch (InvalidInputException e)
		{
			e.printStackTrace();
		}
	
	}

	/*
	 * Test method for 'RoverPosition.toString()'
	 */
	public void testRoverToString()
	{
		try
		{
			RoverPosition pos=new RoverPosition("5 56.33 N");
			assertEquals("5.0 56.33 N",pos.toString());
		}
		catch (InvalidInputException e)
		{
			e.printStackTrace();
		}

	}

	/*
	 * Test method for 'Rover.Rover(String)'
	 */
	public void testRover() 
	{
		try
		{
			new Rover("5 56.33 N");
		}
		catch (InvalidInputException e)
		{
			e.printStackTrace();
			fail();
		}
		try
		{
			new Rover("5 56.33 n");
		}
		catch (InvalidInputException e)
		{
			//this is expected.
		}
		
	}

	/*
	 * Test method for 'Rover.move(RoversOnPlateauSimulation, char)'
	 */
	public void testMove()
	{
		try
		{
			String simInput = 
				"5 5\n"+
				"1 2 N\n"+
				"LMLMLMLMM\n"+
				"3 3 E\n"+
				"MMRMMRMRRM\n"
				;
			String expectedOutput = 
				"1.0 3.0 N\n"
				+"5.0 1.0 E\n";
			RoversOnPlateauSimulation sim = null;
			sim = new RoversOnPlateauSimulation(simInput);
			sim.setShowStepByStep(true);
			Rover rover = new Rover("1 4 N");
			try
			{
				rover.move(sim,'l');
				fail();
			}
			catch (InvalidInstructionException e)
			{
				//e.printStackTrace();
				//this is expected.
			}
			catch (RoverInoperationalException e)
			{
				e.printStackTrace();
				fail();
			}
			rover = new Rover("5 1 E");
			try
			{
				rover.move(sim,'M');
				fail();
			}
			catch (InvalidInstructionException e)
			{
				e.printStackTrace();
				fail();
			}
			catch (RoverInoperationalException e)
			{
				//e.printStackTrace();
				//this is expected.
			}
			rover = new Rover("5 1 E");
			try
			{
				rover.move(sim,'L');
				rover.move(sim,'M');
				rover.move(sim,'R');
				assertEquals("5.0 1.0 E\n"+"5.0 2.0 E",rover.toString());
			}
			catch (InvalidInstructionException e)
			{
				e.printStackTrace();
				fail();
			}
			catch (RoverInoperationalException e)
			{
				fail();
			
			}
		}
		catch (InvalidInputException e)
		{
			e.printStackTrace();
			fail();
		}

	}


	/*
	 * Test method for 'Controller.instructRover(RoversOnPlateauSimulation, Rover, String)'
	 */
	public void testInstructRover()
	{
		try
		{
			String simInput = 
				"5 5\n"+
				"1 2 N\n"+
				"LMLMLMLMM\n"+
				"3 3 E\n"+
				"MMRMMRMRRM\n"
				;
			String expectedOutput = 
				"1.0 3.0 N\n"
				+"5.0 1.0 E\n";
			RoversOnPlateauSimulation sim = null;
			sim = new RoversOnPlateauSimulation(simInput);
			sim.setShowStepByStep(true);
			Rover rover = new Rover("5 1 E");
			Controller isro = new Controller();
			isro.instructRover(sim,rover,"LMR");
			assertEquals("5.0 1.0 E\n"+"5.0 2.0 E",rover.toString());
		}
		catch (InvalidInputException e)
		{
			e.printStackTrace();
			fail();
		}

	}

	
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(RoversOnPlateauSimulationTest.class);
		
	}

}
