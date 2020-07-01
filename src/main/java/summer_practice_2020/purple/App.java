package summer_practice_2020.purple;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;

import javafx.stage.Stage;

import javafx.scene.control.Button;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import java.io.IOException;


public class App extends Application {
    @Override
    public void start(Stage s) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));

        Button btn = (Button) root.lookup("#button1");
        btn.setOnAction(ev -> System.out.println("Hello world!"));

        Scene sc = new Scene(root, 640, 480);

        s.setTitle("Hello, World!");
        s.setScene(sc);
        s.show();
    }

    @FXML
    protected void onQuitClicked(ActionEvent e) {
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
