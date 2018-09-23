package package1;

import java.util.logging.Logger;

public class SubClass extends SuperClass{
	private static Logger logger = Logger.getLogger(SubClass.class.getName());
	private int i;
	SubClass()
	{
		logger.info("entry ");

	}
	protected void privateSuperMethod()
	{
		logger.info("entry ");
		
	}
	
	class InnerClass
	{
		InnerClass()
		{
			logger.info("entry"+i);	
		}
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int i=0;
		class InnerMethodClass
		{
			InnerMethodClass()
			{
				logger.info("entry"+i);	
			}
		}
		//new SubClass().mehdi();
//		SubClass.InnerClass ic=new SubClass().new InnerClass();
		logger.info(""+new SubClass().i);
		logger.info(System.getProperties().toString());
	}

}

interface Interface
{
	
}

