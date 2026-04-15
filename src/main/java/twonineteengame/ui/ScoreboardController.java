package twonineteengame.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles the UI elements of the scoreboard.
 */
public class ScoreboardController {

    @FXML
    private TableView<GameResults> highScoreTable;

    @FXML
    private TableColumn<GameResults, String> redPlayer;

    @FXML
    private TableColumn<GameResults, String> bluePlayer;

    @FXML
    private TableColumn<GameResults, String> winner;

    @FXML
    private TableColumn<GameResults, Integer> steps;



    @FXML
    private void initialize() {
        Logger.debug("Loading high scores");
        Platform.runLater(() -> {
            List<GameResults> gameResults = new ArrayList<>();
            gameResults = (List<GameResults>) highScoreTable.getScene().getWindow().getUserData();
            ObservableList<GameResults> observableGameResults = FXCollections.observableArrayList();
            observableGameResults.addAll(gameResults);

            redPlayer.setCellValueFactory(new PropertyValueFactory<>("redPlayerName"));
            bluePlayer.setCellValueFactory(new PropertyValueFactory<>("bluePlayerName"));
            winner.setCellValueFactory(new PropertyValueFactory<>("winner"));
            steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
            highScoreTable.setItems(observableGameResults);
        });
    }

    @FXML
    private void onBackButton(final ActionEvent actionEvent) throws IOException {
        Logger.info("Going back to start menu");
        final var root = FXMLLoader.<Parent>load(Objects.requireNonNull(getClass().getResource("/ui/startmenu.fxml")));
        final var stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
