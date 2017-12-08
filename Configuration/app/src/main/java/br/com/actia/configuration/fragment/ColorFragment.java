package br.com.actia.configuration.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;

import br.com.actia.configuration.R;
import br.com.actia.configuration.model.ListItem;

/**
 * Created by Armani andersonaramni@gmail.com on 19/11/16.
 */

public class ColorFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private static String TAG = "COLOR FRAGMENT";
    private static String FRAG_DATA = "item";
    private ListItem item;
    private View   spaceColor;
    private EditText txtColor;
    private SeekBar sbTransparent;
    private SeekBar sbRed;
    private SeekBar sbGreen;
    private SeekBar sbBlue;
    private Button  btnSave;
    private int colorValue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        item = (ListItem) getArguments().getSerializable(FRAG_DATA);

        View v = inflater.inflate(R.layout.color_fragment, container, false);

        spaceColor = v.findViewById(R.id.color_space);
        txtColor = (EditText) v.findViewById(R.id.color_text);
        sbTransparent = (SeekBar) v.findViewById(R.id.seek_transparent);
        sbRed = (SeekBar) v.findViewById(R.id.seek_red);
        sbGreen = (SeekBar) v.findViewById(R.id.seek_green);
        sbBlue = (SeekBar) v.findViewById(R.id.seek_blue);
        btnSave = (Button) v.findViewById(R.id.color_save);

        sbTransparent.setOnSeekBarChangeListener(this);
        sbRed.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Button clicked");
            }
        });

        txtColor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        if(item != null) {
            colorValue = Color.parseColor(item.getValue());
        }
        else {
            colorValue = Color.parseColor("#FF000000");
        }

        Log.v(TAG, "COR = " + item.getValue() + "  HEX == " + Integer.toHexString(colorValue));
        refreshSeekBars();
        return v;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(seekBar == sbTransparent) {
            colorValue = (colorValue & 0x00FFFFFF) + (progress << 24);
        }
        else if(seekBar == sbRed) {
            colorValue = (colorValue & 0xFF00FFFF) + (progress << 16);
        }
        else if(seekBar == sbGreen) {
            colorValue = (colorValue & 0xFFFF00FF) + (progress << 8);
        }
        else {
            colorValue = (colorValue & 0xFFFFFF00) + progress;
        }
        setSpaceColor();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void setSpaceColor() {
        spaceColor.setBackgroundColor(colorValue);
        txtColor.setText(Integer.toHexString(colorValue));
    }

    private void refreshSeekBars() {
        int val;

        val = (colorValue & 0xFF000000) >> 24;
        sbTransparent.setProgress(val);

        val = (colorValue & 0x00FF0000) >> 16;
        sbRed.setProgress(val);

        val = (colorValue & 0x0000FF00) >> 8;
        sbGreen.setProgress(val);

        val = colorValue & 0x000000FF;
        sbBlue.setProgress(val);
    }
}
