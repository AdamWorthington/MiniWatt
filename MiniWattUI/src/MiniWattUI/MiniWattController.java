package MiniWattUI;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Chris Doak on 10/21/2015.
 * A controller for the miniwatt_main fxml.
 */
public class MiniWattController implements Initializable {

    private static final String[] VALID_FILE_EXTENSIONS = {"pdf","bmp","jpg","jpeg","png"};

    private Stage primaryStage;

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
    ObservableList<String> historyList;
    Map<String, ArrayList<MiniWattResult>> historyMap;

    @FXML VBox resultsVBox;
    @FXML TextArea resultsTextArea;
    private boolean resultsShowing;

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
        historyList = FXCollections.observableArrayList();
        historyMap = new HashMap<String, ArrayList<MiniWattResult>>();
        historyListView.setItems(historyList);

        historyListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                showResults(historyMap.get(newValue));
            }
        });

        mainHBox.getChildren().remove(resultsVBox);
        resultsShowing = false;

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

        final SubmitStatusDialog statusDialog = new SubmitStatusDialog(primaryStage);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String questionSetTitle;
                ArrayList<String> questions;
                String questionsDoc;
                String referenceDoc;

                if (questionsToggle.getSelectedToggle().equals(questionsAsTextButton)) {
                    if (questionsTextArea.getText().isEmpty()) {
                        questionsStatusLabel.setText("Please provide questions as text!");
                        submitButton.setDisable(false);
                        statusDialog.close();
                        return;
                    }
                    statusDialog.setText("Parsing questions text...");
                    questionsDoc = questionsTextArea.getText();
                    questionSetTitle = questionsDoc.split("\n")[0];

                } else {
                    if (questionsFile == null) {
                        questionsStatusLabel.setText("Please provide a questions file!");
                        submitButton.setDisable(false);
                        statusDialog.close();
                        return;
                    }
                    statusDialog.setText("Parsing questions document...");
                    questionsDoc = parseFile(questionsFile);
                    questionSetTitle = questionsFile.getName();
                }

                if (questionsDoc != null) {
                    statusDialog.setText("Extracting questions...");
                    questions = TextInterpret.extractQuestions(questionsDoc);
                } else {
                    questionsStatusLabel.setText("Error with questioins.");
                    submitButton.setDisable(false);
                    statusDialog.close();
                    return;
                }

                if (referenceToggle.getSelectedToggle().equals(referenceAsTextButton)) {
                    if (referenceTextArea.getText().isEmpty()) {
                        referenceStatusLabel.setText("Please provide reference as text!");
                        submitButton.setDisable(false);
                        statusDialog.close();
                        return;
                    }
                    statusDialog.setText("Parsing reference text...");
                    referenceDoc = referenceTextArea.getText();
                } else if (referenceToggle.getSelectedToggle().equals(referenceFromFileButton)) {
                    if (referenceFile == null) {
                        referenceStatusLabel.setText("Please provide a reference file!");
                        submitButton.setDisable(false);
                        statusDialog.close();
                        return;
                    }
                    statusDialog.setText("Parsing reference document...");
                    referenceDoc = parseFile(referenceFile);
                    if (referenceDoc == null) {
                        referenceStatusLabel.setText("Error with reference.");
                    }
                } else {
                    referenceDoc = null;
                }

                if (questions.isEmpty()) {
                    questionsStatusLabel.setText("No questions found.");
                    submitButton.setDisable(false);
                    statusDialog.close();
                    return;
                }

                statusDialog.setText("Sending data to MiniWatt server...");
                ArrayList<MiniWattResult> res;
                try {
                    res = NetworkEngine.post_question(questions, referenceDoc);
                } catch (Exception e) {
                    submitStatusLabel.setText("Error with network.");
                    e.printStackTrace();
                    statusDialog.close();
                    submitButton.setDisable(false);
                    return;
                }
                statusDialog.close();
                historyMap.put(questionSetTitle + "...", res);
                historyList.add(questionSetTitle + "...");
                showResults(res);
            }
        });
    }

    @FXML void onResultsCloseClicked() {
        submitButton.setDisable(false);
        mainHBox.getChildren().remove(resultsVBox);
        mainHBox.getChildren().add(0, mainScrollPane);
        resultsShowing = false;
        historyListView.getSelectionModel().select(null);
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

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    // Shows the results of the query in the results panel.
    private void showResults(ArrayList<MiniWattResult> results) {
        if (!resultsShowing) {
            mainHBox.getChildren().remove(mainScrollPane);
            mainHBox.getChildren().add(0, resultsVBox);
        }

        StringBuilder builder = new StringBuilder();

        for(MiniWattResult mwr : results)
        {
            builder.append("Question: ");
            builder.append(mwr.getQuestion().getQuestionText());
            builder.append("\n");
            List<ImmutablePair<String, Integer>> data = mwr.getResults();
            for(ImmutablePair<String, Integer> ip : data)
            {
                builder.append("Answer: ");
                builder.append(ip.getLeft());
                builder.append("\n");
                builder.append("Confidence: ");
                builder.append(ip.getRight());
                builder.append("\n");
            }
        }

        resultsShowing = true;
        resultsTextArea.setText(builder.toString());
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

    private String parseFile(File document) {
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
