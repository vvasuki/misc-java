package package1;

import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.ByteArrayManipulators;
import utilities.ByteManipulators;

public class ArithmaticTest {
    private static Logger logger = Logger.getLogger(ReflectionTest.class.toString());
    static {
        logger.setLevel(Level.ALL);
    }

    static void testLong2bytes() {
        long l = 0x5a5a;
        int numOfBytes = 8;
        byte[] bytes = new byte[numOfBytes];
        for (int i = numOfBytes - 1; i >= 0; i--) {
            bytes[i] = (byte) (l >> 8);
            l = l >> 8;
        }
        logger.info(ByteArrayManipulators.bytesToHex(bytes));
    }

    static byte reverseByte_short(byte b) {
        // testLong2bytes();
        short s = ByteManipulators.unsignedByteToShort(b);
        // logger.info("byte is " + b);
        // logger.info("short is " + s);
        long l1 = 0x0202020202l;
        long l2 = 0x010884422010l;
        long l3 = (long) (s * l1);
        // logger.info("( l3 & l2) % 1023 is " + (l3 & l2) % 1023);
        s = (short) ((l3 & l2) % 1023);
        b = (byte) s;
        // logger.info("byte is " + b);
        // logger.info("short is " + s);
        return b;

    }


    static byte getLastBit(byte b) {
        return (byte) (b & 0x01);
    }
    static byte reverseByte(byte b) {
        byte lastBit;
        byte resultByte = 0x00;
        for (int i = 0; i < 8; i++) {
            resultByte <<= 1;
            lastBit = getLastBit(b);
            b >>>= 1;
            resultByte |= lastBit;
        }
        logger.info("resultByte is " + resultByte);
        return resultByte;

    }

    public static void main(String[] args) {
        logger.info("got:" + reverseByte((byte) 0x82));
        logger.info("expected:" + reverseByte_short((byte) 0x82));

    }
}
