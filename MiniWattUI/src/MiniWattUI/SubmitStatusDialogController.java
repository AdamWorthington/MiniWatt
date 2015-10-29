package MiniWattUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by cdoak_000 on 10/28/2015.
 * Controller for the submit status dialog.
 */
public class SubmitStatusDialogController {
    @FXML Label submitStatusLabel;

    public void setStatusLabelText(String text) {
        submitStatusLabel.setText(text);
    }

    public String getStatusLabelText() {
        return submitStatusLabel.getText();
    }
}
