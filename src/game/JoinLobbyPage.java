package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
//TODO: Validate name
public class JoinLobbyPage extends BorderPane {

	private LobbyPage lobbyView;
	private boolean isPlayer2Ready;
	private boolean isHostReady;
	private BufferedWriter writer;
	private BufferedReader reader;
	private Socket socket;
	private TextField Name;
	private boolean continueChecking;

	public JoinLobbyPage() {
		//Related to thread reading host status in method waitTillBothPlayersReady
		this.continueChecking = true;

		this.setPrefSize(1360, 780);
		StackPane s1 = new StackPane();
		StackPane s2 = new StackPane();
		s1.prefWidth(240);
		s2.prefWidth(240);
		this.setLeft(s1);
		this.setRight(s2);
		Label nameLabel = createLabel("Name: ");
		Name = createTextField("Name");
		VBox nameBox = new VBox(nameLabel,Name);
		nameBox.setMaxWidth(350);

		Label ipAddressLabel = createLabel("IP Address: ");
		TextField IPAddress = createTextField("IP Address");
		VBox IPAddressBox = new VBox(ipAddressLabel,IPAddress);
		IPAddressBox.setMaxWidth(350);

		Button submit = createButton("Join");
		submit.setOnAction(e->{
			try {
				this.isPlayer2Ready = false;
				this.socket = new Socket(IPAddress.getText(),12345);
				this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.println("connection established");

				System.out.println("sending client Name to Host");
				writer.write(Name.getText()+"\n");
				writer.flush();

				String HostName = reader.readLine();
				this.lobbyView = new LobbyPage();
				lobbyView.addPlayerToList(HostName);
				lobbyView.addPlayerToList(Name.getText());

				Stage stage = (Stage)submit.getScene().getWindow();
				stage.setScene(new Scene(lobbyView));


				System.out.println("awaiting mutual ready status");
				waitTillBothPlayersReady();
			} catch (UnknownHostException e1) {
				System.out.println("couldnot find host");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		VBox txtBox = new VBox(nameBox,IPAddressBox,submit);
		txtBox.setMaxWidth(350);
		txtBox.setAlignment(Pos.CENTER);
		txtBox.setSpacing(80);
		this.setCenter(txtBox);
	}

	private void waitTillBothPlayersReady() {
		//continuously read Host status
		//TODO:Handle when to interrupt the thread :')
		Thread handleGameStatus = new Thread(()->{
			long counter = 0;
			while (this.socket.isConnected() && continueChecking ) {

				try {
					counter++;
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
					System.out.println("--------------------" + counter );
					e.printStackTrace();
					break;
				}
			}
		});

		handleGameStatus.start();


		lobbyView.setOnReadyClicked(e->{
			this.isPlayer2Ready=!isPlayer2Ready;
			lobbyView.setPlayerReady(Name.getText(), isPlayer2Ready);
		});


	}

	private void exchangeReadyStatus() throws IOException {
		//checks do we change Host player status?
		boolean oldValue = this.isHostReady; 
		String msg = reader.readLine();
		if (msg!=null)
			this.isHostReady = msg.equals("true")? true: msg.equals("false")? false : this.isHostReady;
		if (oldValue!=this.isHostReady) {
			System.out.println(this.isHostReady? "host is now ready" : "host is now not ready");
			Platform.runLater(()->{
				this.lobbyView.setPlayerReady(this.lobbyView.getJoinedPlayersList().get(0).getText(), this.isHostReady);
			});
		}


		//send out this player ready status
		writer.write(isPlayer2Ready+"\n");
		writer.flush();
	}

	private void exchangeReadyStatusReverseOrder() throws IOException {

		//send out this player ready status
		writer.write(isPlayer2Ready+"\n");
		writer.flush();

		//checks do we change Host player status?
		boolean oldValue = this.isHostReady; 
		String msg = reader.readLine();
		if (msg!=null)
			this.isHostReady = msg.equals("true")? true: msg.equals("false")? false : this.isHostReady;
		if (oldValue!=this.isHostReady) {
			System.out.println(this.isHostReady? "host is now ready" : "host is now not ready");
			Platform.runLater(()->{
				this.lobbyView.setPlayerReady(this.lobbyView.getJoinedPlayersList().get(0).getText(), this.isHostReady);
			});
		}

	}

	private Label createLabel(String string) {
		Label l = new Label(string);
		l.setFont(Font.font("badoni MT",FontWeight.BOLD,20));
		return l;
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

	private Button createButton(String string) {
		Button b = new Button(string);
		b.setPrefSize(300, 70);
		b.setFont(Font.font("badoni MT",FontWeight.BOLD,30));
		b.setStyle("-fx-background-color: #E15000 ;"
				+ " -fx-background-radius:30 ;"
				+ " -fx-background-insets:2;"
				+ " -fx-border-color:#550000 ;"
				+ "	-fx-border-radius: 40 ;"
				+ "-fx-border-width: 7;");
		b.setOnMouseEntered(e->{
			b.setStyle("-fx-background-color: #FF0000 ;"
					+ " -fx-background-radius:30 ;"
					+ " -fx-background-insets:2;"
					+ " -fx-border-color:#EE0000 ;"
					+ "	-fx-border-radius: 40 ;"
					+ "-fx-border-width: 7;");
		});
		b.setOnMouseExited(e->{
			b.setStyle("-fx-background-color: #E15000 ;"
					+ " -fx-background-radius:30 ;"
					+ " -fx-background-insets:2;"
					+ " -fx-border-color:#550000 ;"
					+ "	-fx-border-radius: 40 ;"
					+ "-fx-border-width: 7;");
		});
		return b;
	}
}
