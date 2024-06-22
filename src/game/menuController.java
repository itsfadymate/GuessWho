package game;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class menuController implements Initializable{
	@FXML
	ImageView backgroundImage;
	@FXML
	Button singlePlayerButton;
	@FXML
	Button multiPlayerButton;
	private Timeline animateBg ;
	private ScaleTransition singleplayerButtonAnimation;
	private ScaleTransition multiPlayerButtonAnimation;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		   this.backgroundImage.setViewport(new Rectangle2D(0,0,700,400));
		   this.animateBg = new Timeline(new KeyFrame(Duration.millis(50),new EventHandler<ActionEvent>(){
			double xDisplacement = 0.5;
			double yDisplacement = 1;

			@Override
			public void handle(ActionEvent arg0) {
				Rectangle2D vp =  backgroundImage.getViewport();

				backgroundImage.setViewport(new Rectangle2D(
			            vp.getMinX() + xDisplacement,
			            vp.getMinY() + yDisplacement,
			            vp.getWidth(),
			            vp.getHeight()
			        ));
				if (vp.getMaxX() > backgroundImage.getImage().getWidth() || vp.getMinX() < 0) {
					xDisplacement *=-1;
					backgroundImage.setViewport(new Rectangle2D(
				            vp.getMinX() + 3*xDisplacement,
				            vp.getMinY() ,
				            vp.getWidth(),
				            vp.getHeight()
				        ));
				}
				if (vp.getMaxY()> backgroundImage.getImage().getHeight() || vp.getMinY() < 0) {
					yDisplacement *=-1;
					backgroundImage.setViewport(new Rectangle2D(
				            vp.getMinX() ,
				            vp.getMinY() + 3*yDisplacement,
				            vp.getWidth(),
				            vp.getHeight()
				        ));
				}
			}



		}));
		   this.animateBg.setCycleCount(Animation.INDEFINITE);
		   this.animateBg.play();
	       this.singleplayerButtonAnimation = new ScaleTransition();
	       this.multiPlayerButtonAnimation = new ScaleTransition();
		   playButtonAnimation(singleplayerButtonAnimation,singlePlayerButton);
		   playButtonAnimation(multiPlayerButtonAnimation,multiPlayerButton);





	}


	private void playButtonAnimation(ScaleTransition ButtonAnimation,Button button) {
		   ButtonAnimation.setNode(button);
		   ButtonAnimation.setByX(0.3);
		   ButtonAnimation.setByY(0.3);
		   ButtonAnimation.setDuration(Duration.millis(700));
		   ButtonAnimation.setCycleCount(Animation.INDEFINITE);
		   ButtonAnimation.setAutoReverse(true);
		   ButtonAnimation.play();
	}


	public void startSinglePlayer(){
		animateBg.stop();
	}
	public void startMultiPlayer() {
		animateBg.stop();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("CreateJoinLobby.fxml"));
			//Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			//stage.setScene(scene);
			//stage.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void OnButtonEntered(MouseEvent e) {
		Button b = (Button)e.getSource();
		b.setStyle("-fx-border-radius: 50; -fx-border-width: 7;-fx-background-radius: 50; -fx-background-color:#00FF00 ; -fx-border-color:#00BB00 ;");
		singleplayerButtonAnimation.pause();
		multiPlayerButtonAnimation.pause();
	}
    public void OnButtonExited(MouseEvent e) {
    	Button b = (Button)e.getSource();
		b.setStyle("-fx-border-radius: 50; -fx-border-width: 7;-fx-background-radius: 50; -fx-background-color:#00DD00 ; -fx-border-color:#008800 ;");
		singleplayerButtonAnimation.play();
		multiPlayerButtonAnimation.play();
    }
}
