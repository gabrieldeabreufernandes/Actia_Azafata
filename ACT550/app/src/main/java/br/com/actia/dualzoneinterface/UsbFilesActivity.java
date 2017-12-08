package br.com.actia.dualzoneinterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.actia.controller.PlaylistHandler;

/**
 * Created by Armani andersonaramni@gmail.com on 26/02/17.
 */

public class UsbFilesActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "UsbFilesActivity";
    private ListView pendriveListView;
    private ListView tabletListView;
    private Button   btnDelete;
    private Button   btnRefresh;
    private Button   btnToRight;

    private List<String> listUsb;
    private List<String> listTablet;

    private ArrayAdapter arrayAdapterTablet;
    private ArrayAdapter arrayAdapterUsb;

    private String strUsbPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_usb);

        pendriveListView = (ListView) findViewById(R.id.list_pendrive_id);
        tabletListView = (ListView) findViewById(R.id.list_tablet_id);

        btnDelete   = (Button) findViewById(R.id.btn_usb_delete);
        btnRefresh  = (Button) findViewById(R.id.btn_usb_refresh);
        btnToRight  = (Button) findViewById(R.id.btn_usb_toright);

        btnDelete.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnToRight.setOnClickListener(this);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Toast.makeText(getApplicationContext(), "EXTRAS NULL", Toast.LENGTH_LONG).show();
            } else {
                strUsbPath = extras.getString(UsbBroadcastReceiver.USB_DATA);
                Toast.makeText(getApplicationContext(), strUsbPath, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "SAVE INSTANCE NULL", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        PlaylistHandler playlistHandler = new PlaylistHandler(strUsbPath);
        listTablet = playlistHandler.getPlaylists(PlaylistHandler.PLAYLIST_USB, PlaylistHandler.FOLDER_FILTER);
        if(listTablet != null && listTablet.size() > 1)
            Collections.sort(listTablet);

        arrayAdapterTablet = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.simple_list_item_multiple_choice, listTablet);

        if(listTablet == null) {
            listTablet = new ArrayList<>();
        }

        tabletListView.setAdapter(arrayAdapterTablet);
        tabletListView.setOnItemClickListener(this);

        //LIST PENDRIVE FILES
        listUsb = playlistHandler.getPlaylists(PlaylistHandler.PLAYLIST_EXTERNAL_USB, PlaylistHandler.FOLDER_FILTER);//PlaylistHandler.USB_PATH);

        arrayAdapterUsb = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.simple_list_item_multiple_choice, listUsb);

        if(listUsb != null) {
            Collections.sort(listUsb);
            pendriveListView.setAdapter(arrayAdapterUsb);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.media_notfound, Toast.LENGTH_LONG).show();
        }

        pendriveListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {
        if(view == btnDelete) {
            int idx = tabletListView.getCheckedItemPosition();
            if(idx >= 0)
                deleteTabletItem(idx);
        } else if (view == btnRefresh) {
            refreshTabletList();
        } else if (view == btnToRight) {
            copyFilesFromUSB();
        }
    }


    private void deleteTabletItem(final long index) {
        //Confirmation OK?
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirmatioin)
                .setMessage(R.string.delete_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PlaylistHandler playlistHandler = new PlaylistHandler(strUsbPath);
                        boolean ret = playlistHandler.deletePlaylist(listTablet.get((int) index));

                        if(ret == true) {
                            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                                    android.R.anim.slide_out_right);
                            anim.setDuration(500);

                            tabletListView.getChildAt((int)index).startAnimation(anim);

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    refreshTabletList();
                                }
                            }, anim.getDuration());
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void copyFilesFromUSB() {
        if(pendriveListView.getCheckedItemCount() <= 0)
            return;

        PlaylistHandler playlistHandler = new PlaylistHandler(strUsbPath);

        SparseBooleanArray selectedItems = pendriveListView.getCheckedItemPositions();

        for(int i = 0; i < selectedItems.size(); i++) {
            String item = listUsb.get(selectedItems.keyAt(i));

            //Doesn't copy item already in the list
            if(listTablet.contains(item))
                continue;

            boolean ret = playlistHandler.copyPlaylistFromUSB(item);

            if(ret ==  true) {
                listTablet.add(item);
                Collections.sort(listTablet);

                tabletListView.clearChoices();
                arrayAdapterTablet.clear();
                arrayAdapterTablet.addAll(listTablet);
                arrayAdapterTablet.notifyDataSetChanged();
            } else {
                Log.d(TAG, "Error copying file" + listUsb.get(selectedItems.keyAt(i)));
            }
        }

        pendriveListView.clearChoices();
        arrayAdapterUsb.notifyDataSetChanged();
    }

    private void refreshTabletList() {
        tabletListView.clearChoices();
        arrayAdapterTablet.notifyDataSetChanged();

        PlaylistHandler playlistHandler = new PlaylistHandler(strUsbPath);
        listTablet = playlistHandler.getPlaylists(PlaylistHandler.PLAYLIST_USB, PlaylistHandler.FOLDER_FILTER);
        Collections.sort(listTablet);

        arrayAdapterTablet.clear();
        arrayAdapterTablet.addAll(listTablet);
        arrayAdapterTablet.notifyDataSetChanged();
    }

}
