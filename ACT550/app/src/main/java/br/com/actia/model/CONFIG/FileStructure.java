package br.com.actia.model.CONFIG;

/**
 * Created by Armani andersonarmani@gmail.com on 28/10/16.
 */

public class FileStructure {
    private String          bgColor;
    private AppTopBar       topBar;
    private AppMenuBar      menuBar;
    private AppWorkArea     workArea;
    private AppBottomArea   bottomArea;

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

    public AppMenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(AppMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public AppWorkArea getWorkArea() {
        return workArea;
    }

    public void setWorkArea(AppWorkArea workArea) {
        this.workArea = workArea;
    }

    public AppBottomArea getBottomArea() {
        return bottomArea;
    }

    public void setBottomArea(AppBottomArea bottomArea) {
        this.bottomArea = bottomArea;
    }
}
