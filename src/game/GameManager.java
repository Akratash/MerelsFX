package game;

import java.util.List;

import game.model.Board;
import game.model.Location;
import game.model.Player;
import game.util.Color;
import game.util.GamePhase;

/**
 * @author Lukas Zanner
 */
public class GameManager {

	private Player player;
	private Player otherPlayer;
	private Player player1;
	private Player player2;

	private Board board;

	private GamePhase gamePhase;

	public GameManager() {
		board = Board.getInstance();

		startGame();
	}

	public void startGame() {
		player1 = new Player("Player 1", Color.BLACK);
		player2 = new Player("Player 2", Color.WHITE);
		player = player1;
		otherPlayer = player2;

		gamePhase = GamePhase.PLACE;

		board.startGame();
	}

	public Player getCurrentPlayer() {
		return player;
	}

	public Player getOtherPlayer() {
		return otherPlayer;
	}

	public Color getColorOfCurrentPlayer() {
		return player.getColor();
	}

	public Color getColorOfOtherPlayer() {
		return otherPlayer.getColor();
	}

	public GamePhase getGamePhase() {
		return gamePhase;
	}

	public List<Location> getAllowedDropLocations() {
		return board.getAllowedDropLocations();
	}

	public void setAllowDropLocations(Location oldLocation) {
		if (gamePhase == GamePhase.PLACE || player.getNumberOfTiles() == 3 && gamePhase == GamePhase.ENDGAME) {
			board.setAllowedDropLocations(oldLocation, 1);
		} else {
			board.setAllowedDropLocations(oldLocation, 0);
		}
	}

	/**
	 * Reacts to the event when a tile was placed on the board.
	 * 
	 * @param location
	 *            new location of a tile
	 */
	public void tilePlaced(Location location) {
		board.setNewLocation(location);

		if (gamePhase == GamePhase.PLACE) {
			board.addTileToBoard(player.getColor());
		} else {
			board.updateBoard(player.getColor());
		}

		if (board.getNumberOfTilesPlacedOnBoard() == 18) {
			gamePhase = GamePhase.MOVE;
		}
		if (board.getNumberOfTilesPlacedOnBoard() >= 5) {
			if (board.checkMerels(location)) {
				board.setMerelSet(true);
			}
		}

		changePlayers();

		if (gamePhase == GamePhase.MOVE) {
			if (!nextMovePossible()) {
				board.setGameWon(true);
			}
		}
	}

	/**
	 * Changes the current player.
	 */
	private void changePlayers() {
		if (player.equals(player1)) {
			player = player2;
			otherPlayer = player1;
		} else {
			player = player1;
			otherPlayer = player2;
		}
	}

	/**
	 * Checks if a move is possible.
	 * 
	 * @return true if moving is possible, false otherwise
	 */
	public boolean nextMovePossible() {
		boolean tileCanBeDropped = false;
		for (Location location : board.getGameGrid().keySet()) {
			if (player.equals(player1) && board.getGameGrid().get(location) == player.getColor()
					|| player.equals(player2) && board.getGameGrid().get(location) == player.getColor()) {
				tileCanBeDropped = board.nextMovePossible(location);
				if (board.nextMovePossible(location)) {
					tileCanBeDropped = true;
					break;
				}
			}
		}
		return tileCanBeDropped;
	}

	/**
	 * Checks if a tile can be dropped on the passed location.
	 * 
	 * @param location
	 *            possible drop location
	 * @return true if tile can be dropped, false otherwise
	 */
	public boolean tileCanBeDropped(Location location) {
		return board.tileCanBeDropped(location);
	}

	/**
	 * Checks if the tile on the passed location can be removed.
	 * 
	 * @param location
	 *            possible location a tile should be removed from
	 * @return true if the tile can be removed, false otherwise
	 */
	public boolean tileCanBeRemoved(Location location) {
		return board.tileCanBeRemoved(location, player.getColor());
	}

	/**
	 * Checks if the tile on the passed location can be removed.
	 * 
	 * @param location
	 *            possible location a tile should be removed from
	 * @param color
	 *            tile's color
	 * @return true if the tile can be removed, false otherwise
	 */
	public boolean tileCanBeRemoved(Location location, Color color) {
		return board.tileCanBeRemoved(location, color);
	}

	/**
	 * Checks if the game phase "end game" is reached
	 * 
	 * @return true if phase end game is reached, false otherwise
	 */
	public boolean isEndGame() {
		return gamePhase == GamePhase.ENDGAME && otherPlayer.getNumberOfTiles() == 3;
	}

	/**
	 * Removes the tile on the passed location.
	 * 
	 * @param location
	 *            the location the tile is removed from
	 */
	public void removeTile(Location location) {
		player.removeTile();

		if (player.getNumberOfTiles() == 3 && gamePhase != GamePhase.ENDGAME) {
			gamePhase = GamePhase.ENDGAME;
		}
		if (player.getNumberOfTiles() < 3) {
			board.setGameWon(true);
		}

		board.removeTile(location, player.getColor());
	}

}
