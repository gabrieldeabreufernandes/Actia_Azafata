package br.com.actia.model;

import java.util.List;

/**
 * Created by Armani andersonaramni@gmail.com on 30/10/16.
 */

public class AppButtonArea {
    private String      bgColor;
    private String      titleColor;
    private String      title1;
    private String      title2;
    private List<AppButtonIcon> buttonList;

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public List<AppButtonIcon> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<AppButtonIcon> buttonList) {
        this.buttonList = buttonList;
    }
}
