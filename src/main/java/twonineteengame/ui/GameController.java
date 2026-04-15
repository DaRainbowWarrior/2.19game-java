package twonineteengame.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.pmw.tinylog.Logger;
import twonineteengame.model.GameState;
import twonineteengame.model.Position;
import twonineteengame.model.Value;
import twonineteengame.util.FileHelper;
import twonineteengame.util.GameStateMoveMaker;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static twonineteengame.model.GameState.colSize;
import static twonineteengame.model.GameState.rowSize;

/**
 * Handles the main game UI elements and features.
 */
public class GameController {
    @FXML
    private GridPane board;

    @FXML
    private Label redLabel;

    @FXML
    private Label blueLabel;

    @FXML
    private Label turnLabel;

    @FXML
    private Label stepsLabel;

    @FXML
    private Button startButton;

    @FXML
    private Button finishButton;

    private GameState model;

    private GameStateMoveMaker moveMaker;

    private BooleanProperty inGame;

    private PlayerNames playerNames;

    private List<GameResults> gameResults;

    @FXML
    private void initialize() {
        Logger.info("Initializing variables");
        Platform.runLater(() -> {
            playerNames = (PlayerNames) board.getScene().getWindow().getUserData();
            redLabel.setText(playerNames.redPlayerName);
            blueLabel.setText(playerNames.bluePlayerName);
        });
        buildBoard();
        gameResults = new ArrayList<>();
        inGame = new SimpleBooleanProperty(true);
        moveMaker.playerTurn = new ReadOnlyObjectWrapper<>(Value.BLUE);
        stepsLabel.setText("0");
        startButton.setDisable(true);
        labelNextTurn();}

    private void buildBoard() {
        Logger.info("Setting GameState, GameStateMoveMaker to default");
        model = new GameState();
        moveMaker = new GameStateMoveMaker(model);
        board.getStyleClass().add("board");
        board.getChildren().clear();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
        Logger.info("Adding listener to GameStateMoveMaker phase enum");
        moveMaker.phaseState().addListener((element, old, actual) -> {
            switch (actual) {
                case FROM -> {}
                case TO -> showFrom(moveMaker.getFrom());
                case READY -> hideFrom(moveMaker.getFrom());
            }
        });
    }

    private StackPane createSquare(int i, int j) {
        StackPane square = new StackPane();
        square.getStyleClass().add("square");
        Circle circle = new Circle(30);
        circle.fillProperty().bind(createSquareBinding(model.getBoardValueFromGameState(i, j)));
        square.getChildren().add(circle);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private ObjectBinding<Paint> createSquareBinding(ReadOnlyObjectProperty<Value> squareProperty) {
        return new ObjectBinding<Paint>() {{ super.bind(squareProperty); }
            @Override
            protected Paint computeValue() {
                return switch (squareProperty.get()) {
                    case EMPTY -> Color.TRANSPARENT;
                    case RED -> Color.RED;
                    case BLUE -> Color.BLUE;
                };
            }
        };
    }

    @FXML
    private void handleMouseClick(MouseEvent mouseEvent) {
        var square = (StackPane) mouseEvent.getSource();
        Position clickPosition = new Position(GridPane.getRowIndex(square),GridPane.getColumnIndex(square));
        moveMaker.selectMove(clickPosition);
        if(moveMaker.isPhaseReady()) {
            moveMaker.makeMove();
            labelNextTurn();
            handleWinning(clickPosition);
            stepsLabel.setText(Integer.toString(moveMaker.steps));
        }
    }

    private void labelNextTurn() {
        switch (moveMaker.playerTurn.get()) {
            case RED -> {
                turnLabel.setText("RED");
                turnLabel.setTextFill(Color.RED);
            }
            case BLUE -> {
                turnLabel.setText("BLUE");
                turnLabel.setTextFill(Color.BLUE);
            }
            case EMPTY -> throw new IllegalStateException();
        }
    }

    private void handleWinning(Position position) {
        if(model.winCheck(position) != Value.EMPTY) {
            moveMaker.isGameWon = true;
            Logger.info("Winner is " + blueLabel.getText() + " in "
                    + moveMaker.steps + " steps with last move in "
                    + position);
            startButton.setDisable(false);
            finishButton.setDisable(true);
            inGame.set(false);
            saveGameResult();
        }
    }

    private void saveGameResult() {
        switch (moveMaker.playerTurn.get()) {
            case RED ->
                gameResults.add(new GameResults(redLabel.getText(),blueLabel.getText(),"Blue", moveMaker.steps));
            case BLUE ->
               gameResults.add(new GameResults(redLabel.getText(),blueLabel.getText(),"Red", moveMaker.steps));
            case EMPTY -> throw new IllegalStateException();
        }
    }

    private void showFrom(Position position) {
        var pane = getSquare(position);
        pane.getStyleClass().add("selected");
    }

    private void hideFrom(Position position) {
        var pane = getSquare(position);
        pane.getStyleClass().remove("selected");
    }

    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }throw new AssertionError();
    }

    @FXML
    private void onStartButton() {
        Logger.info("Started new game");
        inGame.set(true);
        moveMaker.steps = 0;
        stepsLabel.setText(Integer.toString(moveMaker.steps));
        moveMaker.playerTurn.set(Value.BLUE);
        labelNextTurn();
        buildBoard();
        startButton.setDisable(true);
        finishButton.setDisable(false);
    }

    @FXML
    private void onFinishButton() {
        Logger.info("Game aborted at " + moveMaker.steps + " steps.");
        inGame.set(false);
        finishButton.setDisable(true);
        startButton.setDisable(false);
    }

    @FXML
    private void onSaveButton(ActionEvent actionEvent) {
        Logger.info("Saving scoreboard");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FileHelper.showDialog(false, stage)
                .ifPresent(file -> {
                    try {
                        objectMapper.writeValue(file, gameResults);
                    } catch (IOException e) {
                        Logger.error(e, "Failed to save results!");
                    }
                });
    }
}
