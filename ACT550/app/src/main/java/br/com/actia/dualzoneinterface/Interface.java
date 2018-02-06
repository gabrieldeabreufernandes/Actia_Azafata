package br.com.actia.dualzoneinterface;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.actia.Globals;
import br.com.actia.communication.DeviceCOM.DeviceCom;
import br.com.actia.controller.AuxFragment;
import br.com.actia.controller.ConfigFragment;
import br.com.actia.controller.LoadConfigurations;
import br.com.actia.controller.MicFragment;
import br.com.actia.controller.RadioFragment;
import br.com.actia.controller.SDFragment;
import br.com.actia.controller.UsbFragment;
import br.com.actia.event.DVDStatusEvent;
import br.com.actia.event.PlaylistSDChangedEvent;
import br.com.actia.event.PlaylistUSBChangedEvent;
import br.com.actia.event.SelectConfigEvent;
import br.com.actia.model.CONFIG.AppBottomArea;
import br.com.actia.model.CONFIG.AppTopBar;
import br.com.actia.model.CONFIG.FileStructure;
import br.com.actia.model.EquipmentStatusFrame;
import br.com.actia.model.DVD_S_MODEL.DVDSource;
import br.com.actia.model.UsbMediaFile;
import br.com.actia.service.CanComService;

public class Interface extends Activity implements View.OnClickListener {
    private static final boolean DRIVER = true;
    private static final boolean PASSENGER = false;
    private static final String  DRIVER_TAG = "DRIVER_TAG";
    private static final String  PASSENGER_TAG = "PASSENGER_TAG";
    private static final String TAG = "DUAL ZONE MAIN";
    private ImageButton ibVipUsb;
    private ImageButton ibVipSd;
    private ImageButton ibVipRadio;
    private ImageButton ibVipAux;
    private ImageButton ibVipConfig;
    //GAFR
    private ImageButton ibVipMic;

    private ImageButton ibExecUsb;
    private ImageButton ibExecSd;
    private ImageButton ibExecRadio;
    private ImageButton ibExecAux;
    private ImageButton ibExecConfig;
    //GAFR
    private ImageButton ibExecMic;

    private Intent      intentService = null;
    private Globals     globals = null;

    private UsbMediaFile usbMediaFile;
    private UsbMediaFile sdMediaFile;

    private FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        usbMediaFile = new UsbMediaFile();
        sdMediaFile  = new UsbMediaFile();

        menuInitialize();

        Log.v(TAG, "Starting LoadConfigurations()");
        LoadConfigurations loader = new LoadConfigurations();
        FileStructure fileStructure = loader.getFileStructure();

        if(fileStructure != null) {
            Log.v(TAG, "fileStructure FOUND");
            setConfigurations(fileStructure);
        }
        else {
            Log.v(TAG, "fileStructure == NULL");
        }

        globals = Globals.getInstance(this);
        globals.getEventBus().register(this);

