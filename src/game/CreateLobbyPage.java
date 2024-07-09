package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CreateLobbyPage extends StackPane {
	private static final String BG_IMAGE_PATH = "images/questionMarkTexture1.png";
	private LobbyPage lobbyView;
	private boolean isHostReady;
	private boolean isPlayer2Ready;
	private BufferedReader reader;
	private BufferedWriter writer;
	private Socket socket;
	private boolean continueChecking;
	private TextField nickNameInput;
	public CreateLobbyPage() {
		
		//Related to thread reading host status in method waitTillBothPlayersReady
		this.continueChecking = true;
		this.isHostReady = false;
		
		//backgroundImage
		/*Image bgImage = new Image(this.getClass().getResourceAsStream("images/lobbyBg.png"));
		ImageView bgView = new ImageView(bgImage);
		bgView.setPreserveRatio(true);
		bgView.setFitWidth(1360);
		this.getChildren().add(bgView);*/
		Image bgImage = new Image(getClass().getResourceAsStream(BG_IMAGE_PATH));
		BackgroundImage bgI = new BackgroundImage(bgImage,
				BackgroundRepeat.REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT);
		Background bg = new Background(bgI);
		this.setBackground(bg);
		
		//Title Image
		Image titleImage = new Image(this.getClass().getResourceAsStream("images/createLobbyTitle.png"));
		ImageView titleView = new ImageView(titleImage);
		StackPane titleContainer = new StackPane(titleView);
		titleContainer.setAlignment(Pos.CENTER);
		StackPane.setMargin(titleView, new Insets(0,0,60,00));
		
		//label & textField
		//TODO: missing validation as nickName cannot be empty nor greater than 20 char
		Label textFieldLabel = createLabel("Nickname: ");
		 nickNameInput = createTextField("Nickname: ");
		HBox inputContainer = new HBox(textFieldLabel,nickNameInput);
		inputContainer.setAlignment(Pos.CENTER);
		inputContainer.setSpacing(70);
		
		
		//Creating the lobby
		Button createLobby = createButton("Create","#E1F000","#CCCC00","#FFFF00","#EEEE00");
		createLobby.setOnAction(e->{
			
			this.lobbyView = new LobbyPage();
			lobbyView.addPlayerToList(nickNameInput.getText());
			lobbyView.onLeaveLobbyClicked(l->{ //wholy unfinished and need to be implemented in joinLobby
				Stage s = (Stage)createLobby.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
				try {
					s.setScene(loader.load());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			
			Stage stage = (Stage)createLobby.getScene().getWindow();
			stage.setScene(new Scene(lobbyView));
			
			try {
				System.out.println("awaiting connection");
				ServerSocket ss = new ServerSocket(12345);
				this.socket = ss.accept();
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				System.out.println("connection established");
				String player2Name = reader.readLine();
				lobbyView.addPlayerToList(player2Name);
				System.out.println("sending Host Name to clients");
				writer.write(nickNameInput.getText()+"\n");
				writer.flush();
				System.out.println("awaiting mutual ready status...");
				waitTillBothPlayersReady();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
		
		
		
		VBox uiContainer = new VBox(titleContainer,inputContainer,createLobby);
		uiContainer.setAlignment(Pos.CENTER);
		uiContainer.setSpacing(100);
		
		this.getChildren().add(uiContainer);
	}
	private void waitTillBothPlayersReady() {
		//continuously read Host status
		
		Thread handleGameStatus = new Thread(()->{
			long counter = 0;
			while (this.socket.isConnected() && continueChecking ) {
				counter++;
				try {
					exchangeReadyStatus();
					/*
					 * Bug: gameView is displayed only when host is ready first then the client
					 * Fix: exchangeReadyStatus in a reversed order so it works in any order
					 * I need to find a better solution tho and why this happens in the first place
					 */
					exchangeReadyStatusReverseOrder();
					//checks do we start game?
					if (isHostReady && isPlayer2Ready) {
						continueChecking = false;
						
						
						Platform.runLater(()->{
						this.lobbyView.setUserMsg("Starting game");
						//startGame here	
							FXMLLoader loader = new FXMLLoader(getClass().getResource("player.fxml"));
							Parent root;
							try {
								root = loader.load();
								PlayerController controller = loader.getController();
								controller.initialize(this.socket);
								Stage stage =(Stage)lobbyView.getScene().getWindow();
								stage.setScene(new Scene(root));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
					}
				} catch (IOException e) {
					System.out.println("--------------------" + counter);
					e.printStackTrace();
					break;
				}
			}
		});
		handleGameStatus.start();
		
		
		lobbyView.setOnReadyClicked(e->{
			this.isHostReady=!isHostReady;
			lobbyView.setPlayerReady(this.nickNameInput.getText(), this.isHostReady);
		});
	}
	private void exchangeReadyStatus() throws IOException {
		//send this player ready status
		writer.write(isHostReady+"\n");
		writer.flush();
		
		
		//check do we change client player status
		boolean oldValue = this.isPlayer2Ready;
		String msg = reader.readLine();
		if (msg!=null)
			this.isPlayer2Ready = msg.equals("true")? true: msg.equals("false")? false : this.isPlayer2Ready;
		if (oldValue!=this.isPlayer2Ready) {
			System.out.println(this.isPlayer2Ready? "client is now ready" : "client is now not ready");
			Platform.runLater(()->{
				this.lobbyView.setPlayerReady(this.lobbyView.getJoinedPlayersList().get(1).getText(), this.isPlayer2Ready);
			});
		}
	}
	private void exchangeReadyStatusReverseOrder() throws IOException {
		
		//check do we change client player status
		boolean oldValue = this.isPlayer2Ready;
		String msg = reader.readLine();
		if (msg!=null)
			this.isPlayer2Ready = msg.equals("true")? true: msg.equals("false")? false : this.isPlayer2Ready;
		if (oldValue!=this.isPlayer2Ready) {
			System.out.println(this.isPlayer2Ready? "client is now ready" : "client is now not ready");
			Platform.runLater(()->{
				this.lobbyView.setPlayerReady(this.lobbyView.getJoinedPlayersList().get(1).getText(), this.isPlayer2Ready);
			});
		}
		
		//send this player ready status
		writer.write(isHostReady+"\n");
		writer.flush();
				
	}
	private TextField createTextField(String promptText) {
		TextField tf = new TextField();
		tf.setPromptText(promptText);
		tf.setMaxWidth(350);
		tf.setPrefHeight(30);
		tf.setFont(Font.font("badoni MT",FontWeight.BOLD,20));
		tf.setStyle(" -fx-background-radius:30;");
		
		
		return tf;
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
	
	private Label createLabel(String string) {
		// TODO Auto-generated method stub
		Label l = new Label(string);
		l.setTextFill(Color.YELLOW);
		l.setFont(Font.font("badoni MT",FontWeight.BOLD,40));
		return l;
	}
	
	

	

}
