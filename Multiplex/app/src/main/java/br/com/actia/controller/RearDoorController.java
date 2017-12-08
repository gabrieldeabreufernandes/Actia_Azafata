package br.com.actia.controller;

import android.util.Log;
import android.view.View;

import br.com.actia.component.MultipleStateButton;
import br.com.actia.model.DataFourStates;
import br.com.actia.model.MultiplexControlFrame;
import br.com.actia.multiplex.MainActivity;
import br.com.actia.multiplex.R;

/**
 * Created by Armani andersonaramni@gmail.com on 29/10/16.
 */

public class RearDoorController implements View.OnClickListener {
    private static final String TAG = "REAR_DOOR_CTRL";
    MultipleStateButton rearDoorBtn;
    DataFourStates       data;
    MainActivity         mainActivity;
    MessageBarController messageBarController;

    public RearDoorController(MultipleStateButton btn, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        data = new DataFourStates(0);
        rearDoorBtn = btn;
        rearDoorBtn.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_4);
        rearDoorBtn.setOnClickListener(this);

        messageBarController = MessageBarController.getInstance();
    }

    public void setFrame(DataFourStates data) {
        rearDoorBtn.setState(data.getState());
        this.data = data;
    }

    public DataFourStates getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        int state = rearDoorBtn.getState();
        this.data.setState(state);

        switch (state) {
            case MultipleStateButton.STATE_1:
                break;
            case MultipleStateButton.STATE_2:
                sendRearDoorOFF();
                break;
            case MultipleStateButton.STATE_4:
                sendRearDoorON();
                break;
            default:
                Log.v(TAG, "State not alloawed");
                break;
        }
    }

    private void sendRearDoorON() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getBackDoorData().setState(DataFourStates.STATE_100);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.rear_door_on));
        Log.v(TAG, "sendRearDoorON");
    }

    private void sendRearDoorOFF() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getBackDoorData().setState(DataFourStates.STATE_OFF);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.rear_door_off));
        Log.v(TAG, "sendRearDoorOFF");
    }
}