        /*byte bt[] = new byte[8];
        bt[0] = (byte)0x21;
        bt[1] = (byte)0x00;
        bt[2] = (byte)0x00;
        bt[3] = (byte)0x00; //39
        bt[4] = (byte)0x00;
        bt[5] = (byte)0x00;
        bt[6] = (byte)0x00;
        bt[7] = (byte)0x00;
        globals.getEventBus().post(new DVDStatusEvent(bt));*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Enable Bluetooth Automatically
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            Toast.makeText(getApplicationContext(), "Bluetooth switched ON", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Start service
        intentService = new Intent(this, CanComService.class);
        intentService.putExtra("COM_TYPE", DeviceCom.DEVICE_BT_C2BT);
        startService(intentService);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Stop the Communication Service
        stopService(new Intent(this, CanComService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void menuInitialize() {
        ibVipUsb = (ImageButton) findViewById(R.id.vipUsb);
        ibVipSd = (ImageButton) findViewById(R.id.vipSd);
        ibVipRadio = (ImageButton) findViewById(R.id.vipRadio);
        ibVipAux = (ImageButton) findViewById(R.id.vipAux);
        ibVipConfig = (ImageButton) findViewById(R.id.vipConfig);
        //GAFR
        ibVipMic = (ImageButton) findViewById(R.id.vipMic);

        ibVipUsb.setOnClickListener(this);
        ibVipSd.setOnClickListener(this);
        ibVipRadio.setOnClickListener(this);
        ibVipAux.setOnClickListener(this);
        ibVipConfig.setOnClickListener(this);
        //GAFR
        ibVipMic.setOnClickListener(this);

        ibExecUsb = (ImageButton) findViewById(R.id.execUsb);
        ibExecSd = (ImageButton) findViewById(R.id.execSd);
        ibExecRadio = (ImageButton) findViewById(R.id.execRadio);
        ibExecAux = (ImageButton) findViewById(R.id.execAux);
        ibExecConfig = (ImageButton) findViewById(R.id.execConfig);
        //GAFR
        ibExecMic = (ImageButton) findViewById(R.id.execMic);

        ibExecUsb.setOnClickListener(this);
        ibExecSd.setOnClickListener(this);
        ibExecRadio.setOnClickListener(this);
        ibExecAux.setOnClickListener(this);
        ibExecConfig.setOnClickListener(this);
        //GAFR
        ibExecMic.setOnClickListener(this);
    }

    private void setCurrentSources(byte frmDriverSource, byte frmPassSource) {
        Fragment vipFrag = fragmentManager.findFragmentById(R.id.vipTabContent);
        if(vipFrag == null || !(vipFrag instanceof ConfigFragment)) {
            setDriverSource(frmDriverSource);
        }

        Fragment execFrag = fragmentManager.findFragmentById(R.id.execTabContent);
        if(execFrag == null || !(execFrag instanceof ConfigFragment)) {
            setPassengerSource(frmPassSource);
        }
    }

    private void setDriverSource(byte frmDriverSource) {
        //anyone vip btn for clear the correct side menu
        resetMenu(ibVipAux);

        switch (frmDriverSource) {
            case DVDSource.DVD_SOURCE_AUX:
                setFragmentAux(R.id.vipTabContent, DRIVER);
                ibVipAux.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_SDCARD:
                setFragmentSD(R.id.vipTabContent, DRIVER);
                ibVipSd.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_USB:
                setFragmentUSB(R.id.vipTabContent, DRIVER);
                ibVipUsb.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_RADIO:
                setFragmentRadio(R.id.vipTabContent, DRIVER);
                ibVipRadio.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_MIC:
                setFragmentMIC(R.id.vipTabContent, DRIVER);
                ibVipMic.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_CONFIG:
                //setFragmentMIC(R.id.vipTabContent, DRIVER);
                //ibVipConfig.setSelected(true);
                break;
        }
    }

    private void setPassengerSource(byte frmPassSource) {
        //anyone exec btn for clear the correct side menu
        resetMenu(ibExecAux);

        switch (frmPassSource) {
            case DVDSource.DVD_SOURCE_AUX:
                setFragmentAux(R.id.execTabContent, PASSENGER);
                ibExecAux.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_SDCARD:
                setFragmentSD(R.id.execTabContent, PASSENGER);
                ibExecSd.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_USB:
                setFragmentUSB(R.id.execTabContent, PASSENGER);
                ibExecUsb.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_RADIO:
                setFragmentRadio(R.id.execTabContent, PASSENGER);
                ibExecRadio.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_MIC:
                setFragmentMIC(R.id.execTabContent, PASSENGER);
                //GAFR
                ibExecMic.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_CONFIG:
                //setFragmentConfig(R.id.execTabContent, PASSENGER);
                //ibExecConfig.setSelected(true);
                break;
        }
    }

    //Clear all menu selections
    public void resetMenu(ImageButton button) {
        if( button == ibVipSd || button == ibVipUsb || button == ibVipRadio ||
                button == ibVipAux || button == ibVipConfig || button == ibVipMic) {
            ibVipSd.setSelected(false);
            ibVipUsb.setSelected(false);
            ibVipRadio.setSelected(false);
            ibVipAux.setSelected(false);
            ibVipConfig.setSelected(false);
            //GAFR
            ibVipMic.setSelected(false);
        }
        else if( button == ibExecSd || button == ibExecUsb || button == ibExecRadio ||
                button == ibExecAux || button == ibExecConfig || button == ibExecMic ) {
            ibExecSd.setSelected(false);
            ibExecUsb.setSelected(false);
            ibExecRadio.setSelected(false);
            ibExecAux.setSelected(false);
            ibExecConfig.setSelected(false);
            //GAFR
            ibExecMic.setSelected(false);
        }
    }

    @Override
    public void onClick(View view) {
        int R_content;

        ImageButton ib = (ImageButton) view;
        resetMenu(ib);
        ib.setSelected(true);

        //GAFR
        //if(view == ibVipAux || view == ibVipConfig || view == ibVipSd || view == ibVipRadio || view == ibVipUsb) {
        if(view == ibVipAux || view == ibVipConfig || view == ibVipSd || view == ibVipRadio || view == ibVipUsb || view == ibVipMic) {
            R_content = R.id.vipTabContent;
        }
        else {
            R_content = R.id.execTabContent;
        }

        if(view == ibVipUsb || view == ibExecUsb) {
            setFragmentUSB(R_content, (view == ibVipUsb));
        }
        else if(view == ibVipSd || view == ibExecSd) {
            setFragmentSD(R_content, (view == ibVipSd));
        }
        else if(view == ibVipRadio || view == ibExecRadio) {
            setFragmentRadio(R_content, (view == ibVipRadio));
        }
        else if(view == ibVipAux || view == ibExecAux) {
            setFragmentAux(R_content, (view == ibVipAux));
        }
        else if(view == ibVipConfig || view == ibExecConfig) {
            setFragmentConfig(R_content, (view == ibVipConfig));
        }
        //GAFR
        else if(view == ibVipMic || view == ibExecMic) {
            Log.d(TAG, "button MIC pressed...");

            setFragmentMIC(R_content, (view == ibVipMic));
        }
    }

    private void setFragmentUSB(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof UsbFragment)) {
            return;
        }

        Log.d(TAG, "CHANGE USB fragment | IsDriver : " + isDrive);

        UsbFragment usbFragment = new UsbFragment();
        usbFragment.setIsDriver(isDrive);
        usbFragment.sendChangeMode(globals);
        usbFragment.setUsbMediaFile(this.usbMediaFile);

        changeFragment(R_fragmentContainer, usbFragment, isDrive);
    }

    private void setFragmentSD(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof SDFragment)) {
            return;
        }

        Log.d(TAG, "CHANGE SD fragment | IsDriver : " + isDrive);

        SDFragment sdFragment = new SDFragment();
        sdFragment.setIsDriver(isDrive);
        sdFragment.sendChangeMode(globals);
        sdFragment.setSdMediaFile(this.sdMediaFile);

        changeFragment(R_fragmentContainer, sdFragment, isDrive);
    }

    private void setFragmentRadio(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof RadioFragment)) {
            return;
        }

        Log.d(TAG, "CHANGE RADIO fragment | IsDriver : " + isDrive);

        RadioFragment radioFragment = new RadioFragment();
        radioFragment.setIsDriver(isDrive);
        radioFragment.sendChangeMode(globals);

        changeFragment(R_fragmentContainer, radioFragment, isDrive);
    }

    private void setFragmentAux(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof AuxFragment)) {
            return;
        }

        Log.d(TAG, "CHANGE AUX fragment | IsDriver : " + isDrive);

        AuxFragment auxFragment = new AuxFragment();
        auxFragment.setIsDriver(isDrive);
        auxFragment.sendChangeMode(globals);

        changeFragment(R_fragmentContainer, auxFragment, isDrive);
    }

    private void setFragmentConfig(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof ConfigFragment)) {
            return;
        }

        Log.d(TAG, "CHANGE CONFIG fragment | IsDriver : " + isDrive);

        ConfigFragment configFragment = new ConfigFragment();
        configFragment.setIsDriver(isDrive);
        configFragment.sendChangeMode(globals);

        changeFragment(R_fragmentContainer, configFragment, isDrive);
    }

    private void setFragmentMIC(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof MicFragment)) {
            return;
        }

        Log.d(TAG, "CHANGE MIC fragment | IsDriver : " + isDrive);

        MicFragment micFragment = new MicFragment();
        micFragment.setIsDriver(isDrive);
        micFragment.sendChangeMode(globals);

        changeFragment(R_fragmentContainer, micFragment, isDrive);
    }


    /**
     * Change the current fragment (Destroy it) for a new fragment
     * @param R_fragmentContainer - Fragment Container ID
     * @param newFragment - New fragment instance
     * @param isDriver - boolean indicating if is driver or not
     */
    private void changeFragment(int R_fragmentContainer, Fragment newFragment, boolean isDriver) {
        Fragment currentFragment;
        String tag;

        tag = isDriver ? DRIVER_TAG : PASSENGER_TAG;

        currentFragment = fragmentManager.findFragmentByTag(tag);

        if(currentFragment != null) {
            FragmentTransaction trans = fragmentManager.beginTransaction();
            trans.remove(currentFragment).commit();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R_fragmentContainer, newFragment, tag);
        fragmentTransaction.commit();
    }

