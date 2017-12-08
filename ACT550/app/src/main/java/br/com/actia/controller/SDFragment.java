package br.com.actia.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.actia.Globals;
import br.com.actia.communication.CanMSG;
import br.com.actia.dualzoneinterface.R;
import br.com.actia.event.DVDStatusEvent;
import br.com.actia.event.InactiveMediaPlayerStatusEvent;
import br.com.actia.event.MediaPlayerStatusEvent;
import br.com.actia.event.PlaylistSDChangedEvent;
import br.com.actia.model.DVD_S_MODEL.DVDSource;
import br.com.actia.model.DVD_TS_MODEL.DVDStatus;
import br.com.actia.model.EquipmentStatusFrame;
import br.com.actia.model.MediaPlayerStatusFrame;
import br.com.actia.model.PLAYLIST.MediaFile;
import br.com.actia.model.UsbMediaFile;

/**
 * Created by Armani andersonaramni@gmail.com on 20/02/17.
 */

public class SDFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "SD FRAGMENT";
    private View view;
    private ImageButton btnBack;
    private ImageButton btnNext;
    private ImageButton btnPlayPause;
    private TextView txtUsbFile;
    private TextView txtUsbTime;

    private ImageButton btnReturn;
    private TextView    txtListTitle;
    private ListView listViewMedia;

    private boolean isDriver;
    private boolean sdON = false;
    private boolean isCurrentDriveZone = false;

    private DVDStatus sdStatus = null;

    private UsbMediaFile sdMediaFile;

    private Globals      globals = null;
    private List<String> currentList;
    private List<MediaFile> mediaList = null;
    private ArrayAdapter<String> arrayAdapter;
    private boolean      isMediaList = false;

    public SDFragment() {
        sdMediaFile = new UsbMediaFile();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        if(view == null) {
            view = inflater.inflate(R.layout.activity_sd, container, false);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");

        btnBack = (ImageButton) view.findViewById(R.id.btnUSBReward);
        btnNext = (ImageButton) view.findViewById(R.id.btnUSBForward);
        btnPlayPause = (ImageButton) view.findViewById(R.id.btnUSBPlayPause);
        txtUsbFile = (TextView) view.findViewById(R.id.txtUSBFile);
        txtUsbTime = (TextView) view.findViewById(R.id.txtUSBTime);

        //PLAYLIST
        btnReturn       = (ImageButton) view.findViewById(R.id.btnListReturn);
        txtListTitle    = (TextView) view.findViewById(R.id.playlist_title);
        listViewMedia   = (ListView) view.findViewById(R.id.list_medias);

        sdStatus = new DVDStatus(DVDStatus.DVD_STATUS_STOPPED);

        initializeActions();

        //Initialize listview
        currentList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(),
                R.layout.list_item, currentList);

        listViewMedia.setAdapter(arrayAdapter);
        listViewMedia.setOnItemClickListener(this);

        globals = Globals.getInstance(getActivity().getApplication().getApplicationContext());
        globals.getEventBus().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        loadPlaylist();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        globals.getEventBus().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void loadPlaylist() {
        if(sdMediaFile.getPlaylistPath().isEmpty()) {
            //Hide return button
            btnReturn.setVisibility(View.INVISIBLE);
            txtListTitle.setText(getString(R.string.media_playlist));
            isMediaList = false;

            PlaylistHandler playlistHandler = new PlaylistHandler("");
            List<String> playlist_list = playlistHandler.getPlaylists(PlaylistHandler.PLAYLIST_USB,
                    PlaylistHandler.FOLDER_FILTER_SD);

            if(playlist_list == null || playlist_list.size() == 0){
                return;
            }

            if(playlist_list.size() == 1) {
                sdMediaFile.setPlaylistPath(playlist_list.get(0));
                loadMediaList(sdMediaFile);
            }
            else {
                arrayAdapter.clear();
                arrayAdapter.addAll(playlist_list);
                arrayAdapter.notifyDataSetChanged();
            }
        }
        else {
            loadMediaList(sdMediaFile);
        }
    }
    private void loadMediaList(UsbMediaFile usbMediaFile) {
        //Show return button
        btnReturn.setVisibility(View.VISIBLE);
        txtListTitle.setText(getString(R.string.media_list));
        isMediaList = true;

        arrayAdapter.clear();

        PlaylistHandler playlistHandler = new PlaylistHandler("");
        try {
            if(mediaList != null)
                mediaList.clear();

            mediaList = playlistHandler.getMedias(usbMediaFile.getPlaylistPath());

            for (MediaFile mediaFile: mediaList) {
                arrayAdapter.add(mediaFile.getName());
            }
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.media_error, Toast.LENGTH_LONG).show();
        }

        arrayAdapter.notifyDataSetChanged();
    }

    private void initializeActions() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_BACK);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_NEXT);
            }
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sdStatus.getValue()) {
                    case DVDStatus.DVD_STATUS_PLAYING:
                        globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_PAUSE);
                        break;
                    case DVDStatus.DVD_STATUS_PAUSED:
                    case DVDStatus.DVD_STATUS_STOPPED:
                        globals.sendCanCtrlCMDEvent(CanMSG.CAN_CMD_PLAY);
                        break;
                }
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sdMediaFile = new UsbMediaFile();
                globals.getEventBus().post(new PlaylistSDChangedEvent(sdMediaFile));
                loadPlaylist();
            }
        });
    }

    /**
     * Notify when user choose a source
     */
    public void sendChangeMode(Globals globals) {
        CanMSG canMSG = new CanMSG();
        canMSG.setId(CanMSG.MSGID_EQUIPAMENT_CTRL);
        canMSG.setLength((byte) 8);
        canMSG.setType(CanMSG.MSGTYPE_EXTENDED);

        if(isDriver)
            canMSG.setData("001F00ffffffffff");
        else
            canMSG.setData("00F100ffffffffff");

        globals.sendCanCMDEvent(canMSG);
    }

    private void changePlayPauseIcon(boolean isPlaying) {
        if(isPlaying) {
            btnPlayPause.setImageResource(R.drawable.ic_pause_white_36dp);
        }
        else {
            btnPlayPause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
        }
    }

    public void setIsDriver(boolean val) {
        isDriver = val;
    }

    public void setSdMediaFile(UsbMediaFile sdMediaFile) {
        this.sdMediaFile = sdMediaFile;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //MediaFile mediaFile = currentList.get(position);
        if(isMediaList) {
            MediaFile mediaFile =  mediaList.get(position);

            //Send playlist changed Event
            sdMediaFile.setFileNumber(mediaFile.getId());
            sdMediaFile.setFileName(mediaFile.getName());

            Toast.makeText(getActivity(), "ID: " + mediaFile.getId(), Toast.LENGTH_SHORT).show();
            globals.getEventBus().post(new PlaylistSDChangedEvent(sdMediaFile));
        }
        else {
            UsbMediaFile mediaFile = new UsbMediaFile();
            mediaFile.setPlaylistPath(currentList.get(position));
            globals.getEventBus().post(new PlaylistSDChangedEvent(mediaFile));
            //loadMediaList(mediaFile);
        }
    }

    void handleMediaPlayerData(MediaPlayerStatusFrame mediaPlayerStatusFrame) {
        if (sdON) {
            int dvdNumber = mediaPlayerStatusFrame.getDvdFileNumber().getValue();
            txtUsbFile.setText(String.format("%02d", dvdNumber));
            txtUsbTime.setText(mediaPlayerStatusFrame.getDvdTime().toString());

            changePlayPauseIcon(mediaPlayerStatusFrame.getDvdStatus().isPlaying());
        } else {
            txtUsbFile.setText("00");
            txtUsbTime.setText("00:00:00");
        }
    }
    //##############################################################################################
    // EVENTS
    //##############################################################################################
    public void onEventMainThread(final MediaPlayerStatusEvent event) {
        MediaPlayerStatusFrame mediaPlayerStatusFrame = event.getMediaPlayerStatusFrame();

        //Same zones uses MediaPlayer frame (Guillermo Coralles implementation on ACT55X protocol)
        if(isDriver == isCurrentDriveZone) {
            handleMediaPlayerData(mediaPlayerStatusFrame);
        }
    }

    public void onEventMainThread(final InactiveMediaPlayerStatusEvent event) {
        MediaPlayerStatusFrame mediaPlayerStatusFrame = event.getMediaPlayerStatusFrame();

        //Different zones uses InactiveMediaPlayer frame (Guillermo Coralles implementation on ACT55X protocol)
        if(isDriver == !isCurrentDriveZone) {
            handleMediaPlayerData(mediaPlayerStatusFrame);
        }
    }

    public void onEventMainThread(final DVDStatusEvent event) {
        EquipmentStatusFrame equipmentStatusFrame = event.getEquipmentStatusFrame();
        isCurrentDriveZone = equipmentStatusFrame.getZone() == EquipmentStatusFrame.ZONE_DRIVER;

        if(isDriver) {
            sdON = equipmentStatusFrame.getDvdSourceDriver().getValue() == DVDSource.DVD_SOURCE_SDCARD;
        }
        else {
            sdON = equipmentStatusFrame.getDvdSourcePassenger().getValue() == DVDSource.DVD_SOURCE_SDCARD;
        }
    }

    /**
     * EventBus callback
     * @param playlistSDChangedEvent
     */
    public void onEventMainThread(final PlaylistSDChangedEvent playlistSDChangedEvent) {
        UsbMediaFile usbMediaFile = playlistSDChangedEvent.getSdMediaFile();

        if(! usbMediaFile.getPlaylistPath().equalsIgnoreCase(this.sdMediaFile.getPlaylistPath())) {
            this.sdMediaFile = usbMediaFile;

            if(usbMediaFile.getPlaylistPath().isEmpty()) {
                loadPlaylist();
            }
            else {
                loadMediaList(usbMediaFile);
            }
        }
    }

    //##############################################################################################
    // CONFIGURATIONS
    //##############################################################################################
    /*public void configView(AppWorkArea appWorkArea, AppMenuBar appMenuBar) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btnBack.setBackground(mainActivity.makeSelector(Color.parseColor(appMenuBar.getBtnPressed()),
                    Color.parseColor(appMenuBar.getBtnSelected()), Color.parseColor(appMenuBar.getBtnNormal())));

            btnNext.setBackground(mainActivity.makeSelector(Color.parseColor(appMenuBar.getBtnPressed()),
                    Color.parseColor(appMenuBar.getBtnSelected()), Color.parseColor(appMenuBar.getBtnNormal())));

            btnPlayPause.setBackground(mainActivity.makeSelector(Color.parseColor(appMenuBar.getBtnPressed()),
                    Color.parseColor(appMenuBar.getBtnSelected()), Color.parseColor(appMenuBar.getBtnNormal())));
        }*/

}
