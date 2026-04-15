package twonineteengame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.pmw.tinylog.Logger;

import java.util.Objects;

/**
 * Initialises program, starting up the start menu.
 * Sets the title of the window.
 */
public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Logger.info("Starting application, initializing start menu");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/startmenu.fxml")));
        stage.setTitle("Homework project - 2.19 game");
        var scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
