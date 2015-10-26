package MiniWattUI;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Queue;
import java.util.ResourceBundle;

/**
 * Created by Chris Doak on 10/21/2015.
 * A controller for the miniwatt_main fxml.
 */
public class MiniWattController implements Initializable {

    private static final String[] VALID_FILE_EXTENSIONS = {"pdf","bmp","jpg","jpeg","png"};

    private File questionsFile = null;
    private File referenceFile = null;
    private String lastFilePath = null; // The directory of the last chosen file.

    private ToggleGroup questionsToggle;
    private ToggleGroup referenceToggle;

    @FXML ToggleButton historyButton;
    @FXML Button submitButton;
    @FXML Label submitStatusLabel;

    @FXML HBox mainHBox;
    @FXML ScrollPane mainScrollPane;

    @FXML VBox questionsVBox;
    @FXML Label questionsStatusLabel;
    @FXML ToggleButton questionsAsTextButton;
    @FXML ToggleButton questionsFromFileButton;
    @FXML TextArea questionsTextArea;
    @FXML Label questionsFileText;
    @FXML Button questionsFileBrowseButton;
    @FXML Button questionsFileClearButton;

    @FXML VBox referenceVBox;
    @FXML Label referenceStatusLabel;
    @FXML ToggleButton referenceAsTextButton;
    @FXML ToggleButton referenceFromFileButton;
    @FXML ToggleButton referenceAsNullButton;
    @FXML TextArea referenceTextArea;
    @FXML Label referenceFileText;
    @FXML Button referenceFileBrowseButton;
    @FXML Button referenceFileClearButton;

    @FXML ListView historyListView;
    @FXML VBox historyVBox;
    private boolean historyShowing;

    @FXML VBox resultsVBox;
    @FXML TextArea resultsTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionsToggle = new ToggleGroup();
        referenceToggle = new ToggleGroup();

        questionsAsTextButton.fire();
        questionsAsTextButton.setToggleGroup(questionsToggle);
        questionsFromFileButton.setToggleGroup(questionsToggle);

        referenceAsNullButton.fire();
        referenceFromFileButton.setToggleGroup(referenceToggle);
        referenceAsTextButton.setToggleGroup(referenceToggle);
        referenceAsNullButton.setToggleGroup(referenceToggle);

        mainHBox.getChildren().remove(historyVBox);
        historyShowing = false;

        mainHBox.getChildren().remove(resultsVBox);

        questionsToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            public void changed(ObservableValue<? extends Toggle> observable,
                                final Toggle oldValue, final Toggle newValue) {
                if ((newValue == null)) {
                    Platform.runLater(new Runnable() {

                        public void run() {
                            questionsToggle.selectToggle(oldValue);
                        }
                    });
                }
            }
        });

        referenceToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            public void changed(ObservableValue<? extends Toggle> observable,
                                final Toggle oldValue, final Toggle newValue) {
                if ((newValue == null)) {
                    Platform.runLater(new Runnable() {

                        public void run() {
                            referenceToggle.selectToggle(oldValue);
                        }
                    });
                }
            }
        });
    }

    @FXML void onSubmitButtonClicked() {
        resetStatusLabels();
        submitButton.setDisable(true);

        Queue<String> questions;
        String[] questionsDoc;
        String[] referenceDoc;

        if (questionsToggle.getSelectedToggle().equals(questionsAsTextButton)) {
            if (questionsTextArea.getText().isEmpty()) {
                questionsStatusLabel.setText("Please provide questions as text!");
                submitButton.setDisable(false);
                return;
            }
            questionsDoc = questionsTextArea.getText().split("\n");

        } else {
            if (questionsFile == null) {
                questionsStatusLabel.setText("Please provide a questions file!");
                submitButton.setDisable(false);
                return;
            }
            questionsDoc = parseFile(questionsFile);
        }

        if (questionsDoc != null) {
            questions = TextInterpret.extractQuestions(questionsDoc);
        } else {
            questionsStatusLabel.setText("Error with questioins.");
            submitButton.setDisable(false);
            return;
        }

        if (referenceToggle.getSelectedToggle().equals(referenceAsTextButton)) {
            if (referenceTextArea.getText().isEmpty()) {
                referenceStatusLabel.setText("Please provide reference as text!");
                submitButton.setDisable(false);
                return;
            }
            referenceDoc = referenceTextArea.getText().split("\n");
        } else if (referenceToggle.getSelectedToggle().equals(referenceFromFileButton)) {
            if (referenceFile == null) {
                referenceStatusLabel.setText("Please provide a reference file!");
                submitButton.setDisable(false);
                return;
            }
            referenceDoc = parseFile(referenceFile);
            if (referenceDoc == null) {
                referenceStatusLabel.setText("Error with reference.");
            }
        } else {
            referenceDoc = null;
        }

        try {
            NetworkEngine.post_question(questions, referenceDoc);
        } catch (Exception e) {
            submitStatusLabel.setText("Error with network.");
            e.printStackTrace();
        }
        showResults("Results will be shown here when hooked up to the network engine properly.");

    }

    @FXML void onResultsCloseClicked() {
        submitButton.setDisable(false);
        mainHBox.getChildren().remove(resultsVBox);
        mainHBox.getChildren().add(0, mainScrollPane);
    }

    @FXML void onHistoryButtonClicked() {
        if (historyShowing) {
            mainHBox.getChildren().remove(historyVBox);
            historyShowing = false;
        } else {
            mainHBox.getChildren().add(historyVBox);
            historyShowing = true;
        }
    }

    @FXML void onQuestionFileBrowseClicked(ActionEvent event) {
        File qFile = browseFile(event);
        if (qFile != null) {
            questionsFile = qFile;
            questionsFileText.setText(questionsFile.getName());
            questionsFromFileButton.fire();
        }
    }

    @FXML void onQuestionFileClearClicked() {
        questionsFile = null;
        questionsFileText.setText("no file chosen");
        questionsAsTextButton.fire();
    }

    @FXML void onReferenceFileBrowseClicked(ActionEvent event) {
        referenceFile = browseFile(event);
        if (referenceFile != null) {
            referenceFileText.setText(referenceFile.getName());
            referenceFromFileButton.fire();
        } else {
            referenceFileText.setText("no file chosen");
        }
    }

    @FXML void onReferenceFileClearClicked() {
        referenceFile = null;
        referenceFileText.setText("no file chosen");
        referenceAsNullButton.fire();
    }

    // Shows the results of the query in the results panel.
    private void showResults(String results) {
        mainHBox.getChildren().remove(mainScrollPane);
        mainHBox.getChildren().add(0, resultsVBox);
        resultsTextArea.setText(results);
    }

    private File browseFile(ActionEvent event) {
        FileChooser browse = new FileChooser();

        ExtensionFilter pdfFilter = new ExtensionFilter("PDF Document (*.pdf)","*.pdf");
        ExtensionFilter pictureFilter =
                new ExtensionFilter("Picture", "*.bmp", "*.jpg", "*.jpeg", "*.png");

        browse.getExtensionFilters().addAll(pdfFilter, pictureFilter);

        String path;
        if (lastFilePath == null) {
            path = System.getProperty("user.home");
        } else {
            path = lastFilePath;
        }

        browse.setInitialDirectory(new File(path));

        File file = browse.showOpenDialog(((Node)event.getTarget()).getScene().getWindow());
        if (file != null) lastFilePath = file.getParent();
        return file;
    }

    private String bool2String(boolean bool) {
        return (new Boolean(bool)).toString();
    }

    private void resetStatusLabels() {
        submitStatusLabel.setText("");
        questionsStatusLabel.setText("");
        referenceStatusLabel.setText("");
    }

    private String[] parseFile(File document) {
        if (FilenameUtils.isExtension(document.getName(), VALID_FILE_EXTENSIONS)) {
            try {
                if (FilenameUtils.isExtension(document.getName(), "pdf")) {
                    return TextInterpret.parseDocument(document);
                } else {
                    return TextInterpret.parseImage(document);
                }
            } catch (Exception e) {
                submitStatusLabel.setText("Error parsing: " + document.getName());
            }
        }
        return null;
    }

}
