package br.com.actia.model.CONFIG;

import java.util.List;

/**
 * Created by Armani andersonaramni@gmail.com on 28/12/16.
 */
public class AppBottomArea {
    private String          bgColor;
    private List<String>    logoList;

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
}
