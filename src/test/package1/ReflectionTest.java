package package1;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionTest {
	private static Logger logger=Logger.getLogger(ReflectionTest.class.toString());
	static{
		logger.setLevel(Level.ALL);
	}
	public char c='a';
	public int a=12;
	public byte d=55;
	public int b=55;
public static void main(String[] args)
{
	ReflectionTest reflectionTest = new ReflectionTest();
	Field[] fields=reflectionTest.getClass().getFields();
	logger.info("entering "+fields.length);
	for(int i=0;i<fields.length;i++)
	{
		logger.info(fields[i].toString());
		logger.info(fields[i].getType().toString());
	}
}
}
