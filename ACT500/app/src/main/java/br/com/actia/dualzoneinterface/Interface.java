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
import android.widget.Button;
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
import br.com.actia.controller.DvdFragment;
import br.com.actia.controller.LoadConfigurations;
import br.com.actia.controller.MicFragment;
import br.com.actia.controller.RadioFragment;
import br.com.actia.controller.UsbFragment;
import br.com.actia.event.DVDStatusEvent;
import br.com.actia.event.SelectConfigEvent;
import br.com.actia.model.CONFIG.AppBottomArea;
import br.com.actia.model.CONFIG.AppTopBar;
import br.com.actia.model.CONFIG.FileStructure;
import br.com.actia.model.DVDStatusFrame;
import br.com.actia.model.DVD_S_MODEL.DVDSource;
import br.com.actia.service.CanComService;

public class Interface extends Activity implements View.OnClickListener {
    private static final String ACTION_USB_ATTACHED  = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String ACTION_USB_DETACHED  = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    private static final String usbStateChangeAction = "android.hardware.usb.action.USB_STATE";
    private static final boolean DRIVER = true;
    private static final boolean PASSENGER = false;
    private static final String TAG = "DUAL ZONE MAIN";
    private static final long CONFIG_TIME = 5000;
    private ImageButton ibVipDvd;
    private ImageButton ibVipUsb;
    private ImageButton ibVipRadio;
    private ImageButton ibVipAux;
    private ImageButton ibVipConfig;
    private ImageButton ibExecDvd;
    private ImageButton ibExecSd;
    private ImageButton ibExecRadio;
    private ImageButton ibExecAux;
    private ImageButton ibExecConfig;

    private Intent intentService = null;
    private Globals globals = null;

    private byte driverSource = DVDSource.DVD_SOURCE_AUX;
    private byte passengerSource = DVDSource.DVD_SOURCE_AUX;

    private FragmentManager fragmentManager = getFragmentManager();

    private UsbFragment     driverUsbFragment = null;
    private UsbFragment     passengerUsbFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

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

        driverUsbFragment    = new UsbFragment();
        driverUsbFragment.setIsDriver(true);
        passengerUsbFragment = new UsbFragment();
        passengerUsbFragment.setIsDriver(false);

        globals = Globals.getInstance(this);
        globals.getEventBus().register(this);
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

        //Start the Communication Service
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
        ibVipDvd = (ImageButton) findViewById(R.id.vipDvd);
        ibVipUsb = (ImageButton) findViewById(R.id.vipSd);
        ibVipRadio = (ImageButton) findViewById(R.id.vipRadio);
        ibVipAux = (ImageButton) findViewById(R.id.vipAux);
        ibVipConfig = (ImageButton) findViewById(R.id.vipConfig);

        ibVipDvd.setOnClickListener(this);
        ibVipUsb.setOnClickListener(this);
        ibVipRadio.setOnClickListener(this);
        ibVipAux.setOnClickListener(this);
        ibVipConfig.setOnClickListener(this);

        ibExecDvd = (ImageButton) findViewById(R.id.execDvd);
        ibExecSd = (ImageButton) findViewById(R.id.execSd);
        ibExecRadio = (ImageButton) findViewById(R.id.execRadio);
        ibExecAux = (ImageButton) findViewById(R.id.execAux);
        ibExecConfig = (ImageButton) findViewById(R.id.execConfig);

