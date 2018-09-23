package package2;

import java.util.logging.Logger;

import package1.SuperClass;

public class SubClass extends SuperClass{
	static Logger logger = Logger.getLogger(SubClass.class.getName());
	SubClass()
	{
		logger.info("entry ");
		
	}
	public void privateSuperMethod()
	{
		logger.info("entry ");
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SubClass().mehdi();

	}

}
