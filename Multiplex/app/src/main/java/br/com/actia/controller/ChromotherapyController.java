package br.com.actia.controller;


import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import br.com.actia.component.MultipleStateButton;
import br.com.actia.model.DataChromoColors;
import br.com.actia.model.DataChromotherapy;
import br.com.actia.model.DataFourStates;
import br.com.actia.model.DataTwoStates;
import br.com.actia.model.MultiplexControlFrame;
import br.com.actia.multiplex.MainActivity;
import br.com.actia.multiplex.R;

/**
 * Created by Armani andersonaramni@gmail.com on 28/10/16.
 */

public class ChromotherapyController implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private DataChromotherapy dataChromotherapy = null;
    private MultipleStateButton chromoButton;
    private MainActivity mainActivity;
    private LinearLayout chromoView;
    private LinearLayout chromoPane;
    private LinearLayout buttonsView;

    private ToggleButton btnBlue;
    private ToggleButton btnYellow;
    private ToggleButton btnCyan;
    private ToggleButton btnRed;
    private ToggleButton btnPurple;
    private ToggleButton btnOrange;
    private ToggleButton btnGreen;
    private ToggleButton btnPink;
    private ToggleButton btnWhiteGreen;
    private ToggleButton btnChromoAutomatic;

    private SeekBar chromoTime;
    private SeekBar chromoBrightness;

    private MessageBarController messageBarController;

    public ChromotherapyController(MultipleStateButton chromotherapyButton, MainActivity mainAct) {
        this.mainActivity = mainAct;
        this.chromoButton = chromotherapyButton;
        this.chromoButton.setValidStates(MultipleStateButton.VALID_STATE_2 | MultipleStateButton.VALID_STATE_4);
        this.dataChromotherapy = new DataChromotherapy();
        messageBarController = MessageBarController.getInstance();

        chromoView = (LinearLayout) mainActivity.findViewById(R.id.chromotherapy);
        chromoPane = (LinearLayout) mainActivity.findViewById(R.id.chromoPane);
        buttonsView = (LinearLayout) mainActivity.findViewById(R.id.buttonsPanel);

        btnBlue =   (ToggleButton) chromoView.findViewById(R.id.chbt_blue);
        btnYellow = (ToggleButton) chromoView.findViewById(R.id.chbt_yellow);
        btnCyan =   (ToggleButton) chromoView.findViewById(R.id.chbt_cyan);
        btnRed =    (ToggleButton) chromoView.findViewById(R.id.chbt_red);
        btnPurple = (ToggleButton) chromoView.findViewById(R.id.chbt_purple);
        btnOrange = (ToggleButton) chromoView.findViewById(R.id.chbt_orange);
        btnGreen =  (ToggleButton) chromoView.findViewById(R.id.chbt_green);
        btnPink =   (ToggleButton) chromoView.findViewById(R.id.chbt_pink);
        btnWhiteGreen = (ToggleButton) chromoView.findViewById(R.id.chbt_white_green);
        btnChromoAutomatic = (ToggleButton) chromoView.findViewById(R.id.chromoAutomatic);

        chromoTime = (SeekBar) chromoView.findViewById(R.id.chromo_time);
        chromoBrightness = (SeekBar) chromoView.findViewById(R.id.chromo_brightness);

        //Chromotherapy enabled
        chromoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeOutAnimation = AnimationUtils.loadAnimation(chromoView.getContext(), R.anim.fade_out);
                chromoView.startAnimation(fadeOutAnimation);
                chromoView.setVisibility(View.GONE);

                //buttonsView.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(buttonsView.getContext(), R.anim.fade_half_in);
                buttonsView.startAnimation(fadeIn);
                buttonsView.setAlpha((float) 1.0);
                buttonsView.setClickable(true);

                //Chromotherapy data enable chromotherapy
                //dataChromotherapy.getStatus().setState(DataFourStates.STATE_50); //setEnable(new DataTwoStates(DataTwoStates.STATE_ON));

                //Get controlFrame current value
                /*MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
                controlFrame.setChromotherapyData(dataChromotherapy);

                mainActivity.sendMultiplexControlFrame(controlFrame);
                messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                        mainActivity.getApplicationContext().getResources().getString(R.string.chromo_view_on));*/
            }
        });

        //Just to avoid exit chromotherapy clicking in this pane.
        chromoPane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //this.chromoButton.setOnClickListener(this);
        this.chromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Does nothing if it was disabled
                if(dataChromotherapy.getStatus().getState() == DataFourStates.STATE_DISABLE)
                    return;

                /*int state = chromoButton.getState();

                Log.v("CHROMO_BTN", String.valueOf(state));*/

                //Button pressed to show chromotherapy
                //if(state == MultipleStateButton.STATE_4) {
                    if(btnChromoAutomatic.isChecked()) {
                        //Set Chromotherapy to Automatic State
                        dataChromotherapy.getStatus().setState(DataFourStates.STATE_100);
                    }
                    else {
                        //Set Chromotherapy ON
                        dataChromotherapy.getStatus().setState(DataFourStates.STATE_50);
                    }

                    //Set chromotherapy button to ON
                    chromoButton.setState(MultipleStateButton.STATE_4);

                    Animation fadeInAnimation = AnimationUtils.loadAnimation(chromoView.getContext(), R.anim.fade_in);
                    chromoView.startAnimation(fadeInAnimation);
                    chromoView.setVisibility(View.VISIBLE);
                    //buttonsView.setVisibility();

                    //Block the other frameLayout
                    Animation fadeHalf = AnimationUtils.loadAnimation(buttonsView.getContext(), R.anim.fade_half);
                    buttonsView.startAnimation(fadeHalf);
                    buttonsView.setAlpha((float) 0.4);
                    buttonsView.setClickable(false);

                    /*messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                            mainActivity.getApplicationContext().getResources().getString(R.string.chromo_view_on));*/
                /*}
                else {
                    //Set Chromotherapy OFF
                    dataChromotherapy.getStatus().setState(DataFourStates.STATE_OFF);

                    //Get controlFrame current value
                    MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
                    controlFrame.setChromotherapyData(dataChromotherapy);

                    mainActivity.sendMultiplexControlFrame(controlFrame);
                    messageBarController.setMessage(MessageBarController.TYPE_INFORMATION,
                            mainActivity.getApplicationContext().getResources().getString(R.string.chromo_view_off));
                }*/
            }
        });

        //btn handlers
        btnBlue.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnCyan.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnPurple.setOnClickListener(this);
        btnOrange.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnPink.setOnClickListener(this);
        btnWhiteGreen.setOnClickListener(this);

        btnChromoAutomatic.setOnClickListener(this);
        chromoTime.setOnSeekBarChangeListener(this);
        chromoBrightness.setOnSeekBarChangeListener(this);
    }

    public DataChromotherapy getData() {
        return dataChromotherapy;
    }

    /**
     * Handle frame from Multiplex and set appropriated the chromotherapy screen
     * @param chromoData
     */
    public void handleFrame( DataChromotherapy chromoData ) {

        //Does not process data when chromotherapy is visible
        /*if( chromoView.getVisibility() == View.VISIBLE ) {
            return;
        }*/

        //It doesn't process data when chromotherapy is disabled
        /*if( chromoData.getEnable().getState() == DataTwoStates.STATE_OFF ) {
            chromoButton.setEnabled(false);
            return;
        }*/
        if( chromoData.getStatus().getState() == DataFourStates.STATE_DISABLE ) {
            //Disable chromotherapy button
            chromoButton.setState(MultipleStateButton.STATE_1);
        }
        else if( chromoData.getStatus().getState() == DataFourStates.STATE_OFF ) {
            //Enable chromotherapy button
            chromoButton.setState(MultipleStateButton.STATE_2);
        }
        else {
            //Set chromotherapy button
            chromoButton.setState(MultipleStateButton.STATE_4);
        }

        dataChromotherapy = chromoData;

        btnChromoAutomatic.setChecked(chromoData.getStatus().getState() == DataFourStates.STATE_100);

        chromoTime.setProgress( chromoData.getTime().getTime() );
        chromoBrightness.setProgress( chromoData.getBrightness().getValue() );

        int colors = chromoData.getColors().getValue();
        btnBlue.setChecked((colors & DataChromoColors.COLOR_BLUE) != 0);
        btnYellow.setChecked((colors & DataChromoColors.COLOR_YELLOW) != 0);
        btnCyan.setChecked((colors & DataChromoColors.COLOR_CYAN) != 0);
        btnRed.setChecked((colors & DataChromoColors.COLOR_RED) != 0);
        btnPurple.setChecked((colors & DataChromoColors.COLOR_PURPLE) != 0);
        btnOrange.setChecked((colors & DataChromoColors.COLOR_ORANGE) != 0);
        btnGreen.setChecked((colors & DataChromoColors.COLOR_GREEN) != 0);
        btnPink.setChecked((colors & DataChromoColors.COLOR_PINK) != 0);
        btnWhiteGreen.setChecked((colors & DataChromoColors.COLOR_WHITE_GREEN) != 0);
    }

    private void resetToggleButtons() {
        btnBlue.setChecked( false );
        btnYellow.setChecked( false );
        btnCyan.setChecked( false );
        btnRed.setChecked( false );
        btnPurple.setChecked( false );
        btnOrange.setChecked( false );
        btnGreen.setChecked( false );
        btnPink.setChecked( false );
        btnWhiteGreen.setChecked( false );
    }
    @Override
    public void onClick(View v) {
        int val = dataChromotherapy.getColors().getValue();

        //Removing the automatic value
        dataChromotherapy.getStatus().setState(DataFourStates.STATE_50);

        if(v == btnBlue) {
            btnAction((ToggleButton) v);
            btnChromoAutomatic.setChecked(false);
            val = ((ToggleButton)v).isChecked() ? DataChromoColors.COLOR_BLUE : 0;
            dataChromotherapy.getColors().setValue(val);
        }
        else if(v == btnYellow) {
            btnAction((ToggleButton) v);
            btnChromoAutomatic.setChecked(false);
            val = ((ToggleButton)v).isChecked() ? DataChromoColors.COLOR_YELLOW : 0;
            dataChromotherapy.getColors().setValue(val);
        }
        else if(v == btnCyan) {
            btnAction((ToggleButton) v);
            btnChromoAutomatic.setChecked(false);
            val = ((ToggleButton)v).isChecked() ? DataChromoColors.COLOR_CYAN : 0;
            dataChromotherapy.getColors().setValue(val);
        }
        else if(v == btnRed) {
            btnAction((ToggleButton) v);
            btnChromoAutomatic.setChecked(false);
            val = ((ToggleButton)v).isChecked() ? DataChromoColors.COLOR_RED : 0;
            dataChromotherapy.getColors().setValue(val);
        }
        else if(v == btnPurple) {
            btnAction((ToggleButton) v);
            btnChromoAutomatic.setChecked(false);
            val = ((ToggleButton)v).isChecked() ? DataChromoColors.COLOR_PURPLE : 0;
            dataChromotherapy.getColors().setValue(val);
        }
        else if(v == btnOrange) {
            btnAction((ToggleButton) v);
            btnChromoAutomatic.setChecked(false);
            val = ((ToggleButton)v).isChecked() ? DataChromoColors.COLOR_ORANGE : 0;
            dataChromotherapy.getColors().setValue(val);
        }
        else if(v == btnGreen) {
            btnAction((ToggleButton) v);
            btnChromoAutomatic.setChecked(false);
            val = ((ToggleButton)v).isChecked() ? DataChromoColors.COLOR_GREEN : 0;
            dataChromotherapy.getColors().setValue(val);
        }
        else if(v == btnPink) {
            btnAction((ToggleButton) v);
            btnChromoAutomatic.setChecked(false);
            val = ((ToggleButton)v).isChecked() ? DataChromoColors.COLOR_PINK : 0;
            dataChromotherapy.getColors().setValue(val);
        }
        else if(v == btnWhiteGreen) {
            btnAction((ToggleButton) v);
            btnChromoAutomatic.setChecked(false);
            val = ((ToggleButton)v).isChecked() ? DataChromoColors.COLOR_WHITE_GREEN : 0;
            dataChromotherapy.getColors().setValue(val);
        }
        else if(v == btnChromoAutomatic) {

            if(((ToggleButton)v).isChecked() == true) {
                //set to Automatic State
                dataChromotherapy.getStatus().setState(DataFourStates.STATE_100);
                resetToggleButtons();
                dataChromotherapy.getColors().setValue(DataChromoColors.COLOR_NO_COLOR);

                Log.v("CROMO", "TRUE");
            }
            else {
                //set to State ON
                dataChromotherapy.getStatus().setState(DataFourStates.STATE_50);
                dataChromotherapy.getColors().setValue(DataChromoColors.COLOR_NO_COLOR);

                Log.v("CROMO", "FALSE");
            }
        }

        //Get controlFrame current value to send frame to Multiplex
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.setChromotherapyData(dataChromotherapy);

        mainActivity.sendMultiplexControlFrame(controlFrame);

    }

    private void btnAction(ToggleButton bt) {
        boolean isToSet = (bt.isChecked() == true);

        resetToggleButtons();

        if(isToSet) {
            bt.setChecked(true);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar == chromoTime) {
            dataChromotherapy.getTime().setTime(chromoTime.getProgress());
        }
        else if(seekBar == chromoBrightness) {
            dataChromotherapy.getBrightness().setValue(chromoBrightness.getProgress());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //Get controlFrame current value to send frame to Multiplex
        MultiplexControlFrame controlFrame = mainActivity.getMultiplexControlFrame();
        controlFrame.setChromotherapyData(dataChromotherapy);

        mainActivity.sendMultiplexControlFrame(controlFrame);
    }
}
