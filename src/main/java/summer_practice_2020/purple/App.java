package summer_practice_2020.purple;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class App extends Application {
    @Override
    public void start(Stage s) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));
        Scene sc = new Scene(root);
        URL css_path = this.getClass().getResource("style/light/main.css");
        if (css_path == null) {
            System.err.println("Критическая ошибка: CSS файл не найден! Программа будет завершена.");
            return;
        } else {
            sc.getStylesheets().add(css_path.toString());
        }

        s.setTitle("Визуализация работы алгоритма Борувки");
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
