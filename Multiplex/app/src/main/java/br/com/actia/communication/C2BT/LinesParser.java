package br.com.actia.communication.C2BT;

import java.util.ArrayList;

import android.util.Log;

public class LinesParser
{
	private static final String TAG = "LinesParser";
	
	private byte[] remainder;

	private boolean waitingForNewPhrase;
	
//	public ArrayList<String> parse(String text)
//	{
//	}
	
	public LinesParser()
	{
		waitingForNewPhrase = true;
	}
	
	public String parseBuffer(int bytesLength, byte[] paramBuffer)
	{
		StringBuffer retVal = null;
		
		int remainderLength = remainder == null?0:remainder.length;
		int paramLength = paramBuffer == null?0:paramBuffer.length;
		int bufferLength = remainderLength + paramLength;
		byte[] buffer = new byte[bufferLength];
		if(remainder!=null)
		{
			System.arraycopy(remainder, 0, buffer, 0, remainderLength);
		}
		System.arraycopy(paramBuffer, 0, buffer, remainderLength, paramLength);
		
		Log.d(TAG, "Parsing buffer=" + MiscUtils.getHexString(buffer));

		for (int i = 0; i < bufferLength; i++)
		{
			if (isValidPhraseStarting(buffer, i))
			{
				// have new phrase
				waitingForNewPhrase = false;
				
				if(i+6 < bufferLength)
				{
					int phraseLength = buffer[i+6];
					if (i + 6 + phraseLength <= bufferLength)
					{
						retVal = new StringBuffer();
						retVal.append(MiscUtils.getHexString(buffer, i, i + 6 + phraseLength));
						i = i + 6 + phraseLength;
						remainder = null;
						waitingForNewPhrase = true;
						// at the moment return only one line
						// and do not process remainder
						break;
					} 
					else
					{
						// byte buffer stops in the middle of the data record!
						remainder = new byte[bufferLength-i];
						System.arraycopy(buffer, i, remainder, 0, bufferLength-i);
						waitingForNewPhrase = true;
						break;
					}
				}
				else
				{
					// byte buffer stops before length byte
					remainder = new byte[bufferLength-i];
					System.arraycopy(buffer, i, remainder, 0, bufferLength-i);
					waitingForNewPhrase = true;
					break;
				}
			}
			else
			{
				
			}
		}
		
		if(retVal==null)
		{
			return null;
		}
		
		return retVal.toString();
	}

	private boolean isValidPhraseStarting(byte[] buffer, int i)
	{
		if (buffer[i] == (byte) 0xAA && waitingForNewPhrase)
		{
			return true;
		}

		return false;
	}

}
