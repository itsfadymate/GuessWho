package game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
//TODO:validate picking card process to avoid loopholes

public class PlayerController {
	

	@FXML
	ImageView card1;
	@FXML
	ImageView card2;
	@FXML
	ImageView card3;
	@FXML
	ImageView card4;
	@FXML
	ImageView card5;
	@FXML
	ImageView card6;
	@FXML
	ImageView card7;
	@FXML
	ImageView card8;
	@FXML
	ImageView card9;
	@FXML
	ImageView card10;
	@FXML
	ImageView card11;
	@FXML
	ImageView card12;
	@FXML
	ImageView card13;
	@FXML
	ImageView card14;
	@FXML
	ImageView card15;
	@FXML
	ScrollPane scrollPane;
	@FXML
	VBox msgVBox;
	@FXML
	TextField textField;
	@FXML
	ImageView pickedCard;
	@FXML 
	GridPane gridPane;
	@FXML
	Rectangle cardsDimmer;
	@FXML
	Rectangle chatHider;
	@FXML
	Button muteButton;
	@FXML
	ImageView cardPointer;
	@FXML
	Button sendButton;
	@FXML
	AnchorPane root;
	@FXML 
	ImageView instructorWoman;
	@FXML
	StackPane popUp;
	@FXML
	Label endMsg;
	@FXML
	HBox gameContainer;
	

	private Player p1;
	private ImageView[] cards;
	private Image[] displayImages;//contains images currently displayed
	private Image[] flipImages;//contains images to be displayed when an Image is clicked
	private final int maximumTries = 3;
	private int numberOfGuesses;
	private boolean isGuessing;
	private boolean isMuted;
	private ScaleTransition[] transitions;
	private TranslateTransition animatePointer;
	
	
	public void initialize(Socket socket) {
		this.textField.addEventFilter(KeyEvent.KEY_PRESSED, e->{
			if (e.getCode()==KeyCode.ENTER) {
				sendMessage();
			}
		});
		this.isGuessing = false;
		this.isMuted = false;
		this.cards = new ImageView[]
				{card1,card2,card3,card4,card5,
						card6,card7,card8,card9,card10,
						card11,card12,card13,card14,card15
				};
		this.displayImages = new Image[cards.length];
		this.flipImages = new Image[cards.length];
		
		initializeDisplayImages();
		initializeFlipImages();
		
		
		this.p1 = new Player(socket);
		msgVBox.setPadding(new Insets(10,10,10,10));
		msgVBox.setSpacing(20);
		msgVBox.heightProperty().addListener((a,b,newBoxHeight)->{
			scrollPane.setVvalue((Double)newBoxHeight);
		});
		p1.listenForIncomingMessages(msgVBox,this.endMsg,this.popUp,this.gameContainer);
		pickCard();
		
	}


	private void pickCard() {
		playExplanationAnimation();
		ScaleTransition[] transition = animateCards();
		setPickOnClick(transition);
		
	}
	private void playExplanationAnimation() {
		//TODO: should add an image of a woman telling you to pick a card
		this.instructorWoman.setVisible(true);
		this.instructorWoman.setLayoutX(950);
		this.instructorWoman.setLayoutY(300);
		this.chatHider.setVisible(true);
		this.chatHider.setOpacity(0.58);
		this.chatHider.setOpacity(0.58);
		this.sendButton.setDisable(true);
		this.cardPointer.setVisible(true);
		this.cardPointer.setLayoutX(870);
		this.cardPointer.setLayoutY(500);
	    animatePointer = new TranslateTransition();
		animatePointer.setNode(this.cardPointer);
		animatePointer.setByX(-50);
		animatePointer.setByY(-50);
		animatePointer.setDuration(Duration.millis(300));
		animatePointer.setAutoReverse(true);
		animatePointer.setCycleCount(TranslateTransition.INDEFINITE);
		animatePointer.play();
		
	}

