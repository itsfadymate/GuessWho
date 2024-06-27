package game;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class LobbyPage extends StackPane {
	private Label userMsg;
	private Button leaveLobby;
	private Button ready;
	private ListView<Label> joinedPlayers;
	private Timeline animation;
	private ArrayList<Label> joinedPlayersList;
	 

	public LobbyPage() {
		
		//ImageView bgImage = new ImageView(new Image(getClass().getResourceAsStream("")));
		//this.getChildren().add(bgImage);
		ImageView titleImg = new ImageView(new Image(getClass().getResourceAsStream("images/lobbyTitle.png")));
		 joinedPlayers = new ListView<Label>();
		
		leaveLobby = createButton("Leave Lobby","#DD0000","#550000","#FF0000","#AA0000");
		ready = createButton("Ready","#00DD00","#005500","#00FF00","#00AA00");
		HBox buttonContainer = new HBox(leaveLobby,ready);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(50);
		
		VBox playerUI = new VBox(joinedPlayers,buttonContainer);
		playerUI.setAlignment(Pos.CENTER);
		playerUI.setSpacing(20);
		playerUI.setPrefWidth(700);
		
		userMsg = new Label();
		setUserMsg("Waiting for Players");
		
		HBox belowTitleContent = new HBox(playerUI,userMsg);
		belowTitleContent.setSpacing(50);
		belowTitleContent.setAlignment(Pos.CENTER_LEFT);
		
		VBox root = new VBox(titleImg,belowTitleContent);
		root.setAlignment(Pos.CENTER);
		VBox.setMargin(titleImg, new Insets(10,0,50,0));
		this.getChildren().add(root);
	
		this.joinedPlayersList = new ArrayList<Label>();

		
	}
	
	private void playAnimatedMsg(Label userMsg) {
		 animation = new Timeline(new KeyFrame(Duration.millis(500),e->{
			String msg = userMsg.getText();
			userMsg.setText( msg + ".");
			if (msg.substring(msg.length()-4).equals("....")) { //check isn't correct fix it mf
				userMsg.setText(msg.substring(0, msg.length()-4));
			}
		}));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
		
		
	}

	private Button createButton(String string,String bgColor , String radiusColor,String onHoverColor,String onHoverBorderColor) {
		Button b = new Button(string);
		setButtonStyles(bgColor, radiusColor, onHoverColor, onHoverBorderColor, b);
		return b;
	}

	private static void setButtonStyles(String bgColor, String radiusColor, String onHoverColor, String onHoverBorderColor,
			Button b) {
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
	}
	
	public Label setUserMsg(String msg) {
		userMsg.setText(msg);
		userMsg.setWrapText(true);
		userMsg.setFont(Font.font("badoni MT",FontWeight.BOLD,50));
		playAnimatedMsg(userMsg);
		return userMsg;
	}

	public void addPlayerToList(String playerName) {
		Label playerLabel = new Label(formatName(playerName));
		playerLabel.setPrefWidth(400);
		playerLabel.setFont(Font.font("badoni MT",FontWeight.BOLD,30));
		
		playerLabel.setGraphic(new Circle(15,Color.RED));
		playerLabel.setGraphicTextGap(10);
		
		this.joinedPlayers.getItems().add(playerLabel);
		this.joinedPlayersList.add(playerLabel);
		
	}
    private String formatName(String playerName) {
		final int maxNameLength = 20;
		return playerName + " ".repeat(maxNameLength-playerName.length());
	}

	public void onLeaveLobbyClicked(EventHandler<? super MouseEvent> e) {
		this.leaveLobby.setOnMouseClicked(e);
	}
	public void setOnReadyClicked(EventHandler<ActionEvent> handler) {
		ready.setOnAction(new EventHandler<ActionEvent>(){
			private static boolean greenButton = true;
			@Override
			public void handle(ActionEvent e) {
				if (greenButton) {
					setButtonStyles("#DD0000","#550000","#FF0000","#AA0000",ready);
					ready.setText("Not Ready");
					greenButton = false;
				}else {
					setButtonStyles("#00DD00","#005500","#00FF00","#00AA00",ready);
					ready.setText("Ready");
					greenButton = true;
			}
				handler.handle(e);
		  }
			
		});
	}
	public void setPlayerReady(String playerName,boolean ready) {
		Label playerL =this.joinedPlayersList.stream().filter(l->l.getText().equals(formatName(playerName))).findFirst().orElse(null);
		if(playerL!=null) {
		((Circle)playerL.getGraphic()).setFill(ready? Color.GREEN : Color.RED);
		}else {
			System.out.println("should probably throw exception buddy. We can't find player in list view with matching name");
		}
	}
	public ArrayList<Label> getJoinedPlayersList(){return this.joinedPlayersList;}


}
