package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Tester extends Application {

	boolean ready = false;

	@Override
	public void start(Stage s) throws Exception {
		// TODO Auto-generated method stub
		try{
		/*	 ready = true;
		LobbyPage root = new LobbyPage();
		root.addPlayerToList("Fady");
		root.addPlayerToList("Daniel");
		root.setOnReadyClicked(e->{root.setPlayerReady("Fady", ready);
		this.ready = !ready;
		});*/
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("player.fxml"));
		
		s.setScene(new Scene(loader.load()));
		s.setHeight(780);
		s.setWidth(1360);
		s.show();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
