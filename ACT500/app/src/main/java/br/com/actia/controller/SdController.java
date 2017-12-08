package br.com.actia.controller;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.actia.Globals;
import br.com.actia.communication.CanMSG;
import br.com.actia.dualzoneinterface.Interface;
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
 * Created by Armani(anderson.armani@actia.com.br) on 08/10/2015.
 */
public class SdController extends DefaultController {
    private ImageButton btnBack;
    private ImageButton btnNext;
    private ImageButton btnPlayPause;
    private TextView txtUsbFile;
    private TextView txtUsbTime;

    private boolean isDriver;
    private boolean usbON = false;

    private DVDStatus usbStatus = null;
    Globals globals = null;

    public SdController(Interface mainActivity, FrameLayout frameLayout, ImageButton button, @LayoutRes int resource, boolean isDriver) {
        super(mainActivity, frameLayout, button, resource);
        this.isDriver = isDriver;
        this.usbStatus = new DVDStatus(DVDStatus.DVD_STATUS_STOPPED);

        initializeView();
        initializeActions();

        globals = Globals.getInstance(mainActivity.getApplicationContext());
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

    @Override
    public void configView(AppWorkArea appWorkArea, AppMenuBar appMenuBar) {
        Interface mainActivity = (Interface) this.mainActivity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btnBack.setBackground(mainActivity.makeSelector(Color.parseColor(appMenuBar.getBtnPressed()),
                    Color.parseColor(appMenuBar.getBtnSelected()), Color.parseColor(appMenuBar.getBtnNormal())));

            btnNext.setBackground(mainActivity.makeSelector(Color.parseColor(appMenuBar.getBtnPressed()),
                    Color.parseColor(appMenuBar.getBtnSelected()), Color.parseColor(appMenuBar.getBtnNormal())));

            btnPlayPause.setBackground(mainActivity.makeSelector(Color.parseColor(appMenuBar.getBtnPressed()),
                    Color.parseColor(appMenuBar.getBtnSelected()), Color.parseColor(appMenuBar.getBtnNormal())));
        }

    }
}
