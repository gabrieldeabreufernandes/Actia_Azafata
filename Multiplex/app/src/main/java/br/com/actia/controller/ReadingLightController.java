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

public class ReadingLightController implements View.OnClickListener {
    private static final String TAG = "READING_LIGHT_CTRL";
    MultipleStateButton  readingLightBtn;
    DataFourStates       data;
    MainActivity         mainActivity;
    MessageBarController messageBarController;

    public ReadingLightController(MultipleStateButton btn, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        data = new DataFourStates(0);
        readingLightBtn = btn;
        readingLightBtn.setOnClickListener(this);
        readingLightBtn.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_3 | MultipleStateButton.VALID_STATE_4);

        messageBarController = MessageBarController.getInstance();
    }

    public void setFrame(DataFourStates data) {
        readingLightBtn.setState(data.getState());
        this.data = data;
    }

    public DataFourStates getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        int state = readingLightBtn.getState();
        this.data.setState(state);

        switch (state) {
            case MultipleStateButton.STATE_1:
                break;
            case MultipleStateButton.STATE_2:
                sendReadingLightOFF();
                break;
            case MultipleStateButton.STATE_3:
                sendReadingLight50();
                break;
            case MultipleStateButton.STATE_4:
                sendReadingLight100();
                break;
            default:
                Log.v(TAG, "State not alloawed");
                break;
        }
    }

    private void sendReadingLight100() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getReadingLights().setState(DataFourStates.STATE_100);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.reading_light_2));
        Log.v(TAG, "sendReadingLight100");
    }
    private void sendReadingLight50() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getReadingLights().setState(DataFourStates.STATE_50);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.reading_light_1));
        Log.v(TAG, "sendReadingLight50");
    }
    private void sendReadingLightOFF() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getReadingLights().setState(DataFourStates.STATE_OFF);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.reading_light_off));
        Log.v(TAG, "sendReadingLightOFF");
    }
}
