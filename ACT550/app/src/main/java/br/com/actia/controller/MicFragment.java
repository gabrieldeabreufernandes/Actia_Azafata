package br.com.actia.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.actia.dualzoneinterface.R;

/**
 * Created by Armani andersonaramni@gmail.com on 20/02/17.
 */

public class MicFragment extends Fragment{
    private View view;
    private boolean isDriver = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sd, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setIsDriver(boolean val) {
        isDriver = val;
    }
}
