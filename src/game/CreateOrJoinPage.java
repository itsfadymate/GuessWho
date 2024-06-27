package game;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class CreateOrJoinPage extends StackPane {

	public CreateOrJoinPage(){
		// TODO Auto-generated constructor stub
		try {
		ImageView bgImage  = new ImageView(new Image(getClass().getResourceAsStream("images/lobbyBg.png")));
		bgImage.setPreserveRatio(true);
		bgImage.setFitWidth(1360);
		bgImage.setFitHeight(780);
		this.getChildren().add(bgImage);
		}catch (Exception e) {
			e.printStackTrace();
		}
		Button joinLobby = createButton("Join Lobby");
		Button createLobby = createButton("Create Lobby");
		
		joinLobby.setOnAction(e->{
			Stage s = (Stage)joinLobby.getScene().getWindow();
			s.setScene(new Scene(new JoinLobbyPage()));
		});
		createLobby.setOnAction(e->{
			Stage s = (Stage)joinLobby.getScene().getWindow();
			s.setScene(new Scene(new CreateLobbyPage()));
		});
		
		VBox buttonBox = new VBox(joinLobby,createLobby);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(70);
		this.getChildren().add(buttonBox);
		
	}

	private Button createButton(String string) {
		Button b = new Button(string);
		b.setPrefSize(300, 70);
		b.setFont(Font.font("badoni MT",FontWeight.BOLD,30));
		b.setStyle("-fx-background-color: #E1F000 ;"
				+ " -fx-background-radius:30 ;"
				+ " -fx-background-insets:1;"
				+ " -fx-border-color:#CCCC00 ;"
				+ "	-fx-border-radius: 40 ;"
				+ "-fx-border-width: 7;");
		b.setOnMouseEntered(e->{
			b.setStyle("-fx-background-color: #FFFF00 ;"
					+ " -fx-background-radius:30 ;"
					+ " -fx-background-insets:1;"
					+ " -fx-border-color:#EEEE00 ;"
					+ "	-fx-border-radius: 40 ;"
					+ "-fx-border-width: 7;");
		});
		b.setOnMouseExited(e->{
			b.setStyle("-fx-background-color: #E1F000 ;"
					+ " -fx-background-radius:30 ;"
					+ " -fx-background-insets:1;"
					+ " -fx-border-color:#CCCC00 ;"
					+ "	-fx-border-radius: 40 ;"
					+ "-fx-border-width: 7;");
		});
		return b;
	}

	

}
