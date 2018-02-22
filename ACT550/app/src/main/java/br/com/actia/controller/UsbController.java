package br.com.actia.controller;

import android.content.Context;
import android.support.annotation.LayoutRes;
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
public class UsbController extends DefaultController {
    private ImageButton btnBack;
    private ImageButton btnNext;
    private ImageButton btnPlayPause;
    private TextView txtDvdFile;
    private TextView txtDvdTime;

    private boolean isDriver;
    private boolean isUsbOn = false;

    private DVDStatus usbStatus = null;
    Globals globals = null;

    public UsbController(Context context, FrameLayout frameLayout, ImageButton button, @LayoutRes int resource, boolean isDriver) {
        super(context, frameLayout, button, resource);
        this.isDriver = isDriver;
        this.usbStatus = new DVDStatus(DVDStatus.DVD_STATUS_STOPPED);

        initializeView();
        initializeActions();

        globals = Globals.getInstance(context);
        globals.getEventBus().register(this);
    }

    @Override
    public void initializeView() {
        btnBack = (ImageButton) view.findViewById(R.id.btnReward);
        btnNext = (ImageButton) view.findViewById(R.id.btnForward);
        btnPlayPause = (ImageButton) view.findViewById(R.id.btnUSBPlayPause);
        txtDvdFile = (TextView) view.findViewById(R.id.txtUSBFile);
        txtDvdTime = (TextView) view.findViewById(R.id.txtUSBTime);
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

    @Override
    public void sendChangeMode() {
        CanMSG canMSG = new CanMSG();
        canMSG.setId(CanMSG.MSGID_EQUIPAMENT_CTRL);
        canMSG.setLength((byte) 8);
        canMSG.setType(CanMSG.MSGTYPE_EXTENDED);

        if(isDriver)
            canMSG.setData("4000000000000");//("002F00ffffffffff")
        else
            canMSG.setData("5000000000000");//‭00F200ffffffffff

        globals.sendCanCMDEvent(canMSG);
    }

    public void onEventMainThread(final MediaPlayerStatusEvent event) {
        MediaPlayerStatusFrame mediaPlayerStatusFrame = event.getMediaPlayerStatusFrame();

        if(isUsbOn){
            int dvdNumber = mediaPlayerStatusFrame.getDvdFileNumber().getValue();
            txtDvdFile.setText(String.format("%02d", dvdNumber));
            txtDvdTime.setText(mediaPlayerStatusFrame.getDvdTime().toString());

            changePlayPauseIcon(mediaPlayerStatusFrame.getDvdStatus().isPlaying());
        }
        else {
            txtDvdFile.setText("00");
            txtDvdTime.setText("00:00:00");
        }
    }

    public void onEventMainThread(final DVDStatusEvent event) {
        EquipmentStatusFrame equipmentStatusFrame = event.getEquipmentStatusFrame();

        if(isDriver) {
            isUsbOn = equipmentStatusFrame.getDvdSourceDriver().getValue() == DVDSource.DVD_SOURCE_SDCARD;
        }
        else {
            isUsbOn = equipmentStatusFrame.getDvdSourcePassenger().getValue() == DVDSource.DVD_SOURCE_SDCARD;
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
