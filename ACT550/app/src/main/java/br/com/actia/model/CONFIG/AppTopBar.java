package br.com.actia.model.CONFIG;

import java.util.List;

/**
 * Created by Armani andersonaramni@gmail.com on 30/10/16.
 */

public class AppTopBar {
    private String          bgColor;
    private List<String>    logoList;
    private String          area1TextColor;
    private String          area2TextColor;
    private String          area1Text;
    private String          area2Text;

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public List<String> getLogoList() {
        return logoList;
    }

    public void setLogoList(List<String> logoList) {
        this.logoList = logoList;
    }

    public String getArea1TextColor() {
        return area1TextColor;
    }

    public void setArea1TextColor(String area1TextColor) {
        this.area1TextColor = area1TextColor;
    }

    public String getArea2TextColor() {
        return area2TextColor;
    }

    public void setArea2TextColor(String area2TextColor) {
        this.area2TextColor = area2TextColor;
    }

    public String getArea1Text() {
        return area1Text;
    }

    public void setArea1Text(String area1Text) {
        this.area1Text = area1Text;
    }

    public String getArea2Text() {
        return area2Text;
    }

    public void setArea2Text(String area2Text) {
        this.area2Text = area2Text;
    }
}
