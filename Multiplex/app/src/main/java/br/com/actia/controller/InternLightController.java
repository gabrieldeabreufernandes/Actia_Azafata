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

public class InternLightController implements View.OnClickListener {
    private static final String TAG = "INTERN_LIGHT_CTRL";
    MultipleStateButton internLightBtn;
    DataFourStates      data;
    private MainActivity mainActivity;
    private MessageBarController messageBarController;

    public InternLightController(MultipleStateButton btn, MainActivity mainActivity ) {
        this.mainActivity = mainActivity;
        data = new DataFourStates(0);
        internLightBtn = btn;
        internLightBtn.setOnClickListener(this);
        internLightBtn.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_3 | MultipleStateButton.VALID_STATE_4);

        messageBarController = MessageBarController.getInstance();
    }

    public void setFrame(DataFourStates data) {
        internLightBtn.setState(data.getState());
        this.data = data;
    }

    public DataFourStates getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        int state = internLightBtn.getState();
        this.data.setState(state);

        switch (state) {
            case MultipleStateButton.STATE_1:
                break;
            case MultipleStateButton.STATE_2:
                sendInternLightOFF();
                break;
            case MultipleStateButton.STATE_3:
                sendInternLight50();
                break;
            case MultipleStateButton.STATE_4:
                sendInternLight100();
                break;
            default:
                Log.v(TAG, "State not alloawed");
                break;
        }
    }

    private void sendInternLight100() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getInternalLightData().setState(DataFourStates.STATE_100);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.internal_light_2));
        Log.v(TAG, "sendInternLight100");
    }
    private void sendInternLight50() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getInternalLightData().setState(DataFourStates.STATE_50);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.internal_light_1));
        Log.v(TAG, "sendInternLight50");
    }
    private void sendInternLightOFF() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getInternalLightData().setState(DataFourStates.STATE_OFF);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.internal_light_off));
        Log.v(TAG, "sendInternLightOFF");
    }
}