        ibExecDvd.setOnClickListener(this);
        ibExecSd.setOnClickListener(this);
        ibExecRadio.setOnClickListener(this);
        ibExecAux.setOnClickListener(this);
        ibExecConfig.setOnClickListener(this);
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
                setFragmentAux(R.id.vipTabContent, true);
                ibVipAux.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_CD:
                setFragmentDVD(R.id.vipTabContent, true);
                ibVipDvd.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_USB:
                setFragmentUSB(R.id.vipTabContent, true);
                ibVipUsb.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_RADIO:
                setFragmentRadio(R.id.vipTabContent, true);
                ibVipRadio.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_MIC:
                setFragmentMIC(R.id.vipTabContent, true);
                break;
            case DVDSource.DVD_SOURCE_CONFIG:
                setFragmentMIC(R.id.vipTabContent, true);
                ibVipConfig.setSelected(true);
                break;
        }
    }

    private void setPassengerSource(byte frmPassSource) {
        //anyone exec btn for clear the correct side menu
        resetMenu(ibExecAux);

        switch (frmPassSource) {
            case DVDSource.DVD_SOURCE_AUX:
                setFragmentAux(R.id.execTabContent, false);
                ibExecAux.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_CD:
                setFragmentDVD(R.id.execTabContent, false);
                ibExecDvd.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_USB:
                setFragmentUSB(R.id.execTabContent, false);
                ibExecSd.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_RADIO:
                setFragmentRadio(R.id.execTabContent, false);
                ibExecRadio.setSelected(true);
                break;
            case DVDSource.DVD_SOURCE_MIC:
                setFragmentMIC(R.id.execTabContent, false);
                break;
            case DVDSource.DVD_SOURCE_CONFIG:
                setFragmentConfig(R.id.execTabContent, false);
                ibExecConfig.setSelected(true);
                break;
        }
    }

    //Clear all menu selections
    public void resetMenu(ImageButton button) {
        if( button == ibVipDvd || button == ibVipUsb || button == ibVipRadio ||
                button == ibVipAux || button == ibVipConfig ) {
            ibVipDvd.setSelected(false);
            ibVipUsb.setSelected(false);
            ibVipRadio.setSelected(false);
            ibVipAux.setSelected(false);
            ibVipConfig.setSelected(false);
        }
        else if( button == ibExecSd || button == ibExecDvd || button == ibExecRadio ||
                button == ibExecAux || button == ibExecConfig ) {
            ibExecSd.setSelected(false);
            ibExecDvd.setSelected(false);
            ibExecRadio.setSelected(false);
            ibExecAux.setSelected(false);
            ibExecConfig.setSelected(false);
        }
    }

    public void onEventMainThread(final DVDStatusEvent event) {
        DVDStatusFrame dvdStatusFrame = event.getDvdStatusFrame();

        byte frmDriverSource = dvdStatusFrame.getDvdSourceDriver().getValue();
        byte frmPassSource = dvdStatusFrame.getDvdSourcePassenger().getValue();

        setCurrentSources(frmDriverSource, frmPassSource);
    }

    @Override
    public void onClick(View view) {
        int R_content;

        ImageButton ib = (ImageButton) view;
        resetMenu(ib);
        view.setSelected(true);

        if(view == ibVipAux || view == ibVipConfig || view == ibVipDvd || view == ibVipRadio || view == ibVipUsb) {
            R_content = R.id.vipTabContent;
        }
        else {
            R_content = R.id.execTabContent;
        }

        if(view == ibVipUsb || view == ibExecSd) {
            setFragmentUSB(R_content, (view == ibVipUsb));
        }
        else if(view == ibVipDvd || view == ibExecDvd) {
            setFragmentDVD(R_content, (view == ibVipDvd));
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
    }

    private void setFragmentUSB(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof UsbFragment)) {
            return;
        }

        UsbFragment usbFragment;

        if(isDrive) {
            usbFragment = driverUsbFragment;
        }
        else {
            usbFragment = passengerUsbFragment;
        }
        usbFragment.sendChangeMode();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R_fragmentContainer, usbFragment);
        fragmentTransaction.commit();
    }

    private void setFragmentDVD(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof DvdFragment)) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DvdFragment dvdFragment = new DvdFragment();
        dvdFragment.setIsDriver(isDrive);
        dvdFragment.sendChangeMode();

        fragmentTransaction.replace(R_fragmentContainer, dvdFragment);
        fragmentTransaction.commit();
    }

    private void setFragmentRadio(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof RadioFragment)) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RadioFragment radioFragment = new RadioFragment();
        radioFragment.setIsDriver(isDrive);
        radioFragment.sendChangeMode();

        fragmentTransaction.replace(R_fragmentContainer, radioFragment);
        fragmentTransaction.commit();
    }

    private void setFragmentAux(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof AuxFragment)) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AuxFragment auxFragment = new AuxFragment();
        auxFragment.setIsDriver(isDrive);
        auxFragment.sendChangeMode();

        fragmentTransaction.replace(R_fragmentContainer, auxFragment);
        fragmentTransaction.commit();
    }

    private void setFragmentConfig(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof ConfigFragment)) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ConfigFragment configFragment = new ConfigFragment();
        configFragment.setIsDriver(isDrive);
        configFragment.sendChangeMode();

        fragmentTransaction.replace(R_fragmentContainer, configFragment);
        fragmentTransaction.commit();
    }

    private void setFragmentMIC(int R_fragmentContainer, boolean isDrive) {
        Fragment fragment = fragmentManager.findFragmentById(R_fragmentContainer);
        if(fragment != null && (fragment instanceof MicFragment)) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MicFragment micFragment = new MicFragment();
        micFragment.setIsDriver(isDrive);

        fragmentTransaction.replace(R_fragmentContainer, micFragment);
        fragmentTransaction.commit();
    }

    //################################################################################################################
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
                ibVipDvd.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibVipUsb.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibVipRadio.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibVipAux.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibVipConfig.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));

                ibExecDvd.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibExecSd.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibExecRadio.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibExecAux.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
                        Color.parseColor(fileStructure.getMenuBar().getBtnSelected()), Color.parseColor(fileStructure.getMenuBar().getBtnNormal())));
                ibExecConfig.setBackground(makeSelector(Color.parseColor(fileStructure.getMenuBar().getBtnPressed()),
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

        }catch (Exception e) {
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