	private void initializeFlipImages() {
		for (int i=0;i<flipImages.length;i++) {
			flipImages[i] = new Image(this.getClass().getResourceAsStream("images/flippedCard.png"));
		}	
	}
	//TODO: test leave button, test guessing functionality
	private void initializeDisplayImages() {
		for (int i=0;i<this.displayImages.length;i++) {
			this.displayImages[i] = this.cards[i].getImage();
		}

	}
	private void setGuessOnClick(ScaleTransition[] transitions) {
		for (int i=0;i<cards.length;i++) {
			final int finalI = i;
			cards[i].setOnMouseClicked(e->{
				System.out.println("guessed card number: " + finalI + " number of guesses is "+ (numberOfGuesses+1));
				Arrays.stream(transitions).forEach(transition->transition.stop());
				Arrays.stream(cards).forEach(c-> {c.setScaleX(1);c.setScaleY(1);c.setScaleZ(1);});
				
				if (finalI==p1.getFoePickedCardValue()) {
					System.out.println("guessed correctly");
					handleWin();
				}else if (numberOfGuesses++ > maximumTries) {
					System.out.println("exceeded number of guesses");
					handleLoss("you exceeded the number of allowed tries.\n You lost!!",this.endMsg,this.popUp,this.gameContainer);
					return;
				}
				setFlipOnClick();
			}
					);
		}
	}
	private void setFlipOnClick() {
		Arrays.stream(cards).forEach(card->card.setOnMouseClicked(e->{
			for (int i=0;i<cards.length;i++) {//find card index and flip it
				if (card==cards[i]) {
					displayImages[i]=flipImages[i];
					flipImages[i]=cards[i].getImage();
					cards[i].setImage(displayImages[i]);
				}
			}
		}));
	}
	private void setPickOnClick(ScaleTransition[] transitions) {
		for (int i=0;i<cards.length;i++) {
			final int finalI = i;
			cards[i].setOnMouseClicked(e->{
				Arrays.stream(transitions).forEach(transition->transition.stop());
				Arrays.stream(cards).forEach(c-> {c.setScaleX(1);c.setScaleY(1);c.setScaleZ(1);});
				p1.sendMsg(""+finalI, MsgType.cardInfo);
				System.out.println("picked card: " + finalI);
				this.pickedCard.setImage(cards[finalI].getImage());
				this.animatePointer.stop();
				this.instructorWoman.setVisible(false);
				this.cardPointer.setVisible(false);
				this.chatHider.setVisible(false);
				this.sendButton.setDisable(false);
				this.setFlipOnClick();
			});
		}
		
		
	}
	public static void handleLoss(String msg,Label endMsg,StackPane popUp,HBox gameContainer) {
		endMsg.setText(msg);
		popUp.setVisible(true);
		gameContainer.setEffect(new GaussianBlur());
		

	}
	public static void handleOpponentleft(String msg) {
		System.out.println("opponent left");
		Alert a = new Alert(AlertType.ERROR);
		a.setContentText(msg);
		a.setTitle("opponent left");
		a.show();
	}
    private void handleWin() {
		popUp.setVisible(true);
		this.gameContainer.setEffect(new GaussianBlur());
		this.p1.sendMsg("", MsgType.playerWon);

	}
	private ScaleTransition[] animateCards() {
		System.out.println("animating cards");
		ScaleTransition[] transition = new ScaleTransition[cards.length];
		for (int i=0;i<cards.length;i++) {
			transition[i]=new ScaleTransition();
			transition[i].setNode(cards[i]);
			transition[i].setByX(-0.3);
			transition[i].setByY(-0.3);
			transition[i].setByZ(-0.3);
			transition[i].setAutoReverse(true);
			transition[i].setCycleCount(Timeline.INDEFINITE);
			transition[i].play();
		}
		return transition;
	}
	public void makeAGuess() {
		
		if (isGuessing) {
			if (transitions!=null)
				Arrays.stream(transitions).forEach(transition->transition.stop());
			Arrays.stream(cards).forEach(c-> {c.setScaleX(1);c.setScaleY(1);c.setScaleZ(1);});
			setFlipOnClick();
		}else {
			 transitions = animateCards();
			setGuessOnClick(transitions);
		}
		this.isGuessing=!this.isGuessing;
	}
	
	private static boolean showingPickedCard = false;
	public void showPickedCard(){
		if (showingPickedCard) {
			this.pickedCard.setVisible(false);
			this.gridPane.setVisible(true);
			showingPickedCard = false;
		}else{
			this.pickedCard.setVisible(true);
			this.gridPane.setVisible(false);
			showingPickedCard = true;
		}
	}
	public void leaveGame(ActionEvent e){
		
		try {
			FXMLLoader loader = new FXMLLoader(PlayerController.class.getResource("mainMenu.fxml"));
			Parent root = loader.load();
		   Stage s = (Stage)(((Button)e.getSource()).getScene().getWindow());
		   s.setScene(new Scene(root));
		   p1.sendMsg("opponent left the game",MsgType.playerDisconnected );
		}catch (Exception exc) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("there was a problem switching to main menu pls exit the game and reopen");
			a.setTitle("Problemo");
			a.show();
			exc.printStackTrace();
		}
	}
	public void toggleSound() {
		this.isMuted =!this.isMuted;
		if (isMuted) {
			((ImageView)this.muteButton.getGraphic()).setImage(new Image(getClass().getResourceAsStream("images/muteIcon.png")));
		}else {
			((ImageView)this.muteButton.getGraphic()).setImage(new Image(getClass().getResourceAsStream("images/soundIcon.png")));
		}
		
	}
	
	public void sendMessage() {
		if(!textField.getText().isEmpty()) {
			String msg = textField.getText();
			addMessage(msg,msgVBox,true);
			this.textField.setText("");
			p1.sendMsg(msg,MsgType.chatMsg);
		
		}
	}

	static void addMessage(String msg,VBox msgBox,boolean sent) {
		String textWrappercss = "-fx-background-radius:50; ";
		Text txt = new Text(msg);
		txt.setFill(Color.BLACK);
		txt.setFont(Font.font("Arial",FontWeight.NORMAL,30));
		TextFlow textWrapper = new TextFlow(txt);
		textWrapper.setPadding(new Insets(30,30,30,30));
		textWrappercss+=sent? "-fx-background-color:#0000FF" : "-fx-background-color:#c0c0c0";
		textWrapper.setStyle(textWrappercss);
		HBox WrapperBox = new HBox(textWrapper);
		WrapperBox.setAlignment(sent?Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
		Platform.runLater(()->{msgBox.getChildren().add(WrapperBox);});

	}




	



}
