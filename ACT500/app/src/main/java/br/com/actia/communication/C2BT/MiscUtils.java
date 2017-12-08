package br.com.actia.communication.C2BT;

public class MiscUtils {

	public static String getHexString(byte[] b, int start, int length) {
		String result = "";
		for (int i = start; i < length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			result += " ";
		}
		return result.toUpperCase();
	}
	public static String getHexString(byte[] b, int length) {
		return getHexString(b, 0, b==null?0:b.length);
	}
	
	public static String getHexString(byte[] b) {
		return getHexString(b, b==null?0:b.length);
	}

	
	/**
	 * 
	 * @param buf
	 * @param length
	 * @return
	 */
	public static byte checksum(byte[] buf, int length) 
	{
		byte retVal = 0;
		for(int i=0; i < length;i++)
		{
			retVal += buf[i];
		}
		return retVal;
	}
	
	public static void main(String[] args) {
		byte[] Strg_CAN_BackrestForward0 = { 0x55, 0x00, 0x00, 0x00, 0x01, 0x33, 0x04, 0x00, 0x10, 0x00, 0x00, (byte) 0x9D };
		
		Strg_CAN_BackrestForward0[11] = checksum(Strg_CAN_BackrestForward0, 11);
		System.out.println(getHexString(Strg_CAN_BackrestForward0));
		
//		byte[] Strg_CAN_BackrestForward8 = { 0x55, 0x00, 0x00, 0x00, 0x01, 0x33, 0x04, (byte) 0x80, 0x10, 0x00, 0x00, 0x1D };
//
//		// Kippen zurueck
//		byte[] Strg_CAN_BackrestBackward0 = { 0x55, 0x00, 0x00, 0x00, 0x01, 0x33, 0x04, 0x00, 0x20, 0x00, 0x00, (byte) 0xAD };
//		byte[] Strg_CAN_BackrestBackward8 = { 0x55, 0x00, 0x00, 0x00, 0x01, 0x33, 0x04, (byte) 0x80, 0x20, 0x00, 0x00, 0x2D };
	}

}
