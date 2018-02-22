package br.com.actia.controller;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.actia.Globals;
import br.com.actia.communication.CanMSG;
import br.com.actia.dualzoneinterface.R;
import br.com.actia.event.DVDStatusEvent;
import br.com.actia.event.MediaPlayerStatusEvent;
import br.com.actia.model.EquipmentStatusFrame;
import br.com.actia.model.MediaPlayerStatusFrame;
import br.com.actia.model.DVD_S_MODEL.DVDSource;
import br.com.actia.model.DVD_TS_MODEL.DVDStatus;

/**
 * Created by Armani(anderson.armani@actia.com.br) on 08/10/2015.
 */
public class SdController extends DefaultController {
    private ImageButton btnBack;
    private ImageButton btnNext;
    private ImageButton btnPlayPause;
    private TextView txtUsbFile;
    private TextView txtUsbTime;

    private boolean isDriver;
    private boolean sdON = false;

    private DVDStatus sdStatus = null;
    Globals globals = null;

    public SdController(Context context, FrameLayout frameLayout, ImageButton button, @LayoutRes int resource, boolean isDriver) {
        super(context, frameLayout, button, resource);
        this.isDriver = isDriver;
        this.sdStatus = new DVDStatus(DVDStatus.DVD_STATUS_STOPPED);

        initializeView();
        initializeActions();

        globals = Globals.getInstance(context);
        globals.getEventBus().register(this);
    }

    @Override
    public void initializeView() {
        btnBack = (ImageButton) view.findViewById(R.id.btnUSBReward);
        btnNext = (ImageButton) view.findViewById(R.id.btnUSBForward);
        btnPlayPause = (ImageButton) view.findViewById(R.id.btnUSBPlayPause);
        txtUsbFile = (TextView) view.findViewById(R.id.txtUSBFile);
        txtUsbTime = (TextView) view.findViewById(R.id.txtUSBTime);
    }

    @Override
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
            switch (sdStatus.getValue()) {
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

    @Override
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

    public void onEventMainThread(final MediaPlayerStatusEvent event) {
        MediaPlayerStatusFrame mediaPlayerStatusFrame = event.getMediaPlayerStatusFrame();

        if(sdON) {
            int dvdNumber = mediaPlayerStatusFrame.getDvdFileNumber().getValue();
            txtUsbFile.setText(String.format("%02d", dvdNumber));
            txtUsbTime.setText(mediaPlayerStatusFrame.getDvdTime().toString());
            txtUsbTime.setText("%02d:%02d:%02d" +mediaPlayerStatusFrame.getDvdTime().getHours()
                                                + mediaPlayerStatusFrame.getDvdTime().getMinutes()
                                                + mediaPlayerStatusFrame.getDvdTime().getSeconds());

            changePlayPauseIcon(mediaPlayerStatusFrame.getDvdStatus().isPlaying());
        }
        else {
            txtUsbFile.setText("00");
            txtUsbTime.setText("00:00:00");

        }
    }

    public void onEventMainThread(final DVDStatusEvent event) {
        EquipmentStatusFrame equipmentStatusFrame = event.getEquipmentStatusFrame();

        if(isDriver) {
            sdON = equipmentStatusFrame.getDvdSourceDriver().getValue() == DVDSource.DVD_SOURCE_SDCARD;
        }
        else {
            sdON = equipmentStatusFrame.getDvdSourcePassenger().getValue() == DVDSource.DVD_SOURCE_SDCARD;
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
}
