package package1;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ByteBufferTest {
	private static Logger logger=Logger.getLogger(ByteBufferTest.class.toString());
	static{
		logger.setLevel(Level.ALL);
	}
	
	public static void main(String[] args)
	{
		ByteBuffer bf= ByteBuffer.wrap(new byte[] {0x00,0x01,0x02});
		logger.info("position is: "+bf.position());
		bf.rewind();
		logger.info("position is: "+bf.position());
		logger.info("number of bytes are:"+bf.array().length);
		logger.info("capacity is: "+bf.capacity());
		bf.put(2,(byte)0x03);
		logger.info("limit is: "+bf.limit());
		
	}

}
