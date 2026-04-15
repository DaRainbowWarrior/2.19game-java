package twonineteengame.util;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.pmw.tinylog.Logger;
import twonineteengame.model.GameState;
import twonineteengame.model.Position;
import twonineteengame.model.Value;

/**
 * A helper class to {@code GameState}, providing essential functions for moving on the board.
 */
public class GameStateMoveMaker {

    /**
     * An enum visualising the 3 stages for movement on the board.
     */
    public enum Phase {
        /**
         * Represents the first stage of moving: selecting the piece to be moved.
         */
        FROM,
        /**
         * Represents the second stage of moving: selecting the space to move to.
         */
        TO,
        /**
         * Represents the readiness for moving pieces.
         */
        READY

    }

    private GameState model;

    private Position from;

    private Position to;

    /**
     * Keeps track of whether the game has been won or not yet.
     */
    public boolean isGameWon = false;

    /**
     * The amount of movement made by both players combined.
     */
    public int steps;

    /**
     * A Phase {@code enum}, objectified, for keeping track of movement.
     */
    public ReadOnlyObjectWrapper<Phase> phase = new ReadOnlyObjectWrapper<>(Phase.FROM);

    /**
     * A Value {@code enum}, objectified, for keeping track of which players turn it is.
     */
    public ReadOnlyObjectWrapper<Value> playerTurn = new ReadOnlyObjectWrapper<>(Value.BLUE);

    /**
     *  Creates a new GameStateMoveMaker class.
     * @param model a GameState object
     */
    public GameStateMoveMaker(GameState model) {
        this.model = model;
    }

    /**
     * @return the current value of the phase object
     */
    public ReadOnlyObjectProperty<Phase> phaseState() {
        return phase.getReadOnlyProperty();
    }

    /**
     * @return the {@code Position} of the piece to be moved
     */
    public Position getFrom() {
        return from;
    }

    /**
     * @return {@code true} if phase object is {@code Phase.READY}; {@code false} if not
     */
    public boolean isPhaseReady() {
        return phase.get() == Phase.READY;
    }

    /**
     *  Based on where on the 3 phase moving is the program, assigns the clicked position on the board.
     * @param position the clicked position on the board
     */
    public void selectMove(Position position) {
        switch (phase.get()) {
            case FROM -> selectMoveFrom(position);
            case TO -> selectMoveTo(position);
            case READY -> throw new IllegalStateException();
        }
    }

    private void selectMoveFrom(Position position) {
        if(!model.isEmpty(position) && model.getBoardValueFromGameState(position.row(),position.col()).get() == playerTurn.get() && !isGameWon) {
            Logger.info("Selected piece to move at: " + position);
            from = position;
            phase.set(Phase.TO);
        }
    }

    private void selectMoveTo(Position position) {
        if(model.canMove(from, position)) {
            Logger.info("Selected empty space to move to at: " + position);
            to = position;
            phase.set(Phase.READY);
        }
    }

    /**
     * When all conditions are met, moves the piece to the desired location.
     * Adjusts the player turn. Increments the step counter.
     */
    public void makeMove() {
        if(phase.get() != Phase.READY)
            throw new IllegalStateException();
        Logger.info("Moved piece");
        model.move(from, to);
        switch (playerTurn.get()) {
            case RED -> playerTurn.set(Value.BLUE);
            case BLUE -> playerTurn.set(Value.RED);
            case EMPTY -> throw new IllegalStateException();
        }
        steps++;
        reset();
    }

    private void reset() {
        from = null; to = null;
        phase.set(Phase.FROM);
    }
}
