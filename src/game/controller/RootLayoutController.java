package game.controller;

import game.GameManager;
import game.GameMerels;
import game.model.Board;
import game.model.Location;
import game.util.Color;
import game.util.GamePhase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Lukas Zanner
 */
public class RootLayoutController {

	private final String aboutText = "Product Version\n" + GameMerels.FULL_NAME
			+ "\n\nBuild Information\nDate: 2017-07-23\nJava Version: Build 1.8.0_131-b11\n" + "\nAuthor: Lukas Zanner\n";

	private Stage stage;
	private Board board;
	private GameManager gameManager;

	private ObservableList<ImageView> boardGridChildren = FXCollections.observableArrayList();

	@FXML
	private GridPane boardGrid;
	@FXML
	private GridPane leftTileGrid;
	@FXML
	private GridPane rightTileGrid;

	/**
	 * Returns the location of the {@code ImageView} passed to the method.
	 * 
	 * @param iv
	 *            {@code ImageView} to find out the location of
	 * @return location in the corresponding {@code GridPane}
	 */
	private Location getLocationOfTile(ImageView iv) {
		Integer column = GridPane.getColumnIndex(iv);
		Integer row = GridPane.getRowIndex(iv);
		return new Location(column == null ? 0 : column, row == null ? 0 : row);
	}

	/**
	 * Sets the stage of the application.
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Sets the {@code GameManager} object in which the game logic is placed.
	 * 
	 * @param gameManager
	 */
	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	/**
	 * Opens a dialog before starting a new game.
	 */
	@FXML
	private void handleNewGame() {
		enableBlur();
		handleNewGameAndCloseDialog("New Game", null, "Do you really want to start a new game?", 0);
		disableBlur();
	}

	/**
	 * Opens a dialog before closing the game.
	 */
	@FXML
	public void handleClose() {
		enableBlur();
		handleNewGameAndCloseDialog(GameMerels.GAME_NAME, "Quit Game?", "Do you really want to quit the game?", 1);
		disableBlur();
	}

	/**
	 * Opens a confirmation dialog specified by the given parameters.
	 * 
	 * @param title
	 * @param header
	 * @param contentText
	 * @param id
	 *            0 if new game dialog, 1 if close dialog
	 */
	private void handleNewGameAndCloseDialog(String title, String header, String contentText, int id) {
		ButtonType btnYes = new ButtonType("Yes", ButtonData.YES);
		ButtonType btnNo = new ButtonType("No", ButtonData.NO);
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(contentText);
		alert.initOwner(stage);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(btnYes, btnNo);
		alert.showAndWait();
		if (id == 0) {
			if (alert.getResult() == btnYes) {
				initWindow();
				board.setNewGame(true);
			}
		} else if (id == 1) {
			if (alert.getResult() == btnYes) {
				Platform.exit();
			}
		}
	}

	/**
	 * Opens some general information about the game.
	 */
	@FXML
	private void handleAbout() {
		enableBlur();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("About " + GameMerels.GAME_NAME);
		alert.setContentText(aboutText);
		alert.initOwner(stage);
		alert.showAndWait();
		disableBlur();
	}

