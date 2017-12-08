package br.com.actia.communication.C2BT;

import java.io.IOException;
import java.io.InputStream;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


public class ReaderThread extends Thread {

	private static final String TAG = "ReaderThread";
	private InputStream mmInStream;
//	private SeatModel seat;
//	private ByteBufferParser parser;
//	private BluetoothSocket mmSocket;
	private boolean stopThread = false;


	public ReaderThread(BluetoothSocket socket)
	{
		//		parser = new ByteBufferParser(seat);
		InputStream tmpIn = null;  
		// Get the input and output streams, using temp objects because        
		// member streams are final
		try {            
			tmpIn = socket.getInputStream();
//			tmpOut = socket.getOutputStream();
		} 
		catch (IOException e) { }         
		mmInStream = tmpIn;        
//		mmOutStream = tmpOut;    
	}

	@Override
	public void run() 
	{
		while(true)
		{
			if(stopThread)
			{
				break;
			}

			byte[] buffer = new byte[1024];  
			// Read from the InputStream                
			int bytesLength = 0;
			try 
			{
				bytesLength = mmInStream.read(buffer);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(bytesLength > 0)
			{
				String text = MiscUtils.getHexString(buffer, bytesLength);
				Log.d(TAG, text);
				byte[] dest = new byte[bytesLength];
				System.arraycopy(buffer, 0, dest, 0, bytesLength);
				//detailsActivity.sendReaderEventMessage(dest);
			}
		}
	}
	
	public synchronized void stopThread()
	{
		stopThread = true;
		if(mmInStream!=null)
		{
			try
			{
				mmInStream.close();
				mmInStream = null;
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
