package com.jean.fiction.controller;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * Created by jinshubao on 2016/6/4.
 */
public abstract class BaseController implements Initializable {


    protected DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
