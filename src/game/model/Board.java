package game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import game.util.Color;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Lukas Zanner
 */
public class Board {

	private static Board instance = new Board();

	private BooleanProperty newGame;
	private BooleanProperty gameWon;
	private BooleanProperty merelSet;
	private ObjectProperty<Location> placedTileLocation;

	private Map<Location, Color> gameGrid;

	private int numberOfTilesPlacedOnBoard;
	private Location newLocation;
	private Location oldLocation;
	private List<Location> allowedDropLocations;
	private List<Location> allLocations;
	private final Location allLocationsFlag;
	private Map<Location, Color> freeTileLocations;
	private Merel merels;

	private Location l00 = new Location(0, 0);
	private Location l03 = new Location(0, 3);
	private Location l06 = new Location(0, 6);
	private Location l11 = new Location(1, 1);
	private Location l13 = new Location(1, 3);
	private Location l15 = new Location(1, 5);
	private Location l22 = new Location(2, 2);
	private Location l23 = new Location(2, 3);
	private Location l24 = new Location(2, 4);
	private Location l30 = new Location(3, 0);
	private Location l31 = new Location(3, 1);
	private Location l32 = new Location(3, 2);
	private Location l34 = new Location(3, 4);
	private Location l35 = new Location(3, 5);
	private Location l36 = new Location(3, 6);
	private Location l42 = new Location(4, 2);
	private Location l43 = new Location(4, 3);
	private Location l44 = new Location(4, 4);
	private Location l51 = new Location(5, 1);
	private Location l53 = new Location(5, 3);
	private Location l55 = new Location(5, 5);
	private Location l60 = new Location(6, 0);
	private Location l63 = new Location(6, 3);
	private Location l66 = new Location(6, 6);

	public Board() {
		newGame = new SimpleBooleanProperty();
		gameWon = new SimpleBooleanProperty();
		merelSet = new SimpleBooleanProperty();
		placedTileLocation = new SimpleObjectProperty<>();

		gameGrid = new HashMap<>();
		merels = new Merel();
		allowedDropLocations = new ArrayList<>();
		allLocations = new ArrayList<>();
		allLocationsFlag = new Location(-1, -1);
		freeTileLocations = new HashMap<>();
	}

	public static Board getInstance() {
		return instance;
	}

	/**
	 * Initializes all relevant attributes.
	 */
	public void startGame() {
		setNewGame(false);
		setGameWon(false);
		setMerelSet(false);
		setPlacedTileLocation(null);

		numberOfTilesPlacedOnBoard = 0;
		newLocation = null;
		oldLocation = null;

		gameGrid.clear();
		allowedDropLocations.clear();
		fillAllLocationsList();
		freeTileLocations.clear();
		merels.clear();
	}

	public final BooleanProperty newGameProperty() {
		return this.newGame;
	}

	public final boolean isNewGame() {
		return this.newGameProperty().get();
	}

	public final void setNewGame(final boolean newGame) {
		this.newGameProperty().set(newGame);
	}

	public final BooleanProperty gameWonProperty() {
		return this.gameWon;
	}

	public final boolean isGameWon() {
		return this.gameWonProperty().get();
	}

	public final void setGameWon(final boolean gameWon) {
		this.gameWonProperty().set(gameWon);
	}

	public final ObjectProperty<Location> placedTileLocationProperty() {
		return this.placedTileLocation;
	}

	public final Location getPlacedTileLocation() {
		return this.placedTileLocationProperty().get();
	}

	public final void setPlacedTileLocation(final Location placedTileLocation) {
		this.placedTileLocationProperty().set(placedTileLocation);
	}

	public final BooleanProperty merelSetProperty() {
		return this.merelSet;
	}

	public final boolean isMerelSet() {
		return this.merelSetProperty().get();
	}

	public final void setMerelSet(final boolean merelSet) {
		this.merelSetProperty().set(merelSet);
	}

	public Map<Location, Color> getGameGrid() {
		return gameGrid;
	}

	public Merel getMerels() {
		return merels;
	}

