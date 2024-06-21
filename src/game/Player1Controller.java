package game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Player1Controller implements Initializable {

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

	Player1 p1;
	ImageView[] cards;
	Image[] displayImages;//contains images currently displayed
	Image[] flipImages;//contains images to be displayed when an Image is clicked
	private final int maximumTries = 3;
	private int numberOfGuesses;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.displayImages = new Image[cards.length];
		this.flipImages = new Image[cards.length];
		this.cards = new ImageView[]
				{card1,card2,card3,card4,card5,
						card6,card7,card8,card9,card10,
						card11,card12,card13,card14,card15
				};
		initializeDisplayImages();
		initializeFlipImages();



		try {
			this.p1 = new Player1(new ServerSocket(12345));
		} catch (IOException e) {

			e.printStackTrace();
		}

		msgVBox.setSpacing(20);
		msgVBox.heightProperty().addListener((a,b,newBoxHeight)->{
			scrollPane.setVvalue((Double)newBoxHeight);
		});
		p1.listenForIncomingMessages(msgVBox);
		pickCard();
		
	}


	private void pickCard() {
		ScaleTransition[] transition = animateCards();
		setPickOnClick(transition);
		
	}


	private void initializeFlipImages() {
		for (int i=0;i<flipImages.length;i++) {
			flipImages[i] = new Image(this.getClass().getResourceAsStream("images/flippedCard.png"));
		}	
	}
	private void initializeDisplayImages() {
		for (int i=0;i<this.displayImages.length;i++) {
			this.displayImages[i] = this.cards[i].getImage();
		}

	}
	private void setGuessOnClick(ScaleTransition[] transitions) {
		for (int i=0;i<cards.length;i++) {
			final int finalI = i;
			cards[i].setOnMouseClicked(e->{
				Arrays.stream(transitions).forEach(transition->transition.stop());
				Arrays.stream(cards).forEach(c-> {c.setScaleX(1);c.setScaleY(1);c.setScaleZ(1);});
				
				if (finalI==p1.getFoePickedCardValue()) {
					handleWin();
				}else if (numberOfGuesses++ > maximumTries) {
					handleLoss();
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
				p1.sendMsg("T"+finalI, true);
				this.setFlipOnClick();
			});
		}
		
		
	}
	private void handleLoss() {
		// TODO Auto-generated method stub

	}
	private void handleWin() {
		// TODO Auto-generated method stub

	}
	private ScaleTransition[] animateCards() {
		ScaleTransition[] transition = new ScaleTransition[cards.length];
		for (int i=0;i<cards.length;i++) {
			transition[i]=new ScaleTransition();
			transition[i].setNode(cards[i]);
			transition[i].setByX(0.2);
			transition[i].setByY(0.2);
			transition[i].setByZ(0.2);
			transition[i].setAutoReverse(true);
			transition[i].setCycleCount(Timeline.INDEFINITE);
		}
		return transition;
	}
	public void makeAGuess() {
		ScaleTransition[] transition = animateCards();
		setGuessOnClick(transition);
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
	public void leaveGame(){}
	public void toggleSound() {}
	public void sendMessage() {
		if(!textField.getText().isEmpty()) {
			addMessage(textField.getText(),msgVBox,true);
			p1.sendMsg("F" +textField.getText(),false);
		}
	}

	static void addMessage(String msg,VBox msgBox,boolean sent) {
		String textWrappercss = "-fx-background-radius:50; ";
		Text txt = new Text(msg);
		TextFlow textWrapper = new TextFlow(txt);
		textWrappercss+=sent? "-fx-background-color:#0000FF" : "-fx-background-color:#c0c0c0";
		textWrapper.setStyle(textWrappercss);
		HBox WrapperBox = new HBox(textWrapper);
		WrapperBox.setAlignment(sent?Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
		Platform.runLater(()->{msgBox.getChildren().add(WrapperBox);});

	}


	


	



}
