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

public class WCController implements View.OnClickListener {
    private static final String TAG = "WC_CTRL";
    MultipleStateButton wcBtn;
    DataFourStates      data;
    MainActivity        mainActivity;
    MessageBarController messageBarController;

    public WCController(MultipleStateButton btn, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        data = new DataFourStates(0);
        wcBtn = btn;
        wcBtn.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_4);
        wcBtn.setOnClickListener(this);

        messageBarController = MessageBarController.getInstance();
    }

    public void setFrame(DataFourStates data) {
        wcBtn.setState(data.getState());
        this.data = data;
    }

    public DataFourStates getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        int state = wcBtn.getState();
        this.data.setState(state);

        switch (state) {
            case MultipleStateButton.STATE_1:
                break;
            case MultipleStateButton.STATE_2:
                sendWcOFF();
                break;
            case MultipleStateButton.STATE_4:
                sendWcON();
                break;
            default:
                Log.v(TAG, "State not alloawed");
                break;
        }
    }

    private void sendWcON() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getWcData().setState(DataFourStates.STATE_100);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.wc_on));
        Log.v(TAG, "sendWcON");
    }

    private void sendWcOFF() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getWcData().setState(DataFourStates.STATE_OFF);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.wc_off));
        Log.v(TAG, "sendWcOFF");
    }
}
