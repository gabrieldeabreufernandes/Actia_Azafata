package br.com.actia.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import br.com.actia.Globals;
import br.com.actia.communication.CanMSG;
import br.com.actia.dualzoneinterface.R;
import br.com.actia.event.RadioPSEvent;
import br.com.actia.event.RadioStatusEvent;
import br.com.actia.model.CONFIG.AppMenuBar;
import br.com.actia.model.CONFIG.AppWorkArea;
import br.com.actia.model.RADIOStatusFrame;
import br.com.actia.model.RadioPSFrame;

/**
 * Created by Armani andersonaramni@gmail.com on 20/02/17.
 */

public class RadioFragment extends Fragment {
    View view;
    private final Boolean SET_AM = true;
    private final Boolean SET_FM = false;
    private ToggleButton imgbtAm = null;
    private ToggleButton imgbtFm = null;
    private ImageButton imgbtRewardStation = null;
    private ImageButton imgbtForwardStation = null;
    private TextView txtDial = null;
    private TextView txtStationName = null;

    private boolean isDriver;
    private Globals globals = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_radio, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.imgbtAm = (ToggleButton) view.findViewById(R.id.tbRadioAm);
        this.imgbtFm = (ToggleButton) view.findViewById(R.id.tbRadioFm);
        this.imgbtRewardStation = (ImageButton) view.findViewById(R.id.btnReward);
        this.imgbtForwardStation = (ImageButton) view.findViewById(R.id.btnForward);
        this.txtDial = (TextView) view.findViewById(R.id.txtDial);
        this.txtStationName = (TextView) view.findViewById(R.id.txtStationName);

        initializeActions();

        globals = Globals.getInstance(getActivity().getApplication().getApplicationContext());
        globals.getEventBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        globals.getEventBus().unregister(this);
    }

    private void initializeActions() {
        this.imgbtAm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAmFm(SET_AM);
            }
        });

        this.imgbtFm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAmFm(SET_FM);
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

    public void configView(AppWorkArea appWorkArea, AppMenuBar appMenuBar) {

    }


    //################ ACTIONS ################
    private void setAmFm(Boolean isAmSet) {
        imgbtAm.setChecked(isAmSet);
        imgbtFm.setChecked(!isAmSet);
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
        setAmFm(radioStatusFrame.getRadioBand().isBandAM());
    }

    public void setIsDriver(boolean val) {
        isDriver = val;
    }
}
