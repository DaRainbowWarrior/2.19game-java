package twonineteengame.util;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

/**
 * Helper class providing a ready-to-use, configured loading/saving {@code FileChooser}.
 */
public class FileHelper {
    /**
     *  Opens up a dialog for saving / loading JSON files only.
     * @param forOpen {@code true} for open dialog; {@code false} for save dialog
     * @param stage the window in which the {@code FileChooser} dialog needs to open
     * @return optional of read / saved JSON file
     */
    public static Optional<File> showDialog(boolean forOpen, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json documents", "*.json"));
        if (forOpen) {
            fileChooser.setTitle("Choose JSON file to read scoreboard");
            return Optional.ofNullable(fileChooser.showOpenDialog(stage));
        } else {
            fileChooser.setTitle("Choose place to save JSON scoreboard file");
            return Optional.ofNullable(fileChooser.showSaveDialog(stage));
        }
    }
}
