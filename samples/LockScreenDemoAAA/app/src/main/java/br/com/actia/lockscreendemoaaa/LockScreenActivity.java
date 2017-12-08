package br.com.actia.lockscreendemoaaa;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Armani andersonaramni@gmail.com on 17/05/17.
 */

public class LockScreenActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner userSpinner = null;

    @Override
    //public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState, persistentState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_screen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Button btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);

        userSpinner = (Spinner) findViewById(R.id.spinner_user);

        List<String> userList = new ArrayList();
        userList.add("Operador");
        userList.add("Admin");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(userList);

        userSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
