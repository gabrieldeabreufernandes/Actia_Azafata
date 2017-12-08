package br.com.actia.controller;

import android.util.Log;
import android.view.View;

import br.com.actia.component.MultipleStateButton;
import br.com.actia.model.DataTwoStates;
import br.com.actia.model.MultiplexControlFrame;
import br.com.actia.multiplex.MainActivity;
import br.com.actia.multiplex.R;

/**
 * Created by Armani andersonaramni@gmail.com on 29/10/16.
 */

public class AzafataController implements View.OnClickListener {
    private static final String TAG = "AZAFATA_CTRL";
    MultipleStateButton  azafataBtn;
    DataTwoStates        data;
    MainActivity         mainActivity;
    MessageBarController messageBarController;

    public AzafataController(MultipleStateButton btn, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        data = new DataTwoStates(0);
        azafataBtn = btn;
        azafataBtn.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_4);
        azafataBtn.setOnClickListener(this);

        messageBarController = MessageBarController.getInstance();
    }

    public void setFrame(DataTwoStates data) {
        //Does not process not changed states
        if(this.data.getState() == data.getState())
            return;

        int btnState = data.getState() == DataTwoStates.STATE_ON ? MultipleStateButton.STATE_4 : MultipleStateButton.STATE_2;
        azafataBtn.setState(btnState);
        this.data = data;
        if(data.getState() == DataTwoStates.STATE_ON)
            sendAzafataON();
        else
            sendAzafataOFF();

        //Get controlFrame current value
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.setAzafataData(this.data);
        //SEND frame with the changed Azafata data. This is necessary for IRIZAR implementation.
        mainActivity.sendMultiplexControlFrame(controlFrame);
    }

    public DataTwoStates getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        int state = azafataBtn.getState();

        //Always when click set state_2 for azafata
        this.data.setState(MultipleStateButton.STATE_2);

        switch (state) {
            case MultipleStateButton.STATE_1:
                break;
            case MultipleStateButton.STATE_2:
                sendAzafataOFF();
                break;
            case MultipleStateButton.STATE_4:
                sendAzafataOFF();

                //If Azafata is OFF, Keep Azafata OFF on press
                azafataBtn.setState(MultipleStateButton.STATE_2);
                break;
            default:
                Log.v(TAG, "State not alloawed");
                break;
        }
    }

    private void sendAzafataON() {
        messageBarController.setMessage(MessageBarController.TYPE_FIXED_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.azafata_on));
        Log.v(TAG, "sendAzafataON");
    }

    private void sendAzafataOFF() {
        //Send CMD to clear Azafata data
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getAzafataData().setState(DataTwoStates.STATE_OFF);

        mainActivity.sendMultiplexControlFrame(controlFrame);

        //Clear the fixed message
        messageBarController.clearFixedInformations();
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.azafata_off));
        Log.v(TAG, "sendAzafataOFF");
    }
}
