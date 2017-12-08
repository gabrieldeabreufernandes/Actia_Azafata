package br.com.actia.controller;

import android.os.Build;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.actia.multiplex.MainActivity;
import br.com.actia.multiplex.R;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */

public class MessageBarController {
    public final static int TYPE_MESSAGE = 1;
    public final static int TYPE_INFORMATION = 2;
    public final static int TYPE_ERROR = 3;
    public final static int TYPE_FIXED_INFORMATION = 4;

    private static MessageBarController messageBarControllerInstance = null;
    private ImageView   messageIcon = null;
    private TextView    messageText = null;
    private MainActivity mainActivity;

    private int imgIDFixed = 0;
    private int colorIDFixed = 0;
    private String textFixed = "";

    private MessageBarController(){}


    public static MessageBarController getInstance() {
        if(messageBarControllerInstance == null) {
            messageBarControllerInstance = new MessageBarController();
        }
        return  messageBarControllerInstance;
    }

    public void initializeData(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.messageIcon = (ImageView) mainActivity.findViewById(R.id.msg_image);
        this.messageText = (TextView) mainActivity.findViewById(R.id.msg_text);
    }

    public void setMessage(int type, String text) {
        int imgID = 0;
        int colorID = 0;

        switch (type) {
            case TYPE_MESSAGE:
                imgID = R.drawable.ok;
                colorID = R.color.md_green_300;
                break;
            case TYPE_INFORMATION:
                imgID = R.drawable.information;
                colorID = R.color.md_blue_300;
                break;
            case TYPE_ERROR:
                imgID = R.drawable.exclamation;
                colorID = R.color.md_red_300;
                break;
            case TYPE_FIXED_INFORMATION:
                //USED for azafata
                imgID = R.drawable.question; //R.drawable.information;
                colorID = R.color.md_orange_A400;

                imgIDFixed   = imgID;
                colorIDFixed = colorID;
                textFixed    = text;
                break;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            messageText.setTextColor( mainActivity.getResources().getColor(colorID, mainActivity.getTheme()));
            messageIcon.setImageDrawable( mainActivity.getResources().getDrawable(imgID, mainActivity.getTheme()));
        }
        else {
            messageText.setTextColor( mainActivity.getResources().getColor(colorID));
            messageIcon.setImageDrawable( mainActivity.getResources().getDrawable(imgID));
        }
        messageText.setText(text);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                clearFields();
            }
        }, 2000);
    }

    private void clearFields() {

        if(!textFixed.equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                messageText.setTextColor( mainActivity.getResources().getColor(colorIDFixed, mainActivity.getTheme()));
                messageIcon.setImageDrawable( mainActivity.getResources().getDrawable(imgIDFixed, mainActivity.getTheme()));
            }
            else {
                messageText.setTextColor( mainActivity.getResources().getColor(colorIDFixed));
                messageIcon.setImageDrawable( mainActivity.getResources().getDrawable(imgIDFixed));
            }

            messageText.setText(textFixed);
        }
        else {
            messageIcon.setImageDrawable(null);
            messageText.setText("");
        }
    }

    public void clearFixedInformations() {
        textFixed = "";
    }
}

