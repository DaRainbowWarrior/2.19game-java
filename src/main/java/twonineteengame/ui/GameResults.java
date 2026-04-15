package twonineteengame.ui;

/**
 * Helper class for easily creating and storing winning game's info.
 */
public class GameResults {

    String redPlayerName;
    String bluePlayerName;
    String winner;
    int steps;

    /**
     *  Creates a new GameResults class.
     * @param redPlayerName the name of the player with the red pieces
     * @param bluePlayerName the name of the player with the blue pieces
     * @param winner the color of the winner's pieces
     * @param steps the amount of steps needed to win
     */
    public GameResults(String redPlayerName, String bluePlayerName, String winner, int steps) {
        this.redPlayerName = redPlayerName;
        this.bluePlayerName = bluePlayerName;
        this.winner = winner;
        this.steps = steps;
    }

    /**
     * Creates a new, empty, GameResults class.
     */
    public GameResults() {}

    /**
     * @return the name of the player with the red pieces
     */
    public String getRedPlayerName() {
        return redPlayerName;
    }

    /**
     * @return the name of the player with the blue pieces
     */
    public String getBluePlayerName() {
        return bluePlayerName;
    }

    /**
     * @return the color of the winner's pieces
     */
    public String getWinner() {
        return winner;
    }

    /**
     * @return the amount of steps needed to win
     */
    public int getSteps() {
        return steps;
    }
}
