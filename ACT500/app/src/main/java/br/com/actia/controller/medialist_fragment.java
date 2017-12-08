package br.com.actia.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.com.actia.Globals;
import br.com.actia.communication.CanMSG;
import br.com.actia.dualzoneinterface.R;
import br.com.actia.event.DVDStatusEvent;
import br.com.actia.event.DVDTrackStatusEvent;
import br.com.actia.event.PlaylistChangedEvent;
import br.com.actia.model.DVDStatusFrame;
import br.com.actia.model.DVDTrackStatusFrame;
import br.com.actia.model.DVD_S_MODEL.DVDSource;
import br.com.actia.model.PLAYLIST.MediaFile;
import br.com.actia.model.UsbMediaFile;

/**
 * Created by Armani andersonaramni@gmail.com on 17/02/17.
 */

public class medialist_fragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "medialist_fragment";
    public static final String PLAYLIST_NAME = "PLAYLIST_NAME";
    private List<MediaFile>    playlist_list;
    private ArrayAdapter<String> arrayAdapter;
    private ListView        playlistView;
    private ImageButton     ibReturn;
    private String          playlistName;
    private Globals         globals = null;
    private UsbMediaFile    usbMediaFile;

    private ImageButton     btnBack;
    private ImageButton     btnNext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view       = inflater.inflate(R.layout.media_list_view, container, false);
        playlistView    = (ListView) view.findViewById(R.id.list_medias);
        ibReturn        = (ImageButton) view.findViewById(R.id.btnReturn);

        ibReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });

        playlistName = getArguments().getString(PLAYLIST_NAME);
        Log.d(TAG, playlistName);

        usbMediaFile = new UsbMediaFile();
        usbMediaFile.setPlaylistPath(playlistName);

        globals = Globals.getInstance(getActivity().getApplication().getApplicationContext());
        globals.getEventBus().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPlaylistView();

        btnBack = (ImageButton) getActivity().findViewById(R.id.btnUSBReward);
        btnNext = (ImageButton) getActivity().findViewById(R.id.btnUSBForward);

        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long time) {
        MediaFile mediaFile = playlist_list.get(position);

        //Send playlist changed Event
        usbMediaFile.setFileNumber(mediaFile.getId());
        usbMediaFile.setFileName(mediaFile.getName());
        selectFileNumber(usbMediaFile);

        Toast.makeText(getActivity(), "ID: " + mediaFile.getId(), Toast.LENGTH_SHORT).show();
        globals.getEventBus().post(new PlaylistChangedEvent(usbMediaFile));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        globals.getEventBus().unregister(this);
    }

    private void setPlaylistView() {
        List<String> strList = new LinkedList<>();

        PlaylistHandler playlistHandler = new PlaylistHandler("");
        try {
            playlist_list = playlistHandler.getMedias(playlistName);

            for (MediaFile mediaFile: playlist_list) {
                strList.add(mediaFile.getName());
            }
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.media_error, Toast.LENGTH_LONG).show();
        }

        arrayAdapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(),
                R.layout.list_item, strList);

        playlistView.setAdapter(arrayAdapter);

        playlistView.setOnItemClickListener(this);
    }

    /**
     * Send data to change file number
     * @param fileNumber
     */
    private void setMediaFile(int fileNumber) {
        CanMSG canMSG = new CanMSG();
        canMSG.setId(CanMSG.MSGID_EQUIPAMENT_CTRL);
        canMSG.setLength((byte) 8);
        canMSG.setType(CanMSG.MSGTYPE_EXTENDED);

        String dataValue = String.format("0000%02xffffffffff", fileNumber);
        canMSG.setData(dataValue);

        globals.sendCanCMDEvent(canMSG);

        //Data sent
        Log.v(TAG, dataValue);
    }

    /**
     * Set the current file
     * @param usbMediaFile
     */
    private void selectFileNumber(UsbMediaFile usbMediaFile) {
        for(MediaFile mediaFile: playlist_list) {
            if (mediaFile.getName() == usbMediaFile.getFileName()){
                int index = playlist_list.indexOf(mediaFile);
                playlistView.setSelection(index);
                arrayAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * EventBus callback
     * @param playlistChangedEvent
     */
    public void onEventMainThread(final PlaylistChangedEvent playlistChangedEvent) {
        UsbMediaFile usbMediaFile = playlistChangedEvent.getUsbMediaFile();

        if(!usbMediaFile.getPlaylistPath().equalsIgnoreCase(this.usbMediaFile.getPlaylistPath())) {
            playlistName = usbMediaFile.getPlaylistPath();
            //Reload playlistView
            setPlaylistView();
        }

        if(usbMediaFile.getFileNumber() != this.usbMediaFile.getFileNumber()) {
            this.usbMediaFile = usbMediaFile;
            selectFileNumber(usbMediaFile);
        }
    }

    /**
     * EventBus callback
     * @param event
     */
    public void onEventMainThread(final DVDTrackStatusEvent event) {
        DVDTrackStatusFrame dvdTrackStatusFrame = event.getDvdTrackStatusFrame();
        int dvdTrackFileNumber = dvdTrackStatusFrame.getDvdFileNumber().getValue();

        //if there are no items in the list
        if(playlist_list == null || playlist_list.size() == 0)
            return;

        //if Music changed
        int lastFile = this.usbMediaFile.getFileNumber();
        if(lastFile != dvdTrackFileNumber) {
            for(MediaFile mediaFile: playlist_list) {
                if (mediaFile.getId() == lastFile){
                    int index = playlist_list.indexOf(mediaFile);

                    index = ++index >= playlist_list.size() ? 0 : index;
                    MediaFile nextMediaFile = playlist_list.get(index);

                    if(nextMediaFile.getId() != dvdTrackFileNumber) {
                        this.usbMediaFile.setFileName(nextMediaFile.getName());
                        this.usbMediaFile.setFileNumber(nextMediaFile.getId());
                        lastFile = nextMediaFile.getId();

                        setMediaFile(lastFile);
                        selectFileNumber(usbMediaFile);
                    }

                    break;
                }
            }
        }
    }

    /**
     * Next and previous buttons treatment
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(view == btnNext) {
            int position = arrayAdapter.getPosition(usbMediaFile.getFileName());
            position = ++position >= playlist_list.size() ? 0 : position;

            MediaFile mediaFile = playlist_list.get(position);

            this.usbMediaFile.setFileNumber(mediaFile.getId());
            this.usbMediaFile.setFileName(mediaFile.getName());

            //enviar dados pela CAN
            setMediaFile(mediaFile.getId());

            playlistView.setSelection(position);
            arrayAdapter.notifyDataSetChanged();
        } else if (view == btnBack) {
            int position = arrayAdapter.getPosition(usbMediaFile.getFileName());
            position = --position < 0 ? (playlist_list.size() -1) : position;

            MediaFile mediaFile = playlist_list.get(position);

            this.usbMediaFile.setFileNumber(mediaFile.getId());
            this.usbMediaFile.setFileName(mediaFile.getName());

            //enviar dados pela CAN
            setMediaFile(mediaFile.getId());

            playlistView.setSelection(position);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
