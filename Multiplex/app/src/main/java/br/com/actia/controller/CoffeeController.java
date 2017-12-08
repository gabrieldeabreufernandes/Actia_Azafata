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

public class CoffeeController implements View.OnClickListener {
    private static final String TAG = "COFFEE_CTRL";
    private MultipleStateButton coffeeBtn;
    private DataFourStates      data;
    private MainActivity        mainActivity;
    private MessageBarController messageBarController;

    public CoffeeController( MultipleStateButton btn, MainActivity mainActivity ) {
        this.mainActivity = mainActivity;
        data = new DataFourStates(0);
        coffeeBtn = btn;
        coffeeBtn.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_4);
        coffeeBtn.setOnClickListener(this);

        messageBarController = MessageBarController.getInstance();
    }

    public void setFrame(DataFourStates data) {
        coffeeBtn.setState(data.getState());
        this.data = data;
    }

    public DataFourStates getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        int state = coffeeBtn.getState();
        this.data.setState(state);

        switch (state) {
            case MultipleStateButton.STATE_1:
                break;
            case MultipleStateButton.STATE_2:
                sendCoffeeOFF();
                break;
            case MultipleStateButton.STATE_4:
                sendCoffeeON();
                break;
            default:
                Log.v(TAG, "State not alloawed");
                break;
        }
    }

    private void sendCoffeeON() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getCoffeMachineData().setState(DataFourStates.STATE_100);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.coffee_on));
        Log.v(TAG, "sendCoffeON");
    }

    private void sendCoffeeOFF() {
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.getCoffeMachineData().setState(DataFourStates.STATE_OFF);

        mainActivity.sendMultiplexControlFrame(controlFrame);
        messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                mainActivity.getApplicationContext().getResources().getString(R.string.coffee_off));
        Log.v(TAG, "sendCoffeOFF");
    }
}
