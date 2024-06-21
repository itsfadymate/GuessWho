package game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
Player1 p1;
@Override
public void initialize(URL arg0, ResourceBundle arg1) {

	try {
		 this.p1 = new Player1(new ServerSocket(12345));
	} catch (IOException e) {

		e.printStackTrace();
	}
	msgVBox.setSpacing(20);
	msgVBox.heightProperty().addListener((a,b,c)->{
			scrollPane.setVvalue((Double)c);
	});
	p1.listenForIncomingMessages(msgVBox);
}
public void makeAGuess() {}
public void showRules(){}
public void leaveGame(){}
public void toggleSound() {}
public void sendMessage() {
	if(!textField.getText().isEmpty()) {
		addMessage(textField.getText(),msgVBox,true);
		p1.sendMsg(textField.getText());
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
