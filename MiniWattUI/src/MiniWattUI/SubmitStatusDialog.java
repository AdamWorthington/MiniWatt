package MiniWattUI;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by cdoak_000 on 10/28/2015.
 * A class that launches and contacts a SubmitStatusDialog
 */
public class SubmitStatusDialog {
    private SubmitStatusDialogController controller;
    private Stage stage;

    public SubmitStatusDialog(Stage primaryStage) {
        stage = new Stage();
        stage.getIcons().add(primaryStage.getIcons().get(0));

        Parent root;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("submit_status_dialog.fxml"));
        try {
            root = loader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller = loader.getController();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public void close() {
        stage.close();
    }

    public void setText(final String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.setStatusLabelText(text);
            }
        });
    }

    public String getText() {
        return controller.getStatusLabelText();
    }
}
