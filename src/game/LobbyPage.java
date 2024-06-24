package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class LobbyPage extends StackPane {
	private Label userMsg;
	private Button leaveLobby;
	private Button ready;
	private ListView<HBox> joinedPlayers;

	public LobbyPage() {
		
		//ImageView bgImage = new ImageView(new Image(getClass().getResourceAsStream("")));
		//this.getChildren().add(bgImage);
		ImageView titleImg = new ImageView(new Image(getClass().getResourceAsStream("images/lobbyTitle.png")));
		 joinedPlayers = new ListView<HBox>();
		
		leaveLobby = createButton("Leave Lobby","#DD0000","#550000","#FF0000","#AA0000");
		ready = createButton("Ready","#00DD00","#005500","#00FF00","#00AA00");
		HBox buttonContainer = new HBox(leaveLobby,ready);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(50);
		
		VBox playerUI = new VBox(joinedPlayers,buttonContainer);
		playerUI.setAlignment(Pos.CENTER);
		playerUI.setPrefWidth(700);
		
	
		userMsg = new Label("Waiting for Players");
		userMsg.setWrapText(true);
		userMsg.setFont(Font.font("badoni MT",FontWeight.BOLD,50));
		playAnimatedMsg(userMsg);
		
		HBox belowTitleContent = new HBox(playerUI,userMsg);
		belowTitleContent.setSpacing(50);
		
		VBox root = new VBox(titleImg,belowTitleContent);
		root.setAlignment(Pos.CENTER);
		VBox.setMargin(titleImg, new Insets(10,0,50,0));
		this.getChildren().add(root);
		
	}
	
	private void playAnimatedMsg(Label userMsg) {
		Timeline animation = new Timeline(new KeyFrame(Duration.millis(500),e->{
			String msg = userMsg.getText();
			userMsg.setText( msg + ".");
			if (msg.substring(msg.length()-4).equals("...")) { //check isn't correct fix it mf
				userMsg.setText(msg.substring(0, msg.length()-4));
			}
		}));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
		//likely should be global to controll when to stop it
		
	}

	private Button createButton(String string,String bgColor , String radiusColor,String onHoverColor,String onHoverBorderColor) {
		Button b = new Button(string);
		b.setPrefSize(300, 70);
		b.setFont(Font.font("badoni MT",FontWeight.BOLD,30));
		b.setStyle("-fx-background-color:" + bgColor +";"
				+ " -fx-background-radius:30 ;"
				+ " -fx-background-insets:2;"
				+ " -fx-border-color:" + radiusColor + ";"
				+ "	-fx-border-radius: 40 ;"
				+ "-fx-border-width: 7;");
		b.setOnMouseEntered(e->{
			b.setStyle("-fx-background-color:" + onHoverColor +";"
					+ " -fx-background-radius:30 ;"
					+ " -fx-background-insets:2;"
					+ " -fx-border-color:" + onHoverBorderColor + ";"
					+ "	-fx-border-radius: 40 ;"
					+ "-fx-border-width: 7;");
		});
		b.setOnMouseExited(e->{
			b.setStyle("-fx-background-color:" + bgColor +";"
					+ " -fx-background-radius:30 ;"
					+ " -fx-background-insets:2;"
					+ " -fx-border-color:" + radiusColor + ";"
					+ "	-fx-border-radius: 40 ;"
					+ "-fx-border-width: 7;");
		});
		return b;
	}
	
	public void setUserMsg(String msg) {
		userMsg.setText(msg);
	}
	public void addPlayerToList(String playerName) {
	//TODO:Probably will have to use a custom ListView dataStructure
		
		
	}
public void onLeaveLeaveLobbyClicked(EventHandler<? super MouseEvent> e) {
		this.leaveLobby.setOnMouseClicked(e);
	}
	public void onReadyClicked(EventHandler<? super MouseEvent> e) {
		this.leaveLobby.setOnMouseClicked( e);
	}



}
