package utilities;


/**
 * @author vvasuki This class contains utility methods which deal with byte
 *         arguments.
 * 
 */
public class ByteManipulators {

    /**
     * @param b
     * @return the hexadecimal equivalent of the byte.
     */
    public static String byteToHex(byte b) {
        return ByteArrayManipulators.bytesToHex(new byte[] { b });
    }

    /**
     * @param lowerByte
     * @param higherByte
     * @return the equivalent short.
     */
    public static short bytesToShort(byte lowerByte, byte higherByte) {

        short s = (short) (0x00ff & higherByte);
        s <<= 8;
        // RDPLogger.logInfo("s is "+s);
        s |= (short) (0x00ff & lowerByte);
        // RDPLogger.logInfo("s is "+s);
        return s;
    }

    /**
     * @param lowerByte
     * @param higherByte
     * @return the equivalent Integer
     */
    public static int bytesToInt(byte lowerByte, byte higherByte) {
        return ((0x00ff & (int) higherByte) << 8) | (0x00ff & (int) lowerByte);
    }

    /**
     * @param lowerBytelow
     * @param lowerBytehigh
     * @param higherBytelow
     * @param higherBytehigh
     * @return the equivalent Integer
     */
    public static int bytesToInt(byte lowerBytelow, byte lowerBytehigh,
            byte higherBytelow, byte higherBytehigh) {
        int i = 0x00ff & (int) higherBytehigh;
        i = (i << 8) | (0x00ff & (int) higherBytelow);
        i = (i << 8) | (0x00ff & (int) lowerBytelow);
        i = (i << 8) | (0x00ff & (int) lowerBytehigh);
        return i;
    }

    /**
     * @param b
     * @return the short value equivalent to the unsigned byte.
     */
    public static short unsignedByteToShort(byte b) {
        short s;
        s = 0x00ff;
        s &= b;
        return s;
    }

}