	public int getNumberOfTilesPlacedOnBoard() {
		return numberOfTilesPlacedOnBoard;
	}

	public List<Location> getAllowedDropLocations() {
		return allowedDropLocations;
	}

	public Location getAllLocationsFlag() {
		return allLocationsFlag;
	}

	public Map<Location, Color> getFreeTileLocations() {
		return freeTileLocations;
	}

	public void setNewLocation(Location newLocation) {
		this.newLocation = newLocation;
	}

	/**
	 * Sets the possible drop locations for the current player.
	 * 
	 * @param oldLocation
	 * @param id
	 *            1 if a tile can be dropped anywhere on the board, 2 otherwise
	 */
	public void setAllowedDropLocations(Location oldLocation, int id) {
		this.oldLocation = oldLocation;
		allowedDropLocations.clear();

		if (id == 1) {
			allowedDropLocations.addAll(allLocations);
		} else if (id == 0) {
			if (oldLocation.equals(l00)) {
				allowedDropLocations.add(l30);
				allowedDropLocations.add(l03);
			} else if (oldLocation.equals(l30)) {
				allowedDropLocations.add(l00);
				allowedDropLocations.add(l60);
				allowedDropLocations.add(l31);
			} else if (oldLocation.equals(l60)) {
				allowedDropLocations.add(l30);
				allowedDropLocations.add(l63);
			} else if (oldLocation.equals(l11)) {
				allowedDropLocations.add(l31);
				allowedDropLocations.add(l13);
			} else if (oldLocation.equals(l31)) {
				allowedDropLocations.add(l51);
				allowedDropLocations.add(l11);
				allowedDropLocations.add(l30);
				allowedDropLocations.add(l32);
			} else if (oldLocation.equals(l51)) {
				allowedDropLocations.add(l31);
				allowedDropLocations.add(l53);
			} else if (oldLocation.equals(l22)) {
				allowedDropLocations.add(l32);
				allowedDropLocations.add(l23);
			} else if (oldLocation.equals(l32)) {
				allowedDropLocations.add(l22);
				allowedDropLocations.add(l42);
				allowedDropLocations.add(l31);
			} else if (oldLocation.equals(l42)) {
				allowedDropLocations.add(l32);
				allowedDropLocations.add(l43);
			} else if (oldLocation.equals(l03)) {
				allowedDropLocations.add(l00);
				allowedDropLocations.add(l06);
				allowedDropLocations.add(l13);
			} else if (oldLocation.equals(l13)) {
				allowedDropLocations.add(l11);
				allowedDropLocations.add(l15);
				allowedDropLocations.add(l03);
				allowedDropLocations.add(l23);
			} else if (oldLocation.equals(l23)) {
				allowedDropLocations.add(l22);
				allowedDropLocations.add(l24);
				allowedDropLocations.add(l13);
			} else if (oldLocation.equals(l43)) {
				allowedDropLocations.add(l42);
				allowedDropLocations.add(l44);
				allowedDropLocations.add(l53);
			} else if (oldLocation.equals(l53)) {
				allowedDropLocations.add(l51);
				allowedDropLocations.add(l55);
				allowedDropLocations.add(l43);
				allowedDropLocations.add(l63);
			} else if (oldLocation.equals(l63)) {
				allowedDropLocations.add(l60);
				allowedDropLocations.add(l66);
				allowedDropLocations.add(l53);
			} else if (oldLocation.equals(l24)) {
				allowedDropLocations.add(l23);
				allowedDropLocations.add(l34);
			} else if (oldLocation.equals(l34)) {
				allowedDropLocations.add(l24);
				allowedDropLocations.add(l44);
				allowedDropLocations.add(l35);
			} else if (oldLocation.equals(l44)) {
				allowedDropLocations.add(l34);
				allowedDropLocations.add(l43);
			} else if (oldLocation.equals(l15)) {
				allowedDropLocations.add(l13);
				allowedDropLocations.add(l35);
			} else if (oldLocation.equals(l35)) {
				allowedDropLocations.add(l15);
				allowedDropLocations.add(l55);
				allowedDropLocations.add(l34);
				allowedDropLocations.add(l36);
			} else if (oldLocation.equals(l55)) {
				allowedDropLocations.add(l53);
				allowedDropLocations.add(l35);
			} else if (oldLocation.equals(l06)) {
				allowedDropLocations.add(l03);
				allowedDropLocations.add(l36);
			} else if (oldLocation.equals(l36)) {
				allowedDropLocations.add(l06);
				allowedDropLocations.add(l66);
				allowedDropLocations.add(l35);
			} else if (oldLocation.equals(l66)) {
				allowedDropLocations.add(l36);
				allowedDropLocations.add(l63);
			}
		}

	}

