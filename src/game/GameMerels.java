package game;

import java.io.IOException;

import game.controller.RootLayoutController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Lukas Zanner
 */
public class GameMerels extends Application {

	public static final String GAME_NAME = "Merels";
	public static final String GAME_VERSION = "1.0";
	public static final String FULL_NAME = GAME_NAME + " " + GAME_VERSION;

	private RootLayoutController controller;
	private GameManager gameManager;

	/**
	 * Starting point of the game.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(GameMerels.class.getResource("view/RootLayout.fxml"));
			VBox root = loader.load();
			controller = loader.getController();

			gameManager = new GameManager();
			controller.setStage(primaryStage);
			controller.setGameManager(gameManager);

			Platform.setImplicitExit(false);
			primaryStage.setOnCloseRequest(event -> { // WindowEvent
				controller.handleClose();
				event.consume();
			});

			Scene scene = new Scene(root);
			scene.getStylesheets().add(GameMerels.class.getResource("view/application.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.setTitle(GAME_NAME);
			primaryStage.getIcons().add(new Image("file:res/textures/icon.png"));
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
