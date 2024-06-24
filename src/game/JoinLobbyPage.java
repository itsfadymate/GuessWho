package game;

import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class JoinLobbyPage extends BorderPane {

	public JoinLobbyPage() {
		this.setPrefSize(1360, 780);
		StackPane s1 = new StackPane();
		StackPane s2 = new StackPane();
		s1.prefWidth(240);
		s2.prefWidth(240);
		this.setLeft(s1);
		this.setRight(s2);
		Label nameLabel = createLabel("Name: ");
		TextField Name = createTextField("Name");
		VBox nameBox = new VBox(nameLabel,Name);
		nameBox.setMaxWidth(350);
		
		Label ipAddressLabel = createLabel("IP Address: ");
		TextField IPAddress = createTextField("IP Address");
		VBox IPAddressBox = new VBox(ipAddressLabel,IPAddress);
		IPAddressBox.setMaxWidth(350);
		
		Button submit = createButton("Join");
		VBox txtBox = new VBox(nameBox,IPAddressBox,submit);
		txtBox.setMaxWidth(350);
		txtBox.setAlignment(Pos.CENTER);
		txtBox.setSpacing(80);
		this.setCenter(txtBox);
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