	/**
	 * Adds all possible tile locations to a list.
	 */
	private void fillAllLocationsList() {
		allLocations.clear();
		allLocations.addAll(Arrays.asList(l00, l03, l06, l11, l13, l15, l22, l23, l24, l30, l31, l32, l34, l35, l36, l42, l43, l44, l51, l53, l55,
				l60, l63, l66));
	}

	/**
	 * Adds a tile to the list of free tiles on the board.
	 * 
	 * @param color
	 *            tile's color
	 */
	public void addTileToBoard(Color color) {
		gameGrid.put(newLocation, color);
		addFreeTileLocation(newLocation, color);
		numberOfTilesPlacedOnBoard++;
	}

	/**
	 * Updates the state of the board after making a move on it.
	 * 
	 * @param color
	 *            tile's color
	 */
	public void updateBoard(Color color) {
		gameGrid.remove(oldLocation);
		merels.removeMerel(color, oldLocation);
		removeTilesFromFreeTileList(oldLocation);

		addTileToBoard(color);
	}

	/**
	 * Removes the passed locations from the list of free tile locations.
	 * 
	 * @param locations
	 *            tile's locations to be removed
	 */
	public void removeTilesFromFreeTileList(Location... locations) {
		for (Location location : locations) {
			if (freeTileLocations.containsKey(location)) {
				freeTileLocations.remove(location);
			}
		}
	}

	/**
	 * Adds the passed location to the list of free tiles.
	 * 
	 * @param location
	 *            new location of a tile
	 * @param color
	 *            tile's color
	 */
	public void addFreeTileLocation(Location location, Color color) {
		freeTileLocations.put(location, color);
	}

	/**
	 * Checks if tiles can be moved or not
	 * 
	 * @param location
	 *            tile's location to check from
	 * @return true if a tile can be moved, false otherwise
	 */
	public boolean nextMovePossible(Location location) {
		boolean success = false;
		Iterator<Location> it;

		setAllowedDropLocations(location, 0);

		it = allowedDropLocations.iterator();
		while (it.hasNext() && !success) {
			if (gameGrid.get(it.next()) == null) {
				success = true;
			}
		}

		return success;
	}

