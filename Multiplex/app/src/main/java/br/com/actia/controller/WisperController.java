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

public class WisperController implements View.OnClickListener {
    private static final String TAG = "WISPER_CTRL";
    MultipleStateButton  wisperBtn;
    DataFourStates       data;
    MainActivity         mainActivity;
    MessageBarController messageBarController;

    public WisperController(MultipleStateButton btn, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        data = new DataFourStates(0);
        wisperBtn = btn;
        wisperBtn.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_3 | MultipleStateButton.VALID_STATE_4);
        wisperBtn.setOnClickListener(this);

        messageBarController = MessageBarController.getInstance();
    }

    public void setFrame(DataFourStates data) {
        wisperBtn.setState(data.getState());
        this.data = data;
    }

    public DataFourStates getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        int state = wisperBtn.getState();
        this.data.setState(state);

        switch (state) {
            case MultipleStateButton.STATE_1:
                break;
            case MultipleStateButton.STATE_2:
                sendWisperOFF();
                break;
            case MultipleStateButton.STATE_3:
                sendWisper50();
                break;
            case MultipleStateButton.STATE_4:
                sendWisper100();
                break;
            default:
                Log.v(TAG, "State not alloawed");
                break;
        }
    }

    private void sendWisper100() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getWisperData().setState(DataFourStates.STATE_100);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.wisper_2));
        Log.v(TAG, "sendWisper100");
    }

    private void sendWisper50() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getWisperData().setState(DataFourStates.STATE_50);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.wisper_1));
        Log.v(TAG, "sendWisper50");
    }

    private void sendWisperOFF() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getWisperData().setState(DataFourStates.STATE_OFF);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.wisper_off));
        Log.v(TAG, "sendWisperOFF");
    }
}
