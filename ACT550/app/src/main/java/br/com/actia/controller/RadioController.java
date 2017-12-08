package br.com.actia.controller;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import br.com.actia.Globals;
import br.com.actia.communication.CanMSG;
import br.com.actia.dualzoneinterface.R;
import br.com.actia.event.RadioPSEvent;
import br.com.actia.event.RadioStatusEvent;
import br.com.actia.model.RADIOStatusFrame;
import br.com.actia.model.RadioPSFrame;

/**
 * Created by Armani(anderson.armani@actia.com.br) on 08/10/2015.
 */
public class RadioController extends DefaultController {
    private final int SET_FM1 = 0x00;
    private final int SET_FM2 = 0x01;
    private final int SET_FM3 = 0x02;
    private final int SET_AM1 = 0x03;
    private final int SET_AM2 = 0x04;
    private ToggleButton imgbtAm1 = null;
    private ToggleButton imgbtAm2 = null;
    private ToggleButton imgbtFm1 = null;
    private ToggleButton imgbtFm2 = null;
    private ToggleButton imgbtFm3 = null;
    private ImageButton imgbtRewardStation = null;
    private ImageButton imgbtForwardStation = null;
    private TextView txtDial = null;
    private TextView txtStationName = null;

    private boolean isDriver;
    private Globals globals = null;

    public RadioController(Context context, FrameLayout frameLayout, ImageButton button, @LayoutRes int resource, boolean isDriver) {
        super(context, frameLayout, button, resource);
        this.isDriver = isDriver;

        initializeView();
        initializeActions();

        globals = Globals.getInstance(context);
        globals.getEventBus().register(this);
    }

    @Override
    public void initializeView() {
        this.imgbtAm1 = (ToggleButton) view.findViewById(R.id.tbRadioAm1);
        this.imgbtAm2 = (ToggleButton) view.findViewById(R.id.tbRadioAm2);
        this.imgbtFm1 = (ToggleButton) view.findViewById(R.id.tbRadioFm1);
        this.imgbtFm2 = (ToggleButton) view.findViewById(R.id.tbRadioFm2);
        this.imgbtFm3 = (ToggleButton) view.findViewById(R.id.tbRadioFm3);
        this.imgbtRewardStation = (ImageButton) view.findViewById(R.id.btnReward);
        this.imgbtForwardStation = (ImageButton) view.findViewById(R.id.btnForward);
        this.txtDial = (TextView) view.findViewById(R.id.txtDial);
        this.txtStationName = (TextView) view.findViewById(R.id.txtStationName);
    }

    @Override
    public void initializeActions() {
        this.imgbtFm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAmFm(SET_FM1);
            }
        });
        this.imgbtFm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAmFm(SET_FM2);
            }
        });
        this.imgbtFm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAmFm(SET_FM3);
            }
        });
        this.imgbtAm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAmFm(SET_AM1);
            }
        });

        this.imgbtAm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAmFm(SET_AM2);
            }
        });

        this.imgbtRewardStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_SEEK_DOWN);
            }
        });

        this.imgbtForwardStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_SEEK_UP);
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
            canMSG.setData("003F00ffffffffff");
        else
            canMSG.setData("00F300ffffffffff");

        globals.sendCanCMDEvent(canMSG);
    }


    //################ ACTIONS ################
    private void setAmFm(int val) {
        imgbtAm1.setChecked(false);
        imgbtAm2.setChecked(false);
        imgbtFm1.setChecked(false);
        imgbtFm2.setChecked(false);
        imgbtFm3.setChecked(false);

        switch (val){
            case SET_FM1:
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_RADIO_FM1);
                imgbtFm1.setChecked(true);
                break;
            case SET_FM2:
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_RADIO_FM2);
                imgbtFm2.setChecked(true);
                break;
            case SET_FM3:
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_RADIO_FM3);
                imgbtFm3.setChecked(true);
                break;
            case SET_AM1:
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_RADIO_AM1);
                imgbtAm1.setChecked(true);
                break;
            case SET_AM2:
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_RADIO_AM2);
                imgbtAm2.setChecked(true);
                break;
        }

    }

    private void changeRadioName(String name) {
        txtStationName.setText(name);
    }

    //################ EVENTS ################
    public void onEventMainThread(final RadioPSEvent event) {
        RadioPSFrame radioPSFrame = event.getRadioPSFrame();
        txtStationName.setText(radioPSFrame.getRds());
    }

    public void onEventMainThread(final RadioStatusEvent event) {
        RADIOStatusFrame radioStatusFrame = event.getRadioStatusFrame();

        if(radioStatusFrame.getRadioBand().isBandAM())
        txtDial.setText(radioStatusFrame.getRadioFrequency().getAMDial());
        else
            txtDial.setText(radioStatusFrame.getRadioFrequency().getFMDial());
        setAmFm(radioStatusFrame.getRadioBand().getBand());
    }
}
