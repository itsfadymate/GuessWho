package game;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Launcher extends Application {
 public static void main(String[] args) {
	  Application.launch(args);
}

@Override
public void start(Stage stage) throws Exception {
	// TODO Auto-generated method stub
	Parent root = new BorderPane();
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/mainMenu.fxml"));
		root = loader.load();
	}catch (Exception e) {
		e.printStackTrace();
	}
	stage.setScene(new Scene(root));
	stage.setHeight(780);
	stage.setWidth(1360);
	//stage.setFullScreen(true);
	stage.show();
}
}
