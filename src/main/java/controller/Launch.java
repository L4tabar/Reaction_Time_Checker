package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import javafx.scene.control.TextField;

import java.io.IOException;


@Slf4j
public class Launch{

    public static String nev = "";

    @FXML
    AnchorPane menu;
    @FXML
    TextField username;
    @FXML
    Label errorLabel;
    @FXML
    private void close() {
        menu.setDisable(true);
    }
    @FXML
    private void open() {
        menu.setDisable(false);
    }



    @FXML
    public void start(){

        if (username.getText().isEmpty()) {
            errorLabel.setText(" Adj meg egy nevet!!");
        } else {
            Stage stage = new Stage();
            Game game = new Game();
            nev = username.getText();
            Game.felhasznalo = nev;
            try {
                game.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void topTen() throws IOException {

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/topten.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
        }
}
