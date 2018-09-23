package package1;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SuperClass {
	static Logger logger = Logger.getLogger(SuperClass.class.getName());
	
	public SuperClass()
	{
		logger.setLevel(Level.FINEST);
		logger.info("entry");
		privateSuperMethod();
	}
	protected void mehdi()
	{
		logger.info("entry");
		privateSuperMethod();
	}

	protected void privateSuperMethod()
	{
		logger.info("entry superclasse");
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
