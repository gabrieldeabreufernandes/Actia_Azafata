package br.com.actia.playlisttest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Armani andersonaramni@gmail.com on 17/02/17.
 */

public class playlist_fragment extends Fragment implements AdapterView.OnItemClickListener {
    ArrayList<String> playlist_list;
    ArrayAdapter<String> arrayAdapter;
    ListView playlistView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.playlist_view, container, false);
        playlistView = (ListView) view.findViewById(R.id.list_view_id);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        playlist_list = new ArrayList<>();
        playlist_list.add("Anderson");
        playlist_list.add("Augusto");
        playlist_list.add("Armani");
        playlist_list.add("Adriane");
        playlist_list.add("Dienstmann");
        playlist_list.add("Armani");

        arrayAdapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, playlist_list);

        playlistView.setAdapter(arrayAdapter);

        playlistView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long time) {
        Fragment newFragment = new medialist_fragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.list_fragment_id, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}
