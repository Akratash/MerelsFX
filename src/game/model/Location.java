package game.model;

/**
 * @author Lukas Zanner
 */
public class Location {

	private final int x;
	private final int y;

	/**
	 * Constructor for a location object which sets the location in the form (x, y)
	 * 
	 * @param x
	 *            position in the x-direction
	 * @param y
	 *            position in the y-direction
	 */
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return (x + 1) * (y + 1);
	}

	@Override
	public String toString() {
		return "Location{" + "x=" + x + ", y=" + y + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Location) {
			Location other = (Location) obj;
			return this.x == other.x && this.y == other.y;
		}
		return false;
	}

}
