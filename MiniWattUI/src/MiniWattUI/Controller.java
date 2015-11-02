package MiniWattUI;

import com.sun.jnlp.ApiDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.scene.Node;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;

public class Controller
{
    private File questionFile;
    private File sourceFile;

    @FXML protected void ButtonClick_UploadQuestions(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose PDF Question File");
        ExtensionFilter filter = new ExtensionFilter("PDF document (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(filter);
        filter = new ExtensionFilter("Picture", "*.png" , "*.bmp", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(filter);
        questionFile = fileChooser.showOpenDialog(((Node)event.getTarget()).getScene().getWindow());
    }

    @FXML protected void ButtonClick_UploadSource(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose PDF Source File");
        ExtensionFilter filter = new ExtensionFilter("PDF document (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(filter);
        sourceFile =  fileChooser.showOpenDialog(((Node)event.getTarget()).getScene().getWindow());
    }

    @FXML protected void ButtonClick_GetAnswers(ActionEvent event)
    {
        if(questionFile == null)
            return;

        try
        {
            String questionDoc = null;
            String sourceDoc = null;
            String extension = getFileExtension(questionFile);
            if(extension.compareTo("pdf") == 0)
                questionDoc = TextInterpret.parseDocument(questionFile);
            else
                questionDoc = TextInterpret.parseImage(questionFile);

            ArrayList<String> questions = TextInterpret.extractQuestions(questionDoc);

            if(sourceFile != null)
            {
                extension = getFileExtension(sourceFile);
                if(extension.compareTo("pdf") == 0)
                    sourceDoc = TextInterpret.parseDocument(sourceFile);
                else
                    sourceDoc = TextInterpret.parseImage(sourceFile);
            }

            NetworkEngine.post_question(questions, sourceDoc);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }

    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    private static void configureFileChooser(FileChooser fileChooser)
    {
        fileChooser.setInitialDirectory(
          new File(System.getProperty("user.home"))
        );
    }

}
