package game.manipulator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;

import torre.TorreMorteiro;

/**
 * Manipulador para a torre morteiro. Configura a área
 * para a qual o morteiro dispara.
 */
public class ManipuladorMorteiro extends ManipuladorVazio {

	// composite para usar transparências nas miras
	private static final AlphaComposite transp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);

	public ManipuladorMorteiro(TorreMorteiro t) {
		super(t);
	}

	@Override
	public void desenhar(Graphics2D g) {
		Composite oldComp = g.getComposite();
		Point attackPoint = ((TorreMorteiro) getTorre()).getAreaAlvo();
		g.setComposite(transp);
		g.setColor(Color.RED);
		if (attackPoint != null)
			g.fillOval(attackPoint.x - 20, attackPoint.y - 20, 40, 40);
		g.fillOval(attackPoint.x - 20, attackPoint.y - 20, 40, 40);
		g.setComposite(oldComp);
	}

	@Override
	public void mouseReleased(Point p) {
		((TorreMorteiro) getTorre()).setAreaAlvo(p);
	}

	@Override
	public void mouseDragged(Point p) {
		((TorreMorteiro) getTorre()).setAreaAlvo(p);
	}

}
