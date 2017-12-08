package br.com.actia.multiplex;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import br.com.actia.Globals;
import br.com.actia.communication.CanMSG;
import br.com.actia.communication.DeviceCOM.DeviceCom;
import br.com.actia.component.MultipleStateButton;
import br.com.actia.controller.AzafataController;
import br.com.actia.controller.ChromotherapyController;
import br.com.actia.controller.CoffeeController;
import br.com.actia.controller.InternLightController;
import br.com.actia.controller.LoadConfigurations;
import br.com.actia.controller.MessageBarController;
import br.com.actia.controller.ReadingLightController;
import br.com.actia.controller.RearDoorController;
import br.com.actia.controller.RefrigeratorController;
import br.com.actia.controller.SeatNumberController;
import br.com.actia.controller.WCController;
import br.com.actia.controller.WifiController;
import br.com.actia.controller.WisperController;
import br.com.actia.event.MultiplexStatusEvent;
import br.com.actia.model.AppButton;
import br.com.actia.model.FileStructure;
import br.com.actia.model.MultiplexControlFrame;
import br.com.actia.model.MultiplexStatusFrame;
import br.com.actia.service.CanComService;

/**
 * A full-screen activity that hides the system UI (i.e.
 * status bar and navigation/system bar).
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Globals         globals;
    private View            mContentView;
    private LinearLayout    brandBarView;
    private LinearLayout    buttonPaneView;
    private LinearLayout    messageBarView;
    private TextView        titleArea1;
    private TextView        titleArea2;


    private WifiController          wifiController;
    private WCController            wcController;
    private CoffeeController        coffeeController;
    private RefrigeratorController  refrigeratorController;
    private WisperController        wisperController;
    private RearDoorController      rearDoorController;
    private InternLightController   internLightController;
    private ReadingLightController  readingLightController;
    private SeatNumberController    seatNumberController;
    private AzafataController       azafataController;
    private ChromotherapyController chromotherapyController;
    private Intent                  intentService = null;
    private FileStructure           fileStructure;

    private MessageBarController    messageBarController;

    byte btData[] = {(byte)0x7E, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};

    private MultipleStateButton mbtWifi         = null;
    private MultipleStateButton mbtWc           = null;
    private MultipleStateButton mbtCoffee       = null;
    private MultipleStateButton mbtRefrigerator = null;
    private MultipleStateButton mbtWisper       = null;
    private MultipleStateButton mbtRearDoor     = null;
    private MultipleStateButton mbtInternLight  = null;
    private MultipleStateButton mbtReadingLight = null;
    private MultipleStateButton mbtSeatNumber   = null;
    private MultipleStateButton mbtAzafata      = null;
    private MultipleStateButton mbtChromotherapy= null;

    //for tests
    private MultiplexStatusFrame lastStatusFrame = null;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hide Android action bar
        hide();

        //Enable Bluetooth Automatically
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            Toast.makeText(getApplicationContext(), "Bluetooth switched ON", Toast.LENGTH_LONG).show();
        }

        brandBarView   = (LinearLayout) findViewById(R.id.brand_bar);
        buttonPaneView = (LinearLayout) findViewById(R.id.buttonsPanel);
        messageBarView = (LinearLayout) findViewById(R.id.messa_bar_view);
        titleArea1     = (TextView) findViewById(R.id.area1_title);
        titleArea2     = (TextView) findViewById(R.id.area2_title);

        //Initialize message controller singleton
        messageBarController = MessageBarController.getInstance();
        messageBarController.initializeData(this);

        //INITIALIZE BUTTONS
        mbtWifi = (MultipleStateButton)findViewById(R.id.btn_wifi);
        mbtWc   = (MultipleStateButton)findViewById(R.id.btn_wc);
        mbtCoffee = (MultipleStateButton)findViewById(R.id.btn_coffee);
        mbtRefrigerator = (MultipleStateButton)findViewById(R.id.btn_refrigerator);
        mbtWisper = (MultipleStateButton)findViewById(R.id.btn_wisper);
        mbtRearDoor = (MultipleStateButton)findViewById(R.id.btn_rcd);
        mbtInternLight = (MultipleStateButton)findViewById(R.id.btn_intern_light);
        mbtReadingLight = (MultipleStateButton)findViewById(R.id.btn_reading_light);
        mbtSeatNumber = (MultipleStateButton)findViewById(R.id.btn_seat_num);
        mbtAzafata = (MultipleStateButton)findViewById(R.id.btn_azafata);
        mbtChromotherapy = (MultipleStateButton)findViewById(R.id.btn_chromotherapy);

        //INITIALIZE CONTROLLERS
        wifiController =            new WifiController(mbtWifi, this);
        wcController =              new WCController(mbtWc, this);
        coffeeController =          new CoffeeController(mbtCoffee, this);
        refrigeratorController =    new RefrigeratorController(mbtRefrigerator, this);
        wisperController =          new WisperController(mbtWisper, this);
        rearDoorController =        new RearDoorController(mbtRearDoor, this);
        internLightController =     new InternLightController(mbtInternLight, this);
        readingLightController =    new ReadingLightController(mbtReadingLight, this);
        seatNumberController =      new SeatNumberController(mbtSeatNumber, this);
        azafataController =         new AzafataController(mbtAzafata, this);
        chromotherapyController =   new ChromotherapyController(mbtChromotherapy, this);

        Log.v(TAG, "Starting LoadConfigurations()");

        LoadConfigurations loader = new LoadConfigurations();
        fileStructure = loader.getFileStructure();

        if(fileStructure != null) {
            Log.v(TAG, "fileStructure FOUND");
            setConfigurations();
        }
        else {
            Log.v(TAG, "fileStructure == NULL");
        }

        //Register event receiver
        globals = Globals.getInstance(this);
        globals.getEventBus().register(this);

        /*byte bt[] = new byte[8];
        bt[0] = (byte)0xFF;
        bt[1] = (byte)0xFF;
        bt[2] = (byte)0xFF;
        bt[3] = (byte)0xF2; //39
        bt[4] = (byte)0x00;
        bt[5] = (byte)0xE4;
        bt[6] = (byte)0x00;
        bt[7] = (byte)0x00;
        globals.getEventBus().post(new MultiplexStatusEvent(bt));*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        hide();

        //Start service
        intentService = new Intent(this, CanComService.class);
        intentService.putExtra("COM_TYPE", DeviceCom.DEVICE_BT_C2BT);
        startService(intentService);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, CanComService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, CanComService.class));
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /**
     * Return a multiplex control frame with all configurations
     * @return
     */
    public MultiplexControlFrame getMultiplexControlFrame() {
        MultiplexControlFrame multiplexControlFrame = new MultiplexControlFrame();
        multiplexControlFrame.setAzafataData(azafataController.getData());
        multiplexControlFrame.setBackDoorData(rearDoorController.getData());
        multiplexControlFrame.setCoffeMachineData(coffeeController.getData());
        multiplexControlFrame.setInternalLightData(internLightController.getData());
        multiplexControlFrame.setReadingLights(readingLightController.getData());
        multiplexControlFrame.setRefrigeratorData(refrigeratorController.getData());
        multiplexControlFrame.setSeatNumData(seatNumberController.getData());
        multiplexControlFrame.setWcData(wcController.getData());
        multiplexControlFrame.setWifiData(wifiController.getData());
        multiplexControlFrame.setWisperData(wisperController.getData());
        multiplexControlFrame.setChromotherapyData(chromotherapyController.getData());

        try {
            //Toast.makeText(getApplicationContext(), "Simple CLONE was made!", Toast.LENGTH_LONG).show();
            return multiplexControlFrame.clone();
        } catch (CloneNotSupportedException e) {
            //Toast.makeText(getApplicationContext(), "Simple copy was made!", Toast.LENGTH_LONG).show();
            return multiplexControlFrame;
        }
    }

    /**
     * Send to multiplex a Control Frame
     */
    public void sendMultiplexControlFrame(MultiplexControlFrame multiplexControlFrame) {

        Log.d(TAG, "### MULTIPLEX CONTROL FRAME SENT = " + multiplexControlFrame);

        CanMSG canMSG = new CanMSG(CanMSG.MSGID_MULTIPLEX_CMD, CanMSG.MSGTYPE_EXTENDED, (byte)8,
                                    multiplexControlFrame.getDataArray());

        Log.v(TAG, canMSG.toString());
        globals.sendCanCMDEvent(canMSG);
    }

    /**
     * EventBus event callback
     * @param event MultiplexStatusEvent
     */
    public void onEventMainThread(final MultiplexStatusEvent event) {
        //Log.v(TAG, "onEventMainThread");

        MultiplexStatusFrame frame = event.getMultiplexStatusFrame();


        // IMPLEMENTED to send an ECHO for MUX when a data was changed by MUX
        if(lastStatusFrame == null || !lastStatusFrame.dataIsEqual(frame.getData()) ) {
            lastStatusFrame = frame;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CanMSG canMSG = new CanMSG(CanMSG.MSGID_MULTIPLEX_CMD, CanMSG.MSGTYPE_EXTENDED,
                            CanMSG.DEFAULT_MSG_LEN, lastStatusFrame.getData());

                    globals.sendCanCMDEvent(canMSG);
                }
            }, 100); //100ms that Marcos propouse
        }

        wifiController.setFrame( frame.getWifiData() );
        wcController.setFrame( frame.getWcData() );
        coffeeController.setFrame( frame.getCoffeMachineData() );
        refrigeratorController.setFrame( frame.getRefrigeratorData() );
        wisperController.setFrame( frame.getWisperData() );
        rearDoorController.setFrame( frame.getBackDoorData() );

        internLightController.setFrame( frame.getInternalLightData() );
        readingLightController.setFrame( frame.getReadingLights() );
        seatNumberController.setFrame( frame.getSeatNumData() );
        azafataController.setFrame( frame.getAzafataData() );
        chromotherapyController.handleFrame( frame.getChromotherapyData() );
    }

    /**
     * Set APP configurations from Configuration File
     */
    private void setConfigurations() {
        try
        {
            //Set main button area background color
            if(!fileStructure.getTopBar().getBgColor().isEmpty())
                brandBarView.setBackgroundColor(Color.parseColor(fileStructure.getTopBar().getBgColor()));
            if(!fileStructure.getBgColor().isEmpty())
                buttonPaneView.setBackgroundColor(Color.parseColor(fileStructure.getBgColor()));
            if(!fileStructure.getBottomArea().getBgColor().isEmpty())
                messageBarView.setBackgroundColor(Color.parseColor(fileStructure.getBottomArea().getBgColor()));
            //Button Area Title1
            if(!fileStructure.getButtonArea().getTitleColor().isEmpty())
                titleArea1.setTextColor(Color.parseColor(fileStructure.getButtonArea().getTitleColor()));
            if(!fileStructure.getButtonArea().getTitle1().isEmpty())
                titleArea1.setText(fileStructure.getButtonArea().getTitle1());
            //Button Area Title2
            if(!fileStructure.getButtonArea().getTitleColor().isEmpty())
                titleArea2.setTextColor(Color.parseColor(fileStructure.getButtonArea().getTitleColor()));
            if(!fileStructure.getButtonArea().getTitle2().isEmpty())
                titleArea2.setText(fileStructure.getButtonArea().getTitle2());

            //Chromotherapy;
            TextView txtTitle = (TextView) findViewById(R.id.chromo_title);
            if(!fileStructure.getChromo().getTitle().isEmpty())
                txtTitle.setText(fileStructure.getChromo().getTitle());

            if(!fileStructure.getChromo().getTitleColor().isEmpty())
                txtTitle.setTextColor(Color.parseColor(fileStructure.getChromo().getTitleColor()));

            if(!fileStructure.getChromo().getTitleColor().isEmpty()) {
                TextView txtTime = (TextView) findViewById(R.id.chromo_time_txt);
                txtTime.setTextColor(Color.parseColor(fileStructure.getChromo().getTitleColor()));
            }

            if(!fileStructure.getChromo().getTitleColor().isEmpty()) {
                TextView txtBrightness = (TextView) findViewById(R.id.chromo_brightness_txt);
                txtBrightness.setTextColor(Color.parseColor(fileStructure.getChromo().getTitleColor()));
            }

            if(!fileStructure.getChromo().getBgColor().isEmpty()) {
                LinearLayout chromoPane = (LinearLayout)findViewById(R.id.chromoPane);
                chromoPane.setBackgroundColor(Color.parseColor(fileStructure.getChromo().getBgColor()));
            }

            ToggleButton mbtBlue = (ToggleButton) findViewById(R.id.chbt_blue);
            ToggleButton mbtYellow = (ToggleButton) findViewById(R.id.chbt_yellow);
            ToggleButton mbtCyan = (ToggleButton) findViewById(R.id.chbt_cyan);
            ToggleButton mbtRed = (ToggleButton) findViewById(R.id.chbt_red);
            ToggleButton mbtPurple = (ToggleButton) findViewById(R.id.chbt_purple);
            ToggleButton mbtOrange = (ToggleButton) findViewById(R.id.chbt_orange);
            ToggleButton mbtGreen = (ToggleButton) findViewById(R.id.chbt_green);
            ToggleButton mbtPink = (ToggleButton) findViewById(R.id.chbt_pink);
            ToggleButton mbtWhiteGreen = (ToggleButton) findViewById(R.id.chbt_white_green);

            if(!fileStructure.getChromo().getBlue().isEmpty())
                mbtBlue.setBackgroundColor(Color.parseColor(fileStructure.getChromo().getBlue()));
            if(!fileStructure.getChromo().getYellow().isEmpty())
                mbtYellow.setBackgroundColor(Color.parseColor(fileStructure.getChromo().getYellow()));
            if(!fileStructure.getChromo().getCyan().isEmpty())
                mbtCyan.setBackgroundColor(Color.parseColor(fileStructure.getChromo().getCyan()));
            if(!fileStructure.getChromo().getRed().isEmpty())
                mbtRed.setBackgroundColor(Color.parseColor(fileStructure.getChromo().getRed()));
            if(!fileStructure.getChromo().getPurple().isEmpty())
                mbtPurple.setBackgroundColor(Color.parseColor(fileStructure.getChromo().getPurple()));
            if(!fileStructure.getChromo().getOrange().isEmpty())
                mbtOrange.setBackgroundColor(Color.parseColor(fileStructure.getChromo().getOrange()));
            if(!fileStructure.getChromo().getGreen().isEmpty())
                mbtGreen.setBackgroundColor(Color.parseColor(fileStructure.getChromo().getGreen()));
            if(!fileStructure.getChromo().getPink().isEmpty())
                mbtPink.setBackgroundColor(Color.parseColor(fileStructure.getChromo().getPink()));
            if(!fileStructure.getChromo().getWhiteGreen().isEmpty())
                mbtWhiteGreen.setBackgroundColor(Color.parseColor(fileStructure.getChromo().getWhiteGreen()));

            setButtonDrawable(fileStructure.getButton());
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
            messageBarController.setMessage(MessageBarController.TYPE_ERROR, getResources().getString(R.string.err_config_file));
        }
    }

    /**
     * Set buttons drawable background
     * @param button
     */
    private void setButtonDrawable(AppButton button) {
        // Set buttons background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mbtWifi.setBackground(getLevelListButtons(button));

            mbtWifi.setState(MultipleStateButton.STATE_2);

            mbtWc.setBackground(getLevelListButtons(button));
            mbtWc.setState(MultipleStateButton.STATE_2);

            mbtCoffee.setBackground(getLevelListButtons(button));
            mbtCoffee.setState(MultipleStateButton.STATE_2);

            mbtRefrigerator.setBackground(getLevelListButtons(button));
            mbtRefrigerator.setState(MultipleStateButton.STATE_2);

            mbtWisper.setBackground(getLevelListButtons(button));
            mbtWisper.setState(MultipleStateButton.STATE_2);

            mbtRearDoor.setBackground(getLevelListButtons(button));
            mbtRearDoor.setState(MultipleStateButton.STATE_2);

            mbtInternLight.setBackground(getLevelListButtons(button));
            mbtInternLight.setState(MultipleStateButton.STATE_2);

            mbtReadingLight.setBackground(getLevelListButtons(button));
            mbtReadingLight.setState(MultipleStateButton.STATE_2);

            mbtSeatNumber.setBackground(getLevelListButtons(button));
            mbtSeatNumber.setState(MultipleStateButton.STATE_2);

            mbtAzafata.setBackground(getLevelListButtons(button));
            mbtAzafata.setState(MultipleStateButton.STATE_2);

            mbtChromotherapy.setBackground(getLevelListButtons(button));
            mbtChromotherapy.setState(MultipleStateButton.STATE_2);
        }
    }

    /**
     *
     * @param button AppButton
     * @return Level List Drawable
     */
    private LevelListDrawable getLevelListButtons(AppButton button) {
        int[] colors1 = {Color.parseColor(button.getDisableColor1()),Color.parseColor(button.getDisableColor2())};
        int[] colors2 = {Color.parseColor(button.getNormalColor1()),Color.parseColor(button.getNormalColor2())};
        int[] colors3 = {Color.parseColor(button.getState1Color1()),Color.parseColor(button.getState1Color2())};
        int[] colors4 = {Color.parseColor(button.getState2Color1()),Color.parseColor(button.getState2Color2())};

        GradientDrawable gd1 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors1);
        gd1.setCornerRadius(0f);

        GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors2);
        gd2.setCornerRadius(0f);

        GradientDrawable gd3 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors3);
        gd3.setCornerRadius(0f);

        GradientDrawable gd4 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors4);
        gd4.setCornerRadius(0f);

        LevelListDrawable levelListDrawable = new LevelListDrawable();
        levelListDrawable.addLevel(0, 0, gd1);
        levelListDrawable.addLevel(1, 1, gd2);
        levelListDrawable.addLevel(2, 2, gd3);
        levelListDrawable.addLevel(3, 3, gd4);

        return levelListDrawable;
    }
}
