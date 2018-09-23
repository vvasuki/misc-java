package utilities;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ByteArrayManipulators {
	private final static Logger logger = Logger
			.getLogger(ByteArrayManipulators.class.toString());

	static {
		logger.setLevel(Level.OFF);
	}

	public static String bytesToBinary(byte[] bytes) {
		// Create a BigInteger using the byte array
		BigInteger bi = new BigInteger(bytes);
		// Format to binary
		String s = bi.toString(2); // 100100000111111110000
		return s;

	}

	public static String bytesToDecimal(byte[] bytes) {
		// Create a BigInteger using the byte array
		BigInteger bi = new BigInteger(bytes);
		// Format to decimal
		String s = bi.toString(); // 1183728
		return s;

	}

	public static String bytesToHex(byte[] bytes) {
		// logger.info("first byte is:"+bytes[0]);
		if (bytes[0] < 0) {
			byte[] bytesOld = bytes;
			bytes = new byte[bytes.length + 1];
			bytes[0] = 0;
			for (int i = 0; i < bytesOld.length; i++)
				bytes[i + 1] = bytesOld[i];
		}
		// logger.info("first byte is:"+bytes[0]);
		if (bytes.length == 0)
			return "";
		// Create a BigInteger using the byte array
		BigInteger bi = new BigInteger(bytes);
		// Format to hexadecimal
		String s = bi.toString(16); // 120ff0
		if (s.length() % 2 != 0) {
			// Pad with 0
			s = "0" + s;
		}
		return s;

	}

	public static long bytes2long(byte[] bytes) {
		long l = 0;
		BigInteger bi = new BigInteger(bytes);
		l = bi.longValue();
		logger.info("l is: " + l);
		return l;

	}

	public static short bytesToShort(byte lowerByte, byte higherByte)
	{
		byte[] bytes = {0, higherByte, lowerByte};
		/*bytes[0] = 0;
		  bytes[1] = higherByte;
		  bytes[2] = lowerByte;
		  */
		BigInteger bi = new BigInteger(bytes);
		return bi.shortValue();
	}
	public static int bytesToInt(byte lowerByte, byte higherByte)
	{
		byte[] bytes = {0, 0, higherByte, lowerByte};
		/*bytes[0] = 0;
		  bytes[1] = higherByte;
		  bytes[2] = lowerByte;
		  */
		BigInteger bi = new BigInteger(bytes);
		return bi.intValue();
	}

	public static int bytesToInt(byte lowerBytelow, byte lowerBytehigh, byte higherBytelow, byte higherBytehigh)
	{
		byte[] bytes = {higherBytehigh, higherBytelow, lowerBytehigh, lowerBytelow};
		BigInteger bi = new BigInteger(bytes);
		return bi.intValue();
	}

}
