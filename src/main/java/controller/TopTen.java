package controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.PersistenceModule;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import model.Users;
import dao.UsersDao;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


@Slf4j
public class TopTen implements Initializable {


    @FXML
    AnchorPane topten;
    @FXML
    TableView table;
    @FXML
    TableColumn column1;
    @FXML
    TableColumn column2;
    @FXML
    TableColumn column3;

    @FXML
    public void topTen(Stage primaryStage) throws IOException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        UsersDao usersDao = injector.getInstance(UsersDao.class);
        List<Users> top = usersDao.getTopTen();
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        column3.setCellValueFactory(new PropertyValueFactory<>("score"));
        table.getItems().clear();
        table.getItems().addAll(top);

    }
}
