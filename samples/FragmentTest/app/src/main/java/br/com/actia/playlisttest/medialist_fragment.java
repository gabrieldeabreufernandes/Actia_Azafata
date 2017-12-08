package br.com.actia.playlisttest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Armani andersonaramni@gmail.com on 17/02/17.
 */

public class medialist_fragment extends Fragment implements AdapterView.OnItemClickListener {
    ArrayList<String> playlist_list;
    ArrayAdapter<String> arrayAdapter;
    ListView        playlistView;
    ImageButton     ibReturn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view       = inflater.inflate(R.layout.media_list_view, container, false);
        playlistView    = (ListView) view.findViewById(R.id.list_medias);
        ibReturn        = (ImageButton) view.findViewById(R.id.return_button);

        ibReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        playlist_list = new ArrayList<>();
        playlist_list.add("music 1");
        playlist_list.add("music 2");
        playlist_list.add("music 3");
        playlist_list.add("music 4");
        playlist_list.add("music 5");
        playlist_list.add("music 6");
        playlist_list.add("music 7");
        playlist_list.add("music 8");

        arrayAdapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, playlist_list);

        playlistView.setAdapter(arrayAdapter);

        playlistView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long time) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
}
