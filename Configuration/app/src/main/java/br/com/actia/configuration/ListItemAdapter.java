package br.com.actia.configuration;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.actia.configuration.model.ListItem;

/**
 * Created by Armani andersonaramni@gmail.com on 15/11/16.
 */

public class ListItemAdapter extends BaseAdapter {
    List mList = null;
    Context context = null;
    LayoutInflater inflater = null;

    public ListItemAdapter(Context context, List itens) {
        mList = itens;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem item = (ListItem) getItem(position);
        TextView tvName = null;
        TextView tvValue = null;
        View     vColor = null;

        int type = item.getType();
        convertView = inflater.inflate(R.layout.item_color, parent, false);

        switch (type) {
            case ListItem.TYPE_COLOR:
                tvName = (TextView) convertView.findViewById(R.id.item_text);
                tvValue = (TextView) convertView.findViewById(R.id.item_value);
                vColor = (View) convertView.findViewById(R.id.item_color);
                break;
            case ListItem.TYPE_TEXT:
                tvName = (TextView) convertView.findViewById(R.id.item_text);
                tvValue = (TextView) convertView.findViewById(R.id.item_value);
                break;
            default:
                tvName = (TextView) convertView.findViewById(R.id.item_text);
                break;
        }

        // Populate the data into the template view using the data object
        if(tvName != null)
            tvName.setText(item.getName());

        if(tvValue != null) {
            tvValue.setText(item.getValue());
        }

        if(vColor != null) {
            vColor.setBackgroundColor(Color.parseColor(item.getValue()));
        }

        return convertView;
    }
}
