package game.model;

import game.util.Color;

/**
 * @author Lukas Zanner
 */
public class Player {

	private String name;
	private Color color;
	private int numberOfTiles = 9;

	public Player(String name, Color color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	/**
	 * Returns the player's color in lower case.
	 * 
	 * @return lower case String representation of the player's color
	 */
	public String getColorString() {
		return color.toString().toLowerCase();
	}

	/**
	 * Returns the number of tiles the player has left to play with
	 * 
	 * @return number of the player's tiles
	 */
	public int getNumberOfTiles() {
		return numberOfTiles;
	}

	/**
	 * If a tile was removed by the enemy after a merel was set, the number of the player's tiles decreases.
	 */
	public void removeTile() {
		numberOfTiles--;
	}

	@Override
	public String toString() {
		return "{Player: name=" + name + ", color=" + color + ", numberOfTile=" + numberOfTiles + "}";
	}

}
