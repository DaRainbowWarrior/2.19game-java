package twonineteengame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * Represents a state-space representation of the game.
 */
public class GameState {

    /**
     * The number of rows that make up the board.
     */
    public static final int rowSize = 5;

    /**
     * The number of columns that make up the board.
     */
    public static final int colSize = 4;
    private ReadOnlyObjectWrapper<Value>[][] board = new ReadOnlyObjectWrapper[rowSize][colSize];

    /**
     * Creates the pieces and places them in their default positions.
     */
    public GameState() {
        for(int i = 0; i < rowSize; i++)
            for(int j = 0; j < colSize; j++) {
                switch (i) {
                    case 0 -> {
                        if (j % 2 == 0)
                            board[i][j] = new ReadOnlyObjectWrapper<Value>(Value.BLUE);
                        else
                            board[i][j] = new ReadOnlyObjectWrapper<Value>(Value.RED);
                    }
                    case rowSize - 1 -> {
                        if (j % 2 == 0)
                            board[i][j] = new ReadOnlyObjectWrapper<Value>(Value.RED);
                        else
                            board[i][j] = new ReadOnlyObjectWrapper<Value>(Value.BLUE);
                    }
                    default -> board[i][j] = new ReadOnlyObjectWrapper<Value>(Value.EMPTY);
                }

            }

    }

    /**
     *  Finds a specific position on the grid, and returns its state.
     * @param i the row index of position
     * @param j the column index of position
     * @return  {@code ReadOnlyObjectProperty<Value>} of the position on the board
     */
    public ReadOnlyObjectProperty<Value> getBoardValueFromGameState(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     *  Changes the state of a specific position of the board.
     * @param position the {@code Position} where the change needs to be made
     * @param value the state to set the {@code Position} to
     */
    private void setValue(Position position, Value value) {
        board[position.row()][position.col()].set(value);
    }

    /**
     *  Moves a piece to another position on the board.
     * @param from the {@code Position} of the piece to be moved
     * @param to the {@code Position} to move the piece to
     */
    public void move(Position from,Position to) {
        setValue(to, getBoardValueFromGameState(from.row(), from.col()).get());
        setValue(from, Value.EMPTY);
    }

    /**
     *  Determines if the move is possible between the two positions.
     * @param from the {@code Position} of the piece to be moved
     * @param to the {@code Position} to move the piece to
     * @return  {@code true} if the piece can be moved to the new location; {@code false} if not
     */
    public boolean canMove(Position from, Position to) {
        return isOnBoard(from) && isOnBoard(to) && !isEmpty(from) && isEmpty(to) && isAdjacent(from, to);
    }

    /**
     *  Determines if the given position contains a piece.
     * @param position the {@code Position} to be checked
     * @return {@code true} if the {@code Position} contains no piece; {@code false} if it does
     */
    public boolean isEmpty(Position position) {
        return getBoardValueFromGameState(position.row(), position.col()).get() == Value.EMPTY;
    }

    /**
     *  Determines if the position is within the limits of the board.
     * @param position the {@code Position} to be checked
     * @return {@code true} if the {@code Position} is on the board; {@code false} if not
     */
    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < rowSize && 0 <= position.col() && position.col() < colSize;
    }

    /**
     *  Determines if the destination position is adjacent (up,down,left,right) to the starting position.
     * @param from the starting {@code Position}
     * @param to the {@code Position} that needs to be adjacent to the starting position
     * @return  {@code true} if the destination {@code Position} is adjacent; {@code false} if not
     */
    public static boolean isAdjacent(Position from, Position to) {
        var dx = Math.abs(to.row() - from.row());
        var dy = Math.abs(to.col() - from.col());
        return dx + dy == 1;
    }

    /**
     * Checks all possible ways for 3 adjacent same color pieces (winning condition).
     * @param p the {@code Position} to check for winners from
     * @return {@code Value} of the winner player; if {@code Value.EMPTY}, no winner has been found
     */
    public Value winCheck(Position p) {
        //checks horizontally - leftmost piece, rightmost piece, middle piece
        try {
            if(board[p.row()][p.col()].get() == board[p.row()][p.col()-1].get() && board[p.row()][p.col()].get() == board[p.row()][p.col()-2].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            if(board[p.row()][p.col()].get() == board[p.row()][p.col()+1].get() && board[p.row()][p.col()].get() == board[p.row()][p.col()+2].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            if(board[p.row()][p.col()].get() == board[p.row()][p.col()+1].get() && board[p.row()][p.col()].get() == board[p.row()][p.col()-1].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}


        //checks vertically - topmost piece, bottommost piece, middle piece
        try {
            if(board[p.row()][p.col()].get() == board[p.row()-1][p.col()].get() && board[p.row()][p.col()].get() == board[p.row()-2][p.col()].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            if(board[p.row()][p.col()].get() == board[p.row()+1][p.col()].get() && board[p.row()][p.col()].get() == board[p.row()+2][p.col()].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            if(board[p.row()][p.col()].get() == board[p.row()+1][p.col()].get() && board[p.row()][p.col()].get() == board[p.row()-1][p.col()].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}


        //checks diagonally - top left piece, bottom right piece, middle piece
        try {
            if(board[p.row()][p.col()].get() == board[p.row()+1][p.col()+1].get() && board[p.row()][p.col()].get() == board[p.row()+2][p.col()+2].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            if(board[p.row()][p.col()].get() == board[p.row()-1][p.col()-1].get() && board[p.row()][p.col()].get() == board[p.row()-2][p.col()-2].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            if(board[p.row()][p.col()].get() == board[p.row()+1][p.col()+1].get() && board[p.row()][p.col()].get() == board[p.row()-1][p.col()-1].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}

        //checks anti-diagonally - top right piece, bottom left piece, middle piece
        try {
            if(board[p.row()][p.col()].get() == board[p.row()+1][p.col()-1].get() && board[p.row()][p.col()].get() == board[p.row()+2][p.col()-2].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            if(board[p.row()][p.col()].get() == board[p.row()-1][p.col()+1].get() && board[p.row()][p.col()].get() == board[p.row()-2][p.col()+2].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}
        try {
            if(board[p.row()][p.col()].get() == board[p.row()-1][p.col()+1].get() && board[p.row()][p.col()].get() == board[p.row()+1][p.col()-1].get())
                return board[p.row()][p.col()].get();
        }catch (ArrayIndexOutOfBoundsException ignored) {}

        //if no 3 adjacent same-colour pieces (winner) found
        return Value.EMPTY;
    }
}
