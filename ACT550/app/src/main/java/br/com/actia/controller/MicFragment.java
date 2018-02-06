package br.com.actia.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.actia.Globals;
import br.com.actia.communication.CanMSG;
import br.com.actia.dualzoneinterface.R;

/**
 * Created by Armani andersonaramni@gmail.com on 20/02/17.
 * Edited by Gabriel Fernandes  gab.frosa@gmail.com
 */

public class MicFragment extends Fragment{
    private static final String TAG = "MicFragment";
    private View view;
    private boolean isDriver = false;
    //GAFR
    private Globals globals;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sd, container, false);

        globals = Globals.getInstance(getActivity().getApplication().getApplicationContext());
        //globals.getEventBus().register(this);

        Log.v(TAG, "MicFragment Running)");


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //GAFR
    public void sendChangeMode(Globals globals) {
        CanMSG canMSG = new CanMSG();
        canMSG.setId(CanMSG.MSGID_EQUIPAMENT_CTRL);
        canMSG.setLength((byte) 8);
        canMSG.setType(CanMSG.MSGTYPE_EXTENDED);

        /*
            TO DO:  to change Data to send frame with MIC data
         */
        if(isDriver)
            canMSG.setData("000F00ffffffffff");
        else
            canMSG.setData("00F000ffffffffff");

        globals.sendCanCMDEvent(canMSG);
    }

    public void setIsDriver(boolean val) {
        isDriver = val;
    }
}
