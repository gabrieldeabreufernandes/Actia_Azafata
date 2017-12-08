package br.com.actia.communication;

/**
 * Created by Armani on 26/11/2015.
 */
public class BuildCanMSG {
    private CanMSG canMsg;

    public BuildCanMSG() {
        canMsg = new CanMSG();
    }

    public BuildCanMSG setType(byte type) {
        if(type < CanMSG.MSGTYPE_STANDARD || type > CanMSG.MSGTYPE_EXTENDED)
            canMsg.setType(CanMSG.MSGTYPE_STANDARD);
        else
            canMsg.setType(type);
        return this;
    }

    public BuildCanMSG setId(String strId) {
        canMsg.setId((int) hexTextToInt(strId));
        return this;
    }

    public BuildCanMSG setLength(int length) {
        canMsg.setLength((byte) length);
        return this;
    }

    public BuildCanMSG setData(String data) {
        byte[] bt = hexStringToByteArray(data);

        canMsg.setData(bt, canMsg.getLength());
        return this;
    }

    public CanMSG build() {
        return canMsg;
    }

    // Converts a hex string to byte array
    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    // Converts a string to hex
    private long hexTextToInt(String toConvert)
    {
        long iToReturn = 0;
        int iExp = 0;
        char chByte;

        // The string to convert is empty
        if (toConvert.equals(""))
        {
            return 0;
        }
        // The string have more than 8 character (the equivalent value
        // exeeds the DWORD capacyty
        if (toConvert.length() > 8)
        {
            return 0;
        }
        // We convert any character to its Upper case
        toConvert = toConvert.toUpperCase();
        try
        {
            // We calculate the number using the Hex To Decimal formula
            for (int i = toConvert.length() - 1; i >= 0; i--)
            {
                chByte = (char) toConvert.getBytes()[i];
                switch ((int) chByte)
                {
                    case 65:
                        iToReturn += (long) (10 * Math.pow(16.0f, iExp));
                        break;
                    case 66:
                        iToReturn += (long) (11 * Math.pow(16.0f, iExp));
                        break;
                    case 67:
                        iToReturn += (long) (12 * Math.pow(16.0f, iExp));
                        break;
                    case 68:
                        iToReturn += (long) (13 * Math.pow(16.0f, iExp));
                        break;
                    case 69:
                        iToReturn += (long) (14 * Math.pow(16.0f, iExp));
                        break;
                    case 70:
                        iToReturn += (long) (15 * Math.pow(16.0f, iExp));
                        break;
                    default:
                        if ((chByte < 48) || (chByte > 57))
                        {
                            return -1;
                        }
                        iToReturn += (long) Integer.parseInt(((Character) chByte).toString()) * Math.pow(16.0f, iExp);
                        break;
                }
                iExp++;
            }
        }
        catch (Exception ex)
        {
            // Error, return 0
            return 0;
        }
        return iToReturn;
    }
}