	/**
	 * Checks if a merel was set.
	 * 
	 * @param location
	 *            newly set location
	 * @return true if merel was set, false otherwise
	 */
	public boolean checkMerels(Location location) {
		if (location.equals(l00)) {
			return checkLocation(l00, l30, l60, l00, l03, l06);
		} else if (location.equals(l30)) {
			return checkLocation(l00, l30, l60, l30, l31, l32);
		} else if (location.equals(l60)) {
			return checkLocation(l00, l30, l60, l60, l63, l66);
		} else if (location.equals(l11)) {
			return checkLocation(l11, l13, l15, l11, l31, l51);
		} else if (location.equals(l31)) {
			return checkLocation(l30, l31, l32, l11, l31, l51);
		} else if (location.equals(l51)) {
			return checkLocation(l51, l53, l55, l11, l31, l51);
		} else if (location.equals(l22)) {
			return checkLocation(l22, l23, l24, l22, l32, l42);
		} else if (location.equals(l32)) {
			return checkLocation(l30, l31, l32, l22, l32, l42);
		} else if (location.equals(l42)) {
			return checkLocation(l42, l43, l44, l22, l32, l42);
		} else if (location.equals(l03)) {
			return checkLocation(l00, l03, l06, l03, l13, l23);
		} else if (location.equals(l13)) {
			return checkLocation(l11, l13, l15, l03, l13, l23);
		} else if (location.equals(l23)) {
			return checkLocation(l22, l23, l24, l03, l13, l23);
		} else if (location.equals(l43)) {
			return checkLocation(l42, l43, l44, l43, l53, l63);
		} else if (location.equals(l53)) {
			return checkLocation(l51, l53, l55, l43, l53, l63);
		} else if (location.equals(l63)) {
			return checkLocation(l60, l63, l66, l43, l53, l63);
		} else if (location.equals(l24)) {
			return checkLocation(l22, l23, l24, l24, l34, l44);
		} else if (location.equals(l34)) {
			return checkLocation(l34, l35, l36, l24, l34, l44);
		} else if (location.equals(l44)) {
			return checkLocation(l42, l43, l44, l24, l34, l44);
		} else if (location.equals(l15)) {
			return checkLocation(l11, l13, l15, l15, l35, l55);
		} else if (location.equals(l35)) {
			return checkLocation(l34, l35, l36, l15, l35, l55);
		} else if (location.equals(l55)) {
			return checkLocation(l51, l53, l55, l15, l35, l55);
		} else if (location.equals(l06)) {
			return checkLocation(l00, l03, l06, l06, l36, l66);
		} else if (location.equals(l36)) {
			return checkLocation(l34, l35, l36, l06, l36, l66);
		} else if (location.equals(l66)) {
			return checkLocation(l60, l63, l66, l06, l36, l66);
		}
		return false;
	}

	/**
	 * Checks if the passed locations are building a merel or not.
	 * 
	 * @param l1 location of tile 1 in merel #1
	 * @param l2 location of tile 2 in merel #1
	 * @param l3 location of tile 3 in merel #1
	 * @param l4 location of tile 1 in merel #2
	 * @param l5 location of tile 2 in merel #2
	 * @param l6 location of tile 3 in merel #2
	 * @return true if merel is set, false otherwise
	 */
	private boolean checkLocation(Location l1, Location l2, Location l3, Location l4, Location l5, Location l6) {
		boolean merelFound = false;
		if (gameGrid.get(l1) != null && gameGrid.get(l2) != null && gameGrid.get(l3) != null) {
			if ((gameGrid.get(l1) == gameGrid.get(l2) && gameGrid.get(l2) == gameGrid.get(l3))) {
				merels.addMerel(l1, l2, l3);
				removeTilesFromFreeTileList(l1, l2, l3);
				merelFound = true;
			}
		}
		if (gameGrid.get(l4) != null && gameGrid.get(l5) != null && gameGrid.get(l6) != null) {
			if ((gameGrid.get(l4) == gameGrid.get(l5) && gameGrid.get(l5) == gameGrid.get(l6))) {
				merels.addMerel(l4, l5, l6);
				removeTilesFromFreeTileList(l4, l5, l6);
				merelFound = true;
			}
		}
		return merelFound;
	}

	/**
	 * Checks if a tile can actually be dropped on the location that the player wants it to drop.
	 * 
	 * @param location
	 *            possible drop location
	 * @return true if tile can be dropped, false otherwise
	 */
	public boolean tileCanBeDropped(Location location) {
		return allowedDropLocations.contains(location);
	}

	/**
	 * Checks if the tile on the passed location can be removed.
	 * 
	 * @param location
	 *            possible location to be removed
	 * @param color
	 *            tile's color
	 * @return true if tile can be removed, false otherwise
	 */
	public boolean tileCanBeRemoved(Location location, Color color) {
		if (merels.getExistingMerels().contains(location)) {
			if (freeTileLocations.containsValue(color)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes the passed location from all relevant data structures on where tiles are saved.
	 * 
	 * @param location
	 *            the location to be removed
	 * @param color
	 *            tile's color
	 */
	public void removeTile(Location location, Color color) {
		gameGrid.remove(location);
		merels.removeMerel(color, location);
		removeTilesFromFreeTileList(location); // must be done AFTER removeMerel() !!
	}

}
