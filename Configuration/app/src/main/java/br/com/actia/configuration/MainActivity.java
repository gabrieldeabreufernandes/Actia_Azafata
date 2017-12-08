package br.com.actia.configuration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.actia.configuration.fragment.ColorFragment;
import br.com.actia.configuration.model.ListItem;

public class MainActivity extends AppCompatActivity {
    private static String FRAG_DATA = "item";
    private ListItem lItemAppBgColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<ListItem> listItems = new ArrayList<>();
        ListItemAdapter listItemAdapter = new ListItemAdapter(this, listItems);

        final ListView listView = (ListView) findViewById(R.id.preference_list);
        listView.setAdapter(listItemAdapter);


        lItemAppBgColor = new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.bg_color), "#3377DD");

        listItems.add(new ListItem(ListItem.TYPE_SEPARATOR, getResources().getString(R.string.multiplex), ""));
        listItems.add(lItemAppBgColor);

        listItems.add(new ListItem(ListItem.TYPE_SEPARATOR, getResources().getString(R.string.top_bar), ""));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.bg_color), "#FFFFFF"));
        listItems.add(new ListItem(ListItem.TYPE_TEXT, getResources().getString(R.string.logo_list), ""));

        listItems.add(new ListItem(ListItem.TYPE_SEPARATOR, getResources().getString(R.string.button_area), ""));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.bg_color), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.title_color), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_TEXT, getResources().getString(R.string.title1), ""));
        listItems.add(new ListItem(ListItem.TYPE_TEXT, getResources().getString(R.string.title2), ""));

        listItems.add(new ListItem(ListItem.TYPE_SEPARATOR, getResources().getString(R.string.bottom_area), ""));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.bg_color), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.message_color), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.alert_color), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.error_color), "#000000"));

        listItems.add(new ListItem(ListItem.TYPE_SEPARATOR, getResources().getString(R.string.button), ""));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.btn_disable1), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.btn_disable2), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.btn_normal1), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.btn_normal2), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.btn_stage1_1), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.btn_stage1_2), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.btn_stage2_1), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.btn_stage2_2), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.btn_press1), "#000000"));
        listItems.add(new ListItem(ListItem.TYPE_COLOR, getResources().getString(R.string.btn_press2), "#000000"));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                ListItem item = (ListItem)adapter.getItemAtPosition(position);

                //Ignore this kind of item
                if(item.getType() == ListItem.TYPE_SEPARATOR)
                    return;

                listView.setItemChecked(position, true);

                android.app.Fragment fragment = getItemFragment(item);

                if(fragment != null) {
                    android.app.FragmentManager fm = getFragmentManager();
                    android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();

                    fragmentTransaction.replace(R.id.preference_container, fragment);
                    //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.commit();
                }

            }
        });
    }

    /**
     * It is a Fragment factory, responsible to create the appropriate Item Fragment
     * @return
     */
    private android.app.Fragment getItemFragment(ListItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FRAG_DATA, item);

        android.app.Fragment fragment = new ColorFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
