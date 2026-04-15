package twonineteengame.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.pmw.tinylog.Logger;
import twonineteengame.util.FileHelper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Handles the UI elements and features of the start menu.
 */
public class StartController {

    @FXML
    private TextField redPlayerName;
    @FXML
    private TextField bluePlayerName;

    @FXML
    private void onStartGameButton(final ActionEvent actionEvent) throws IOException {
        if(redPlayerName.getText().isBlank() || bluePlayerName.getText().isBlank())
            Logger.error("No two valid names entered!");
        else {
            Logger.info("Loading game with red player " + redPlayerName.getText() + " and blue player " + bluePlayerName.getText());
            final var root = FXMLLoader.<Parent>load(Objects.requireNonNull(getClass().getResource("/ui/gameboard.fxml")));
            final var stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setUserData(new PlayerNames(redPlayerName.getText(),bluePlayerName.getText()));
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
    @FXML
    private void onLoadScoreboardButton(final ActionEvent actionEvent) throws IOException {
        Logger.info("Selecting JSON file to load scoreboard from");
        ObjectMapper objectMapper = new ObjectMapper();
        final var stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FileHelper.showDialog(true, stage)
                .ifPresent(file -> {
                    try {
                        List<GameResults> gameResults = objectMapper.readValue(file, new TypeReference<List<GameResults>>() {});
                        stage.setUserData(gameResults);
                    } catch (IOException e) {
                        Logger.error(e, "Failed to load data!");
                    }
                });
        final var root = FXMLLoader.<Parent>load(Objects.requireNonNull(getClass().getResource("/ui/scoreboard.fxml")));
        stage.setScene(new Scene(root));
        stage.show();


    }
}
