package br.com.actia.model;

import java.util.List;

/**
 * Created by Armani andersonaramni@gmail.com on 30/10/16.
 */

public class AppBottomBar {
    private String  bgColor;
    private String  messageColor;
    private String  alertColor;
    private String  errorColor;

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getMessageColor() {
        return messageColor;
    }

    public void setMessageColor(String messageColor) {
        this.messageColor = messageColor;
    }

    public String getAlertColor() {
        return alertColor;
    }

    public void setAlertColor(String alertColor) {
        this.alertColor = alertColor;
    }

    public String getErrorColor() {
        return errorColor;
    }

    public void setErrorColor(String errorColor) {
        this.errorColor = errorColor;
    }
}
