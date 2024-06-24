package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tester extends Application {



	@Override
	public void start(Stage s) throws Exception {
		// TODO Auto-generated method stub
		s.setScene(new Scene(new LobbyPage()));
		s.setHeight(780);
		s.setWidth(1360);
		s.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
