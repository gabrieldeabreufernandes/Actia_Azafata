package br.com.actia.dualzoneinterface;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.actia.controller.PlaylistHandler;

/**
 * Created by Armani andersonaramni@gmail.com on 06/04/17.
 */

/*
    CHANGED BY GABRIEL:
    this view does not have to be displayed.
 */

public class UsbAutoCopyActivity extends Activity {
    private TextView    mTvTitle;
    private TextView    mTvStatus;
    private ProgressBar mPbProgress;

    private String      mstrUsbPath = "";
    private UsbAutoCopyAsyncTask usbCopyTask;

    private List<String> listUsb;
    private List<String> listTablet;
    private PlaylistHandler mplaylistHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       //setContentView(R.layout.activity_autoload_usb);
        //mTvTitle = (TextView) findViewById(R.id.usb_title);
        //mTvStatus = (TextView) findViewById(R.id.usb_title);
        //mPbProgress = (ProgressBar) findViewById(R.id.usb_progressBar);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Toast.makeText(getApplicationContext(), "EXTRAS NULL", Toast.LENGTH_LONG).show();
            } else {
                mstrUsbPath = extras.getString(UsbBroadcastReceiver.USB_DATA);
                Toast.makeText(getApplicationContext(), mstrUsbPath, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "SAVE INSTANCE NULL", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!mstrUsbPath.isEmpty()) {
            mplaylistHandler = new PlaylistHandler(mstrUsbPath);
            //LIST TABLET FILES
            listTablet = mplaylistHandler.getPlaylists(PlaylistHandler.PLAYLIST_USB, PlaylistHandler.FOLDER_FILTER);
            //LIST PENDRIVE FILES
            listUsb = mplaylistHandler.getPlaylists(PlaylistHandler.PLAYLIST_EXTERNAL_USB, PlaylistHandler.FOLDER_FILTER);

            if(listUsb != null && listUsb.size() >= 1) {
                usbCopyTask = new UsbAutoCopyAsyncTask();
                usbCopyTask.execute();
            }
            else {
                Toast.makeText(getApplicationContext(), "DISPOSITIVO USB NAO ENCONTRADO", Toast.LENGTH_LONG).show();
                //mTvTitle.setText(getString(R.string.usb_copy_error));
            }

            finish();
        }
    }

    private class UsbAutoCopyAsyncTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object obj) {
            super.onPostExecute(obj);

            if ((boolean) obj){
                //mTvTitle.setText(getString(R.string.usb_copy_ok));
                Toast.makeText(getApplicationContext(), "ARQUIVOS COPIADOS COM SUCESSO!", Toast.LENGTH_LONG).show();
            }else {
                //mTvTitle.setText(getString(R.string.usb_copy_error));
                Toast.makeText(getApplicationContext(), "DISPOSITIVO USB NAO ENCONTRADO", Toast.LENGTH_LONG).show();
            }
        }

        /*
        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);

            //mPbProgress.setProgress((int)values[0]);
        }*/

        @Override
        protected Object doInBackground(Object[] objects) {
            boolean ret = false;
            int numFiles = listUsb.size() + listTablet.size();
            int cntFiles = 0;


            //Deleting files that aren't in USB List
            for(String fileName: listTablet) {
                ret = mplaylistHandler.deletePlaylist(fileName);
                publishProgress((int) ((++numFiles / (float) cntFiles) * 100));
            }

            //Copying files
            for(String fileName: listUsb) {
                ret = mplaylistHandler.copyPlaylistFromUSB(fileName);
                if(!ret)
                    break;

                publishProgress((int) ((++numFiles / (float) cntFiles) * 100));
            }

            return ret;
        }
    }

}
