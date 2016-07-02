package com.jean.fiction.controller;

import com.jean.fiction.model.Model;
import com.jean.fiction.service.DownloadService;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class MainController extends BaseController {

    @FXML
    Button download;
    @FXML
    TextField url;
    @FXML
    TableView<Model> table;

    @Override
    public void initialize(URL u, ResourceBundle rb) {
        TableColumn name = table.getColumns().get(0);
        name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Model, Object>, ObservableValue<Object>>() {
            @Override
            public ObservableValue<Object> call(TableColumn.CellDataFeatures<Model, Object> param) {
                return new SimpleObjectProperty<Object>(param.getValue().getName());
            }
        });
        TableColumn info = table.getColumns().get(1);
        info.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Model, Object>, ObservableValue<Object>>() {
            @Override
            public ObservableValue<Object> call(TableColumn.CellDataFeatures<Model, Object> param) {
                return new SimpleObjectProperty<Object>(param.getValue().getInfo());
            }
        });
        TableColumn progress = table.getColumns().get(2);
        progress.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Model, Object>, ObservableValue<Object>>() {
            @Override
            public ObservableValue<Object> call(TableColumn.CellDataFeatures<Model, Object> param) {
                return new SimpleObjectProperty<Object>(param.getValue().getProgressBar());
            }
        });

        download.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String bookId = MainController.this.url.getText();
                if (bookId != null && !bookId.isEmpty()) {
                    final DownloadService downloadService = new DownloadService(bookId);
                    Model model = new Model();
                    model.getName().textProperty().bind(new StringBinding() {
                        {
                            bind(downloadService.bookName, downloadService.author);
                        }

                        @Override
                        protected String computeValue() {
                            return downloadService.bookName.get() + "-" + downloadService.author.get();
                        }
                    });
                    model.getInfo().textProperty().bind(downloadService.messageProperty());
                    model.getProgressBar().progressProperty().bind(downloadService.progressProperty());
                    table.getItems().add(model);
                    downloadService.restart();
                }
            }
        });
    }
}