    //##############################################################################################
    //EVENTS
    //##############################################################################################
    public void onEventMainThread(final DVDStatusEvent event) {
        EquipmentStatusFrame equipmentStatusFrame = event.getEquipmentStatusFrame();

        byte frmDriverSource = equipmentStatusFrame.getDvdSourceDriver().getValue();
        byte frmPassSource = equipmentStatusFrame.getDvdSourcePassenger().getValue();

        setCurrentSources(frmDriverSource, frmPassSource);
    }

    public void onEventMainThread(final SelectConfigEvent event) {
        if(event.isDriver()) {
            setDriverSource(DVDSource.DVD_SOURCE_CONFIG);
        }
        else {
            setPassengerSource(DVDSource.DVD_SOURCE_CONFIG);
        }
    }

    /**
     * EventBus callback
     * @param playlistUSBChangedEvent
     */
    public void onEventMainThread(final PlaylistUSBChangedEvent playlistUSBChangedEvent) {
        UsbMediaFile usbMediaFile = playlistUSBChangedEvent.getUsbMediaFile();

        if(! usbMediaFile.getPlaylistPath().equalsIgnoreCase(this.usbMediaFile.getPlaylistPath())) {
            this.usbMediaFile = usbMediaFile;
        }
    }

    public void onEventMainThread(final PlaylistSDChangedEvent playlistSDChangedEvent) {
        UsbMediaFile sdMediaFile = playlistSDChangedEvent.getSdMediaFile();

        if(! sdMediaFile.getPlaylistPath().equalsIgnoreCase(this.sdMediaFile.getPlaylistPath())) {
            this.sdMediaFile = sdMediaFile;
        }
    }

