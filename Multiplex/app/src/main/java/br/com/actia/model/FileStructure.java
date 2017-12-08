package br.com.actia.model;

/**
 * Created by Armani andersonarmani@gmail.com on 22/10/16.
 */

public class FileStructure {
    private String          bgColor;
    private AppTopBar       topBar;
    private AppButtonArea   buttonArea;
    private AppBottomBar    bottomArea;
    private AppButton       button;
    private AppChromotherapy chromo;

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public AppTopBar getTopBar() {
        return topBar;
    }

    public void setTopBar(AppTopBar topBar) {
        this.topBar = topBar;
    }

    public AppButtonArea getButtonArea() {
        return buttonArea;
    }

    public void setButtonArea(AppButtonArea buttonArea) {
        this.buttonArea = buttonArea;
    }

    public AppBottomBar getBottomArea() {
        return bottomArea;
    }

    public void setBottomArea(AppBottomBar bottomArea) {
        this.bottomArea = bottomArea;
    }

    public AppButton getButton() {
        return button;
    }

    public void setButton(AppButton button) {
        this.button = button;
    }

    public AppChromotherapy getChromo() {
        return chromo;
    }

    public void setChromo(AppChromotherapy chromo) {
        this.chromo = chromo;
    }
}
