package game.manipulator;

import java.awt.Graphics2D;
import java.awt.Point;

import torre.Torre;

/**
 * Classe que define um manipulador que não faz nada. Útil para as classes que
 * não requerem manipulação, como a macaco. Útil também para as subclasses,
 * porque assim apenas têm de implementar os comportamentos que desejam
 */
public class ManipuladorVazio implements ManipuladorTorre {

	private Torre tower; // guarda a torre que está a manipular

	public ManipuladorVazio(Torre t) {
		tower = t;
	}

	@Override
	public void desenhar(Graphics2D g) {
		tower.desenhaRaioAcao(g);
	}

	@Override
	public void mouseDragged(Point p) {
	}

	@Override
	public void mouseReleased(Point p) {
	}

	@Override
	public Torre getTorre() {
		return tower;
	}
}
