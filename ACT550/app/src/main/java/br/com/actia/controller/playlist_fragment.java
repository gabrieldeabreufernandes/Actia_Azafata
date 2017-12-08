package br.com.actia.controller;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.actia.Globals;
import br.com.actia.dualzoneinterface.R;
import br.com.actia.event.PlaylistUSBChangedEvent;
import br.com.actia.model.UsbMediaFile;

/**
 * Created by Armani andersonaramni@gmail.com on 17/02/17.
 */

public class playlist_fragment extends Fragment implements AdapterView.OnItemClickListener {
    private List<String> playlist_list;
    private ArrayAdapter<String> arrayAdapter;
    private ListView playlistView;
    private Globals      globals;
    private View view = null;
    private UsbMediaFile usbMediaFile = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(usbMediaFile == null) {
            usbMediaFile = new UsbMediaFile();
        }

        if(view == null) {
            view = inflater.inflate(R.layout.playlist_view, container, false);
        }
        playlistView = (ListView) view.findViewById(R.id.list_view_id);

        globals = Globals.getInstance(getActivity().getApplicationContext());
        globals.getEventBus().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Get Playlists list - List all .json files into the folder
        PlaylistHandler playlistHandler = new PlaylistHandler("");
        playlist_list = playlistHandler.getPlaylists(PlaylistHandler.PLAYLIST_USB, PlaylistHandler.FOLDER_FILTER);

        if(playlist_list.size() == 1) {
            callMediaFragment(0);
        }

        arrayAdapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(),
                R.layout.list_item, playlist_list);

        if(playlist_list != null) {
            playlistView.setAdapter(arrayAdapter);
            playlistView.setOnItemClickListener(this);
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), R.string.media_notfound, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long time) {
        callMediaFragment(position);
    }

    private void callMediaFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(medialist_fragment.PLAYLIST_NAME, playlist_list.get(position));

        //Send playlist changed Event
        usbMediaFile.setPlaylistPath(playlist_list.get(position));
        usbMediaFile.setFileNumber(0);

        //Start new fragment
        startMediaFragment(bundle);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                globals.getEventBus().post(new PlaylistUSBChangedEvent(usbMediaFile));
            }
        }, 100);
    }

    private void startMediaFragment(Bundle bundle) {
        Fragment newFragment = new medialist_fragment();
        newFragment.setArguments(bundle);

        int fragmentId = getId();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(fragmentId, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        globals.getEventBus().unregister(this);
    }

    public void setCurrentPlaylist(UsbMediaFile usbMediaFile) {
        this.usbMediaFile = usbMediaFile;

        //to avoid to open a lot of fragments
        /*Fragment fragment = getFragmentManager().findFragmentById(R.id.list_fragmentUSB_id);
        if(fragment != null && (fragment instanceof medialist_fragment)) {
            return;
        }
        else {
            //to avoid to open a lot of fragments
            fragment = getFragmentManager().findFragmentById(R.id.list_fragmentSD_id);
            if (fragment != null && (fragment instanceof medialist_fragment)) {
                return;
            }
        }

        int index = playlist_list.indexOf(usbMediaFile.getPlaylistPath());

        Bundle bundle = new Bundle();
        bundle.putString(medialist_fragment.PLAYLIST_NAME, playlist_list.get(index));

        //Start new fragment
        startMediaFragment(bundle);*/
    }

    /**
     * EventBus callback
     * @param playlistUSBChangedEvent
     */
    public void onEventMainThread(final PlaylistUSBChangedEvent playlistUSBChangedEvent) {
        UsbMediaFile usbMediaFile = playlistUSBChangedEvent.getUsbMediaFile();

        setCurrentPlaylist(usbMediaFile);
        /*if(! usbMediaFile.getPlaylistPath().equalsIgnoreCase(this.usbMediaFile.getPlaylistPath())) {
            setCurrentPlaylist(usbMediaFile);
        }*/
    }
}
