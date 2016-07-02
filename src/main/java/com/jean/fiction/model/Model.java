package com.jean.fiction.model;

import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

/**
 * Created by jinshubao on 2016/7/1.
 */
public class Model {

    private Text name = new Text("");
    private Text info = new Text("");
    private ProgressBar progressBar = new ProgressBar(0);

    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }

    public Text getInfo() {
        return info;
    }

    public void setInfo(Text info) {
        this.info = info;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
