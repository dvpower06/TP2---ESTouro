package bloon;

/**
 * interface que representa um observador de bloons.
 * Estes são os interessados em saber que o bloon saiu ou que estourou
 */
public interface BloonObserver {

	/**
	 * informa que um bloon estourou
	 * 
	 * @param b bloon que estourou
	 */
	public void bloonEstourou(Bloon b);

	/**
	 * Informa que um bloon percorre todo o caminho e escapou
	 * 
	 * @param b bloon que escapou
	 */
	public void bloonEscapou(Bloon b);
}
