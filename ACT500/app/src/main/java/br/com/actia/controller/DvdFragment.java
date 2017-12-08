package br.com.actia.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import br.com.actia.model.CONFIG.AppMenuBar;
import br.com.actia.model.CONFIG.AppWorkArea;
import br.com.actia.model.DVDStatusFrame;
import br.com.actia.model.DVDTrackStatusFrame;
import br.com.actia.model.DVD_S_MODEL.DVDSource;
import br.com.actia.model.DVD_TS_MODEL.DVDStatus;

/**
 * Created by Armani andersonaramni@gmail.com on 20/02/17.
 */

public class DvdFragment extends Fragment {
    View view;
    private ImageButton btnBack;
    private ImageButton btnNext;
    private ImageButton btnPlayPause;
    private ImageButton btnEject;
    private TextView txtDvdFile;
    private TextView txtDvdTime;

    private boolean isDriver;
    private boolean diskOn = false;

    private DVDStatus dvdStatus = null;
    Globals globals = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_dvd, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dvdStatus = new DVDStatus(DVDStatus.DVD_STATUS_STOPPED);

        btnBack = (ImageButton) view.findViewById(R.id.btnReward);
        btnNext = (ImageButton) view.findViewById(R.id.btnForward);
        btnPlayPause = (ImageButton) view.findViewById(R.id.btnPlayPause);
        btnEject = (ImageButton) view.findViewById(R.id.btnDiskEject);
        txtDvdFile = (TextView) view.findViewById(R.id.txtDvdFile);
        txtDvdTime = (TextView) view.findViewById(R.id.txtDvdTime);

        initializeActions();

        globals = Globals.getInstance(getActivity().getApplication().getApplicationContext());
        globals.getEventBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        globals.getEventBus().unregister(this);
    }

    public void initializeActions() {
        btnBack.setOnClickListener(new View.OnClickListener() {
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
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (dvdStatus.getValue()) {
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

        btnEject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_EJECT);
            }
        });
    }

    public void sendChangeMode() {
        CanMSG canMSG = new CanMSG();
        canMSG.setId(CanMSG.MSGID_EQUIPAMENT_CTRL);
        canMSG.setLength((byte) 8);
        canMSG.setType(CanMSG.MSGTYPE_EXTENDED);

        if(isDriver)
            canMSG.setData("001F00ffffffffff");
        else
            canMSG.setData("00F100ffffffffff");

        globals.sendCanCMDEvent(canMSG);
    }

    public void configView(AppWorkArea appWorkArea, AppMenuBar appMenuBar) {

    }

    public void onEventMainThread(final DVDTrackStatusEvent event) {
        DVDTrackStatusFrame dvdTrackStatusFrame = event.getDvdTrackStatusFrame();

        if(diskOn){
            int dvdNumber = dvdTrackStatusFrame.getDvdFileNumber().getValue();
            txtDvdFile.setText(String.format("%02d", dvdNumber));
            txtDvdTime.setText(dvdTrackStatusFrame.getDvdTime().toString());

            changePlayPauseIcon(dvdTrackStatusFrame.getDvdStatus().isPlaying());
        }
        else {
            txtDvdFile.setText("00");
            txtDvdTime.setText("00:00:00");
        }
    }

    public void onEventMainThread(final DVDStatusEvent event) {
        DVDStatusFrame dvdStatusFrame = event.getDvdStatusFrame();

        if(isDriver) {
            diskOn = dvdStatusFrame.getDvdSourceDriver().getValue() == DVDSource.DVD_SOURCE_CD;
        }
        else {
            diskOn = dvdStatusFrame.getDvdSourcePassenger().getValue() == DVDSource.DVD_SOURCE_CD;
        }
    }

    private void changePlayPauseIcon(boolean isPlaying) {
        if(isPlaying) {
            btnPlayPause.setImageResource(R.drawable.ic_pause_white_36dp);
        }
        else {
            btnPlayPause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
        }
    }

    public void setIsDriver(boolean val) {
        isDriver = val;
    }
}
