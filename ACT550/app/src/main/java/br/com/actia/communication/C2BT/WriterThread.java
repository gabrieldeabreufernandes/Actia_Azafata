package br.com.actia.communication.C2BT;

import java.io.IOException;
import java.io.OutputStream;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class WriterThread extends Thread
{
	private static final String TAG = "MoverThread";

	private BluetoothSocket mmSocket;
	private OutputStream mmOutStream;

	boolean stopThread = false;

	boolean toggle_0_8 = false;

	private byte[] bufferToSend;
	
	public WriterThread(BluetoothSocket socket)
	{
		if(socket==null)
		{
			throw new IllegalArgumentException("Socket is null!");
		}
		
		mmSocket = socket;
		OutputStream tmpOut = null;         
		// Get the input and output streams, using temp objects because        
		// member streams are final
		try
		{
			tmpOut = socket.getOutputStream();
		}
		catch (IOException e)
		{
		}
		mmOutStream = tmpOut;
	}
	
	
	public void run()
	{
		// buffer store for the stream        
		// bytes returned from read()         
		// Keep listening to the InputStream until an exception occurs
		
		while (true)
		{
			if(stopThread)
			{
				break;
			}

			try 
			{
				byte[] bytesToSend = buildPhraseToSend();
				String s = MiscUtils.getHexString(bytesToSend);
				Log.v(TAG, "Sending :" + s);
				if(bytesToSend!=null && bytesToSend.length > 0)
				{
					toggle_0_8 = !toggle_0_8;
					if(mmOutStream!=null)
					{
						mmOutStream.write(bytesToSend);
					}
				}
			}
			catch (IOException e)
			{
				break;            
			}   
			
			try 
			{
				Thread.sleep(80);
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	private byte[] buildPhraseToSend()
	{
		byte[] buffer;
		
		// at first set the binary mode
//		if(!binaryModeSet)
//		{
//			binaryModeSet = true;
//			return getASCIIBinSentence(true);
//		}

		buffer = getBufferToSend();
		bufferToSend = null;

		
		if( buffer == null || buffer.length == 0 )
		{
			buffer = SentenceBuilder.buildKeepAlivePhrase(toggle_0_8);
		}
		
		return buffer;
	}




	public byte[] getBufferToSend()
	{
		return bufferToSend;
	}

	public void setBufferToSend(byte[] bufferToSend)
	{
		this.bufferToSend = bufferToSend;
	}



	/* Call this from the main Activity to send data to the remote device */    
	public void write(byte[] bytes)
	{
		try {
			mmOutStream.write(bytes);        
		} catch (IOException e) { }    
	}
	
	/* Call this from the main Activity to shutdown the connection */    
	public void cancel() 
	{
		try {            
			mmSocket.close();        
		}
		catch (IOException e) 
		{
			
		}    
	}
	
	public synchronized void stopThread()
	{
		stopThread = true;
		if(mmOutStream!=null)
		{
			try
			{
				mmOutStream.close();
				mmOutStream = null;
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(mmSocket != null)
		{
			try
			{
				mmSocket.close();
				mmSocket = null;
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
