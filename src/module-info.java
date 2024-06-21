module guessWho {
	exports game;

	requires javafx.base;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.controls;
	opens game to javafx.fxml;
}