    //##############################################################################################
    //CONFIGURATIONS
    //##############################################################################################
    private void setConfigurations(FileStructure fileStructure) {
        Log.v(TAG, "###" + fileStructure.toString());

        try {
            //Set TOP BAR CONFIGURATIONS
            AppTopBar topBar = fileStructure.getTopBar();

            if (topBar != null) {
                TextView tvArea1 = (TextView) findViewById(R.id.area1Text);
                TextView tvArea2 = (TextView) findViewById(R.id.area2Text);
                LinearLayout area1 = (LinearLayout) findViewById(R.id.area1Header);
                LinearLayout area2 = (LinearLayout) findViewById(R.id.area2Header);
                LinearLayout brandArea = (LinearLayout) findViewById(R.id.topBrandArea);
                View separator = (View) findViewById(R.id.separator);

                //Remove all default views
                brandArea.removeAllViews();

                //TopBar background color
                brandArea.setBackgroundColor(Color.parseColor(topBar.getBgColor()));
                tvArea1.setBackgroundColor(Color.parseColor(topBar.getBgColor()));
                tvArea2.setBackgroundColor(Color.parseColor(topBar.getBgColor()));
                separator.setBackgroundColor(Color.parseColor(topBar.getBgColor()));

                tvArea1.setText(topBar.getArea1Text());
                tvArea2.setText(topBar.getArea2Text());

                List<String> logoList = topBar.getLogoList();

                for (int i = 0; i < logoList.size(); i++) {
                    ImageView imageView = new ImageView(getApplicationContext());
                    Bitmap bmp = BitmapFactory.decodeFile(logoList.get(i));

                    if(bmp != null)
                        imageView.setImageBitmap(bmp);

                    brandArea.addView(imageView);
                }
            }

            //Set Buttons configurations
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ibVipSd.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibVipUsb.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibVipRadio.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibVipAux.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibVipConfig.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                //GAFR
                ibVipMic.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));

                ibExecSd.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibExecSd.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibExecRadio.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibExecAux.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibExecConfig.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                //GAFR
                ibExecMic.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));

            }

            //Set bottom Bar configurations
            AppBottomArea bottomBar = fileStructure.getBottomArea();

            if(bottomBar != null) {
                LinearLayout poweredArea = (LinearLayout) findViewById(R.id.bottomPoweredBy);

                //Remove all default views
                poweredArea.removeAllViews();

                List<String> logoList = bottomBar.getLogoList();
                for(int i = 0; i < logoList.size(); i++) {
                    ImageView imageView = new ImageView(getApplicationContext());
                    Bitmap bmp = BitmapFactory.decodeFile(logoList.get(i));

                    if(bmp != null)
                        imageView.setImageBitmap(bmp);

                    poweredArea.addView(imageView);
                }
            }

            //Set Controllers colors

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public StateListDrawable makeSelector(int pressedColor, int selectedColor, int normalColor) {
        StateListDrawable res = new StateListDrawable();

        res.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressedColor));
        res.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(selectedColor));
        res.addState(new int[]{}, new ColorDrawable(normalColor));
        return res;
    }
}
