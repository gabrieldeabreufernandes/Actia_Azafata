package br.com.actia.communication.C2BT;

public class SentenceBuilder
{

	/**
	 * ASCII
	 * \x55\x87\xDC
	 * 
	 * BIN
	 * \x55\x86\xDB
	 */
	public static byte[] getASCIIBinSentence(boolean isBin) 
	{
		byte[] retVal = new byte[3];
		if(isBin)
		{
			retVal[0] = 0x55;
			retVal[1] = (byte)0x86;
			retVal[2] = (byte)0xDB;
		}
		else
		{
			retVal[0] = 0x55;
			retVal[1] = (byte)0x87;
			retVal[2] = (byte)0xDC;
		}
		
		return retVal;
	}
	
	/**
	 * { 20kBit \x55\x81\x01\xD7 } 
	 * { 50kBit \x55\x81\x02\xD8 } 
	 * { 100kBit \x55\x81\x03\xD9 } 
	 * { 125kBit \x55\x81\x04\xDA } 
	 * { 250kBit \x55\x81\x05\xDB } 
	 * { 500kBit \x55\x81\x06\xDC } 
	 * { 1MBit \x55\x81\x07\xDD }
	 */
	public static byte[] getBaudrateSentence(String baudrate) 
	{
		byte[] retVal = null;
		
		if ("20 kBit".equals(baudrate)) {
			retVal = new byte[4];
			retVal[0] = 0x55;
			retVal[1] = (byte) 0x81;
			retVal[2] = 0x01;
			retVal[3] = (byte) 0xD7;
		} else if ("50 kBit".equals(baudrate)) {
			retVal = new byte[4];
			retVal[0] = 0x55;
			retVal[1] = (byte) 0x81;
			retVal[2] = 0x02;
			retVal[3] = (byte) 0xD8;
		} else if ("100 kBit".equals(baudrate)) {
			retVal = new byte[4];
			retVal[0] = 0x55;
			retVal[1] = (byte) 0x81;
			retVal[2] = 0x03;
			retVal[3] = (byte) 0xD9;
		} else if ("125 kBit".equals(baudrate)) {
			retVal = new byte[4];
			retVal[0] = 0x55;
			retVal[1] = (byte) 0x81;
			retVal[2] = 0x04;
			retVal[3] = (byte) 0xDA;
		} else if ("250 kBit".equals(baudrate)) {
			retVal = new byte[4];
			retVal[0] = 0x55;
			retVal[1] = (byte) 0x81;
			retVal[2] = 0x05;
			retVal[3] = (byte) 0xDB;
		} else if ("500 kBit".equals(baudrate)) {
			retVal = new byte[4];
			retVal[0] = 0x55;
			retVal[1] = (byte) 0x81;
			retVal[2] = 0x06;
			retVal[3] = (byte) 0xDC;
		} else if ("1 MBit".equals(baudrate)) {
			retVal = new byte[4];
			retVal[0] = 0x55;
			retVal[1] = (byte) 0x81;
			retVal[2] = 0x07;
			retVal[3] = (byte) 0xDD;
		}

		return retVal;
	}

	/**
	 * TIMESTAMP OFF 
	 * \x55\x84\xD9  
	 * 
	 * TIMESTAMP SET 1234
	 * \x55\x83\x12\x34\x1E
	 */
	public static byte[] getTimestampSentence(String timestamp) 
	{
		byte[] retVal = null;
		if ("Off".equals(timestamp)) 
		{
			byte[] val = { 0x55, (byte) 0x84, (byte) 0xD9 };
			retVal = val;
		} 
		else if ("Set 1234".equals(timestamp)) 
		{
			byte[] val = { 0x55, (byte) 0x83, 0x12, 0x34, 0x1E };
			retVal = val;
		}

		return retVal;
	}

	public static byte[] buildKeepAlivePhrase(boolean toggle)
	{
		byte[] Strg_CAN_KeepAlive0 = { 0x55, 0x00, 0x00, 0x00, 0x01, 0x33, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0x8D};
		byte[] Strg_CAN_KeepAlive8 = { 0x55, 0x00, 0x00, 0x00, 0x01, 0x33, 0x04, (byte) 0x80, 0x00, 0x00, 0x00, 0x0D };
		
		if(toggle)
		{
			return Strg_CAN_KeepAlive0;
		}
		else
		{
			return Strg_CAN_KeepAlive8;
		}
	}

	public static byte[] getStdFrameSequence()
	{
		// \x55\x10\x00\x00\x04\x33\x08\x21\x32\x43\x11\x9E\xF4\x87\x77\xDB
		byte[] val = { 0x55, 
				(byte) 0x10, 
				(byte) 0x00, 
				(byte) 0x00, 
				(byte) 0x04, 
				(byte) 0x33, 
				(byte) 0x08, 
				(byte) 0x21, 
				(byte) 0x32, 
				(byte) 0x43, 
				(byte) 0x11, 
				(byte) 0x9E, 
				(byte) 0xF4, 
				(byte) 0x87, 
				(byte) 0x77, 
				(byte) 0xDB };
		return val;
	}

	public static byte[] getResetSequence()
	{
		// \x55\x8F\xE4
		byte[] val = { 0x55, (byte) 0x8F, (byte)0xE4 };
		return val;
	}

	public static byte[] getNoFiltersSequence()
	{
		/*Filters: All Off
		55 82 EB FF FF FF EB FF FF FF 00 00 00 00 00 00 00 00 A7*/
		byte[] val = { 0x55,
				(byte) 0x82,
				(byte) 0xEB,
				(byte) 0xFF,
				(byte) 0xFF,
				(byte) 0xFF,
				(byte) 0xEB,
				(byte) 0xFF,
				(byte) 0xFF,
				(byte) 0xFF,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0xA7 };
		return val;
	}

	public static byte[] getFilterOnlyExtended()
	{

		/*Filters: Filter only Ext
		55 82 88 B9 10 01 E8 00 00 00 80 B9 10 01 E3 FF FF FF 3B*/
		byte[] val = { 0x55,
				(byte) 0x82,
				(byte) 0x88,
				(byte) 0xB9,
				(byte) 0x10,
				(byte) 0x01,
				(byte) 0xE8,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x80,
				(byte) 0xB9,
				(byte) 0x10,
				(byte) 0x01,
				(byte) 0xE3,
				(byte) 0xFF,
				(byte) 0xFF,
				(byte) 0xFF,
				(byte) 0xB3 };
		return val;
	}

}
