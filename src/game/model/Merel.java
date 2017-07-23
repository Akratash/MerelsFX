package game.model;

import java.util.ArrayList;
import java.util.List;

import game.util.Color;

/**
 * @author Lukas Zanner
 */
public class Merel {

	private List<Location> existingMerels;

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

	public Merel() {
		existingMerels = new ArrayList<>();
	}

	public List<Location> getExistingMerels() {
		return existingMerels;
	}

	/**
	 * Clears the list of existing merels.
	 */
	public void clear() {
		existingMerels.clear();
	}

	/**
	 * Removes the passed locations from the list of existing merels.
	 * 
	 * @param color
	 *            tile's color
	 * @param locations
	 *            locations to be removed
	 */
	private void removeLocations(Color color, Location... locations) {
		for (Location location : locations) {
			if (!existingMerels.contains(location)) {
				return;
			}
		}
		for (Location location : locations) {
			existingMerels.remove(location);
			if (!existingMerels.contains(location)) {
				Board.getInstance().addFreeTileLocation(location, color);
			}
		}
	}

	/**
	 * Adds the locations the list of existing merels.
	 * 
	 * @param locations
	 *            tile locations in a merel
	 */
	public void addMerel(Location... locations) {
		for (Location location : locations) {
			existingMerels.add(location);
		}
	}

	/**
	 * Removes locations from the existing merels list depending on the passed location.
	 * 
	 * @param color
	 *            tile's color
	 * @param location
	 *            location, which determines what merels should be removed
	 */
	public void removeMerel(Color color, Location location) {
		if (location.equals(l00)) {
			removeLocations(color, l00, l30, l60);
			removeLocations(color, l00, l03, l06);
		} else if (location.equals(l30)) {
			removeLocations(color, l00, l30, l60);
			removeLocations(color, l30, l31, l32);
		} else if (location.equals(l60)) {
			removeLocations(color, l60, l63, l66);
			removeLocations(color, l00, l30, l60);
		} else if (location.equals(l11)) {
			removeLocations(color, l11, l31, l51);
			removeLocations(color, l11, l13, l15);
		} else if (location.equals(l31)) {
			removeLocations(color, l11, l31, l51);
			removeLocations(color, l30, l31, l32);
		} else if (location.equals(l51)) {
			removeLocations(color, l11, l31, l51);
			removeLocations(color, l51, l53, l55);
		} else if (location.equals(l22)) {
			removeLocations(color, l22, l32, l42);
			removeLocations(color, l22, l23, l24);
		} else if (location.equals(l32)) {
			removeLocations(color, l22, l32, l42);
			removeLocations(color, l30, l31, l32);
		} else if (location.equals(l42)) {
			removeLocations(color, l22, l32, l42);
			removeLocations(color, l42, l43, l44);
		} else if (location.equals(l03)) {
			removeLocations(color, l00, l03, l06);
			removeLocations(color, l03, l13, l23);
		} else if (location.equals(l13)) {
			removeLocations(color, l11, l13, l15);
			removeLocations(color, l03, l13, l23);
		} else if (location.equals(l23)) {
			removeLocations(color, l22, l23, l24);
			removeLocations(color, l03, l13, l23);
		} else if (location.equals(l43)) {
			removeLocations(color, l43, l53, l63);
			removeLocations(color, l42, l43, l44);
		} else if (location.equals(l53)) {
			removeLocations(color, l43, l53, l63);
			removeLocations(color, l51, l53, l55);
		} else if (location.equals(l63)) {
			removeLocations(color, l43, l53, l63);
			removeLocations(color, l60, l63, l66);
		} else if (location.equals(l24)) {
			removeLocations(color, l24, l34, l44);
			removeLocations(color, l22, l23, l24);
		} else if (location.equals(l34)) {
			removeLocations(color, l24, l34, l44);
			removeLocations(color, l34, l35, l36);
		} else if (location.equals(l44)) {
			removeLocations(color, l24, l34, l44);
			removeLocations(color, l42, l43, l44);
		} else if (location.equals(l15)) {
			removeLocations(color, l15, l35, l55);
			removeLocations(color, l11, l13, l15);
		} else if (location.equals(l35)) {
			removeLocations(color, l15, l35, l55);
			removeLocations(color, l34, l35, l36);
		} else if (location.equals(l55)) {
			removeLocations(color, l15, l35, l55);
			removeLocations(color, l51, l53, l55);
		} else if (location.equals(l06)) {
			removeLocations(color, l00, l03, l06);
			removeLocations(color, l06, l36, l66);
		} else if (location.equals(l36)) {
			removeLocations(color, l34, l35, l36);
			removeLocations(color, l06, l36, l66);
		} else if (location.equals(l66)) {
			removeLocations(color, l60, l63, l66);
			removeLocations(color, l06, l36, l66);
		}
	}

}
