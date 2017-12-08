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

public class RefrigeratorController implements View.OnClickListener {
    private static final String TAG = "REFRIGERATOR_CTRL";
    MultipleStateButton  refrigeratorBtn;
    DataFourStates       data;
    MainActivity         mainActivity;
    MessageBarController messageBarController;

    public RefrigeratorController(MultipleStateButton btn, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        data = new DataFourStates(0);
        refrigeratorBtn = btn;
        refrigeratorBtn.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_4);
        refrigeratorBtn.setOnClickListener(this);

        messageBarController = MessageBarController.getInstance();
    }

    public void setFrame(DataFourStates data) {
        refrigeratorBtn.setState(data.getState());
        this.data = data;
    }

    public DataFourStates getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        int state = refrigeratorBtn.getState();
        this.data.setState(state);

        switch (state) {
            case MultipleStateButton.STATE_1:
                break;
            case MultipleStateButton.STATE_2:
                sendRefrigeratorOFF();
                break;
            case MultipleStateButton.STATE_4:
                sendRefrigeratorON();
                break;
            default:
                Log.v(TAG, "State not alloawed");
                break;
        }
    }

    private void sendRefrigeratorON() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getRefrigeratorData().setState(DataFourStates.STATE_100);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.refrigerator_on));
        Log.v(TAG, "sendRefrigeratorON");
    }

    private void sendRefrigeratorOFF() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getRefrigeratorData().setState(DataFourStates.STATE_OFF);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.refrigerator_off));
        Log.v(TAG, "sendRefrigeratorOFF");
    }
}
