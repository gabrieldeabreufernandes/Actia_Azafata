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

public class SeatNumberController implements View.OnClickListener {
    private static final String TAG = "SEAT_NUMBER_CTRL";
    private MultipleStateButton  seatNumberBtn;
    private DataFourStates       data;
    private MessageBarController messageBarController;
    private MainActivity mainActivity;

    public SeatNumberController(MultipleStateButton btn, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        data = new DataFourStates(0);
        seatNumberBtn = btn;
        seatNumberBtn.setOnClickListener(this);
        seatNumberBtn.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_4);

        messageBarController = MessageBarController.getInstance();
    }

    public void setFrame(DataFourStates data) {
        seatNumberBtn.setState(data.getState());
        this.data = data;
    }

    public DataFourStates getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        int state = seatNumberBtn.getState();
        this.data.setState(state);

        switch (state) {
            case MultipleStateButton.STATE_1:
                break;
            case MultipleStateButton.STATE_2:
                sendSeatNumberOFF();
                break;
            case MultipleStateButton.STATE_4:
                sendSeatNumberON();
                break;
            default:
                Log.v(TAG, "State not alloawed");
                break;
        }
    }

    private void sendSeatNumberON() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getSeatNumData().setState(DataFourStates.STATE_100);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.seat_number_on));
        Log.v(TAG, "sendSeatNumberON");
    }

    private void sendSeatNumberOFF() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getSeatNumData().setState(DataFourStates.STATE_OFF);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.seat_number_off));
        Log.v(TAG, "sendSeatNumberOFF");
    }
}
