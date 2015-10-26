package MiniWattUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("miniwatt_main.fxml"));
        primaryStage.getIcons().add(
                new Image(getClass().getClassLoader().getResourceAsStream("MiniWattUI/res/ic_miniwatt_56px.png")));
        primaryStage.setTitle("Mini Watt");
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(400);
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
