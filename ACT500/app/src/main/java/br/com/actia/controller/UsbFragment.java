package br.com.actia.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.actia.Globals;
import br.com.actia.communication.CanMSG;
import br.com.actia.dualzoneinterface.R;
import br.com.actia.event.DVDStatusEvent;
import br.com.actia.event.DVDTrackStatusEvent;
import br.com.actia.event.PlaylistChangedEvent;
import br.com.actia.model.DVDStatusFrame;
import br.com.actia.model.DVDTrackStatusFrame;
import br.com.actia.model.DVD_S_MODEL.DVDSource;
import br.com.actia.model.DVD_TS_MODEL.DVDStatus;
import br.com.actia.model.UsbMediaFile;

/**
 * Created by Armani andersonaramni@gmail.com on 20/02/17.
 */

public class UsbFragment extends Fragment {
    private static final String TAG = "USB FRAGMENT";
    private View view;
    private ImageButton btnBack;
    private ImageButton btnNext;
    private ImageButton btnPlayPause;
    private TextView txtUsbFile;
    private TextView txtUsbTime;

    private boolean isDriver;
    private boolean usbON = false;

    private DVDStatus usbStatus = null;

    private UsbMediaFile usbMediaFile;

    private Globals globals = null;

    public UsbFragment() {
        usbMediaFile = new UsbMediaFile();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        view = inflater.inflate(R.layout.activity_sd, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");

        btnBack = (ImageButton) view.findViewById(R.id.btnUSBReward);
        btnNext = (ImageButton) view.findViewById(R.id.btnUSBForward);
        btnPlayPause = (ImageButton) view.findViewById(R.id.btnUSBPlayPause);
        txtUsbFile = (TextView) view.findViewById(R.id.txtUSBFile);
        txtUsbTime = (TextView) view.findViewById(R.id.txtUSBTime);

        usbStatus = new DVDStatus(DVDStatus.DVD_STATUS_STOPPED);

        initializeActions();

        globals = Globals.getInstance(getActivity().getApplication().getApplicationContext());
        globals.getEventBus().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!usbMediaFile.getPlaylistPath().isEmpty()) {
            playlist_fragment p_fragment = (playlist_fragment) getFragmentManager().findFragmentById(R.layout.playlist_view);
            if (p_fragment != null) {
                p_fragment.setCurrentPlaylist(usbMediaFile);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        globals.getEventBus().unregister(this);
    }

    private void initializeActions() {
        /*btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_BACK);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_NEXT);
            }
        });*/

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (usbStatus.getValue()) {
                    case DVDStatus.DVD_STATUS_PLAYING:
                        globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_PAUSE);
                        break;
                    case DVDStatus.DVD_STATUS_PAUSED:
                    case DVDStatus.DVD_STATUS_STOPPED:
                        globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_PLAY);
                        break;
                }
            }
        });
    }

    /**
     * Notify when user choose a source
     */
    public void sendChangeMode() {
        CanMSG canMSG = new CanMSG();
        canMSG.setId(CanMSG.MSGID_EQUIPAMENT_CTRL);
        canMSG.setLength((byte) 8);
        canMSG.setType(CanMSG.MSGTYPE_EXTENDED);

        if(isDriver)
            canMSG.setData("002F00ffffffffff");
        else
            canMSG.setData("00F200ffffffffff");

        globals.sendCanCMDEvent(canMSG);
    }

    public void onEventMainThread(final DVDTrackStatusEvent event) {
        DVDTrackStatusFrame dvdTrackStatusFrame = event.getDvdTrackStatusFrame();

        if(usbON) {
            int dvdNumber = dvdTrackStatusFrame.getDvdFileNumber().getValue();
            txtUsbFile.setText(String.format("%02d", dvdNumber));
            txtUsbTime.setText(dvdTrackStatusFrame.getDvdTime().toString());
        }
        else {
            txtUsbFile.setText("00");
            txtUsbTime.setText("00:00:00");
        }
    }

    public void onEventMainThread(final DVDStatusEvent event) {
        DVDStatusFrame dvdStatusFrame = event.getDvdStatusFrame();

        if(isDriver) {
            usbON = dvdStatusFrame.getDvdSourceDriver().getValue() == DVDSource.DVD_SOURCE_USB;
        }
        else {
            usbON = dvdStatusFrame.getDvdSourcePassenger().getValue() == DVDSource.DVD_SOURCE_USB;
        }
    }

    /*public void configView(AppWorkArea appWorkArea, AppMenuBar appMenuBar) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btnBack.setBackground(mainActivity.makeSelector(Color.parseColor(appMenuBar.getBtnPressed()),
                    Color.parseColor(appMenuBar.getBtnSelected()), Color.parseColor(appMenuBar.getBtnNormal())));

            btnNext.setBackground(mainActivity.makeSelector(Color.parseColor(appMenuBar.getBtnPressed()),
                    Color.parseColor(appMenuBar.getBtnSelected()), Color.parseColor(appMenuBar.getBtnNormal())));

            btnPlayPause.setBackground(mainActivity.makeSelector(Color.parseColor(appMenuBar.getBtnPressed()),
                    Color.parseColor(appMenuBar.getBtnSelected()), Color.parseColor(appMenuBar.getBtnNormal())));
        }*/


    public void setIsDriver(boolean val) {
        isDriver = val;
    }

    /**
     * EventBus callback
     * @param playlistChangedEvent
     */
    public void onEventMainThread(final PlaylistChangedEvent playlistChangedEvent) {
        UsbMediaFile usbMediaFile = playlistChangedEvent.getUsbMediaFile();

        if(! usbMediaFile.getPlaylistPath().equalsIgnoreCase(this.usbMediaFile.getPlaylistPath())) {
            this.usbMediaFile = usbMediaFile;
        }
    }
}
