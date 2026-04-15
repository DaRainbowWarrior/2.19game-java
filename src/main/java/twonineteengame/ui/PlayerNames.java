package twonineteengame.ui;

/**
 * Helper class for transferring players entered names between the main menu and game screen.
 */
public class PlayerNames {
    String bluePlayerName;
    String redPlayerName;

    /**
     * Creates a new PlayerNames class.
     * @param redPlayerName the name of the player with the red pieces
     * @param bluePlayerName the name of the player with the blue pieces
     */
    public PlayerNames(String redPlayerName, String bluePlayerName) {
        this.bluePlayerName = bluePlayerName;
        this.redPlayerName = redPlayerName;
    }

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
}
