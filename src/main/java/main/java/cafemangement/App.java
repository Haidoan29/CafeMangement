package main.java.cafemangement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        try {
			
			Parent root = FXMLLoader.load(getClass().getResource("/main/resources/FXML/login.fxml"));
			stage.setTitle("Quản lý quán café - Cafe Mangement");
	        stage.getIcons().add(new Image(getClass().getResourceAsStream( "/main/resources/img/app-icon.png" )));
	        
	        Scene scene = new Scene(root);   
	        stage.setScene(scene);
	        stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    

    public static void main(String[] args) {
        launch();
    }

}