	/**
	 * If a invalid move is done by a player, this dialog pops up.
	 * 
	 * @param contentText
	 *            the message to be displayed in the dialog
	 */
	private void showInvalidMoveDialog(String contentText) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(contentText);
		alert.initStyle(StageStyle.UTILITY);
		alert.initOwner(stage);
		alert.showAndWait();
	}

	/**
	 * Popup that informs the players that somebody won and who won the game.
	 */
	private void showGameWonDialog() {
		Label lbl = new Label("GAME OVER!");
		boardGrid.add(lbl, 3, 3);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game won");
		alert.setHeaderText(null);
		alert.setContentText(gameManager.getOtherPlayer().getName() + " (" + gameManager.getOtherPlayer().getColor() + ")" + " won the game!");
		alert.initOwner(stage);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			handleNewGameAndCloseDialog("Game won", null, "Game is over. Do you want to start a new game?", 0);
		}
	}

	/**
	 * For some menu points a blur is put over the game.
	 */
	private void enableBlur() {
		stage.getScene().getRoot().setEffect(new GaussianBlur(4));
	}

	/**
	 * Removes the blur set over the game.
	 */
	private void disableBlur() {
		stage.getScene().getRoot().setEffect(null);
	}

	/**
	 * If a merel was set, an 'X' is displayed on the tiles that can be removed.
	 * 
	 * @param removableColor
	 */
	private void putImagesOnRemovableTiles(Color removableColor) {
		for (ImageView iv : boardGridChildren) {
			Location removableLocation = getLocationOfTile(iv);
			if (iv.getId() != null) {
				if (removableColor == Color.BLACK && iv.getId().contains("blk")) {
					if (gameManager.tileCanBeRemoved(removableLocation, gameManager.getColorOfOtherPlayer())) {
						iv.setImage(new Image("file:res/textures/black_tile_removable.png"));
					}
				} else if (removableColor == Color.WHITE && iv.getId().contains("wht")) {
					if (gameManager.tileCanBeRemoved(removableLocation, gameManager.getColorOfOtherPlayer())) {
						iv.setImage(new Image("file:res/textures/white_tile_removable.png"));
					}
				}
			}
		}
	}

	/**
	 * If a tile is removed after a merel was set, the default image is loaded again.
	 * 
	 * @param color
	 */
	private void resetImagesOnRemovableTiles(Color color) {
		for (ImageView iv : boardGridChildren) {
			if (iv.getId() != null) {
				if (color == Color.BLACK && iv.getId().contains("blk")) {
					iv.setImage(new Image("file:res/textures/black_tile.png"));
				} else if (color == Color.WHITE && iv.getId().contains("wht")) {
					iv.setImage(new Image("file:res/textures/white_tile.png"));
				}
			}
		}
	}

	/**
	 * If a drag is started, a green tile is set as an image on all possible drop locations of the game board.
	 */
	private void putImagesOnAllowedDropLocations() {
		for (ImageView iv : boardGridChildren) {
			Location location = getLocationOfTile(iv);
			if (gameManager.getAllowedDropLocations().contains(location) && iv.getId() == null) {
				iv.setImage(new Image("file:res/textures/green_tile.png"));
			}
		}
	}

	/**
	 * If a drop is completed (successful or unsuccessfull), the green tiles that indicated the possible drop locations are removed again.
	 */
	private void resetAllowedDropLocationImages() {
		for (ImageView iv : boardGridChildren) {
			if (iv.getId() == null) {
				iv.setImage(null);
			}
		}
	}

	/**
	 * Sets the scene to its default values as set at the very first start.
	 */
	private void initWindow() {
		stage.getScene().getStylesheets().clear();
		stage.getScene().getStylesheets().add(GameMerels.class.getResource("view/RootLayout.fxml").toExternalForm());

		if (board.isGameWon()) {
			boardGrid.getChildren().remove(24);
		}

		for (ImageView iv : boardGridChildren) {
			iv.setId(null);
			iv.setImage(null);
		}
	}

	/**
	 * Sets the Listeners for the Properties of the {@code Board} class.
	 */
	private void initBoardPropertyListeners() {
		board.newGameProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue) {
				gameManager.startGame();
			}
		});
		board.merelSetProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue) {
				putImagesOnRemovableTiles(gameManager.getColorOfOtherPlayer());
			} else {
				resetImagesOnRemovableTiles(gameManager.getColorOfCurrentPlayer());
			}
		});
		board.gameWonProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue) {
				showGameWonDialog();
			}
		});
		board.placedTileLocationProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) {
				gameManager.tilePlaced(newValue);
			}
		});
	}

	/**
	 * Initializes the removal of a tile after clicking on it whenever a merel was set.
	 */
	private void initRemoveTileAfterMerel() {
		for (ImageView iv : boardGridChildren) {
			iv.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> { // MouseEvent
				if (board.isMerelSet()) {
					if (iv.getImage() != null && iv.getId() != null) {
						if (iv.getId().contains("blk") && gameManager.getColorOfCurrentPlayer() == Color.WHITE
								|| iv.getId().contains("wht") && gameManager.getColorOfCurrentPlayer() == Color.BLACK) {
							showInvalidMoveDialog("You can't remove your own tiles!");
						}

						if (iv.getId().contains("blk") && gameManager.getColorOfCurrentPlayer() == Color.BLACK
								|| iv.getId().contains("wht") && gameManager.getColorOfCurrentPlayer() == Color.WHITE || gameManager.isEndGame()) {

							Location location = getLocationOfTile(iv);

							if (gameManager.tileCanBeRemoved(location)) {
								iv.setImage(null);
								iv.setId(null);

								gameManager.removeTile(location);
								board.setMerelSet(false);
							} else {
								showInvalidMoveDialog("Selected tile cannot be removed.");
							}
						}
					}
				}
				event.consume();
			});
		}
	}

	/**
	 * Initializes the drag functionality on a given {@code GridPane} parameter.
	 * 
	 * @param grid
	 *            to be allowed to drag from
	 */
	private void initDrag(GridPane grid) {
		for (Node i : grid.getChildren()) {
			ImageView iv = (ImageView) i;

			iv.setOnDragDetected(event -> { // MouseEvent
				if (board.isGameWon()) {
					handleNewGameAndCloseDialog("Game over", null, "Game is over. Do you want to start a new game?", 0);
					return;
				}
				if (board.isMerelSet()) {
					showInvalidMoveDialog(gameManager.getOtherPlayer().getName() + " (" + gameManager.getOtherPlayer().getColorString() + ")"
							+ " set a merel. A tile of " + gameManager.getCurrentPlayer().getName() + " ("
							+ gameManager.getCurrentPlayer().getColorString() + ")" + " has to be removed.");
					return;
				}
				if (iv.getImage() == null) {
					return;
				}
				if (gameManager.getGamePhase() != GamePhase.PLACE && !grid.getId().equals(boardGrid.getId())) {
					return;
				}
				if (gameManager.getCurrentPlayer().getColor() == Color.BLACK && iv.getId().contains("blk")
						|| gameManager.getCurrentPlayer().getColor() == Color.WHITE && iv.getId().contains("wht")) {
					if (gameManager.getGamePhase() == GamePhase.PLACE && grid.getId().equals(boardGrid.getId())) {
						showInvalidMoveDialog("All tiles have to be set onto the game board first before moving them on it.");
						return;
					}

					gameManager.setAllowDropLocations(getLocationOfTile(iv));
					if (gameManager.getGamePhase() != GamePhase.PLACE) {
						putImagesOnAllowedDropLocations();
					}

					Dragboard db = iv.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putImage(iv.getImage());
					content.putString(iv.getId());
					db.setContent(content);
				} else {
					showInvalidMoveDialog(gameManager.getCurrentPlayer().getName() + " is on the turn. A "
							+ gameManager.getCurrentPlayer().getColor().toString().toLowerCase() + " tile has to be moved.");
				}
				event.consume();
			});

			iv.setOnDragDone(event -> { // DragEvent
				if (event.getTransferMode() == TransferMode.MOVE) {
					iv.setImage(null);
					if (grid.getId().equals(boardGrid.getId())) {
						iv.setId(null);
					}
				}
				event.consume();
				resetAllowedDropLocationImages();
			});
		}
	}

	/**
	 * Initializes the drop functionality on a given {@code GridPane} parameter.
	 * 
	 * @param grid
	 *            to be allowed to drop on
	 */
	private void initDrop(GridPane grid) {
		for (Node i : grid.getChildren()) {
			ImageView iv = (ImageView) i;

			iv.setOnDragOver(event -> { // DragEvent
				Dragboard db = event.getDragboard();
				if (event.getGestureSource() != iv && db.hasImage() && db.hasString() && iv.getId() == null) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			});

			iv.setOnDragDropped(event -> { // DragEvent
				Dragboard db = event.getDragboard();
				if (db.hasImage() && db.hasString()) {
					if (iv.getId() == null) {
						Location dropLocation = getLocationOfTile(iv);
						if (gameManager.tileCanBeDropped(dropLocation)) {
							iv.setImage(db.getImage());
							iv.setId(db.getString());
							board.setPlacedTileLocation(dropLocation);
							event.setDropCompleted(true);
						} else {
							showInvalidMoveDialog("Dropping a tile at this position is not allowed.");
						}
					}
				}
				event.consume();
			});
		}
	}

	/**
	 * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		board = Board.getInstance();

		for (Node i : boardGrid.getChildren()) {
			boardGridChildren.add((ImageView) i);
		}

		initBoardPropertyListeners();

		initDrag(leftTileGrid);
		initDrag(rightTileGrid);
		initDrag(boardGrid);
		initDrop(boardGrid);

		initRemoveTileAfterMerel();
	}

}
