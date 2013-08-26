package player;

/**
 * Implements an exception that should be thrown for nonexistent coordinates.
 */
public class InvalidCoordinatesException extends Exception {
	public int coordinates; // The invalid coordinates.

	/**
	 * Creates an exception object for nonexistent coordinates "badCoord".
	 */
	public InvalidCoordinatesException(int badCoord) {
		super("Invalid coordination: " + badCoord);

		coordinates = badCoord;
	}
}
