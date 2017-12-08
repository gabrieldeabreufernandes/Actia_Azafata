package br.com.actia.controller;

import android.util.Log;
import android.view.View;

/*import br.com.actia.communication.BuildCanMSG;
import br.com.actia.communication.CanMSG;
import br.com.actia.communication.CmdHandlerImp;*/
import br.com.actia.component.MultipleStateButton;
import br.com.actia.model.DataFourStates;
import br.com.actia.model.MultiplexControlFrame;
import br.com.actia.multiplex.MainActivity;
import br.com.actia.multiplex.R;

/**
 * Created by Armani andersonaramni@gmail.com on 29/10/16.
 */

public class WifiController implements View.OnClickListener {
    private static final String TAG = "WIFI_CTRL";
    MultipleStateButton wifiBtn;
    DataFourStates       data;
    MainActivity         mainActivity;
    MessageBarController messageBarController;

    public WifiController(MultipleStateButton btn, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        data = new DataFourStates(0);
        wifiBtn = btn;
        wifiBtn.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_4);
        wifiBtn.setOnClickListener(this);

        messageBarController = MessageBarController.getInstance();
    }

    public void setFrame(DataFourStates data) {
        wifiBtn.setState(data.getState());
        this.data = data;
    }

    public DataFourStates getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        int state = wifiBtn.getState();
        this.data.setState(state);

        switch (state) {
            case MultipleStateButton.STATE_1:
                break;
            case MultipleStateButton.STATE_2:
                sendWifiOFF();
                break;
            case MultipleStateButton.STATE_4:
                sendWifiON();
                break;
            default:
                Log.v(TAG, "State not alloawed");
                break;
        }
    }

    private void sendWifiON() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getWifiData().setState(DataFourStates.STATE_100);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.wifi_on));
        Log.v(TAG, "sendWifiON");
    }

    private void sendWifiOFF() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getWifiData().setState(DataFourStates.STATE_OFF);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.wifi_off));
        Log.v(TAG, "sendWifiOFF");
    }
}
