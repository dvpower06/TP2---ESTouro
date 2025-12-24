package game.manipulator;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

import prof.jogos2D.util.DetectorColisoes;
import torre.*;

/**
 * Manipulador para a torre balista. Permite rodar a torre para obter uma
 * direção mais conveniente.
 */
public class ManipuladorBalista extends ManipuladorVazio {

	// composite para usar transparências nas miras
	private static final AlphaComposite transp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);

	public ManipuladorBalista(Torre t) {
		super(t);
	}

	@Override
	public void mouseDragged(Point p) {
		double angulo = DetectorColisoes.getAngulo(getTorre().getComponente().getPosicaoCentro(), p);
		((TorreBalista) getTorre()).setAnguloDisparo((float) angulo);
	}

	@Override
	public void desenhar(Graphics2D g) {
		Composite oldComp = g.getComposite();
		Point centro = getTorre().getComponente().getPosicaoCentro();
		Point attackPoint = ((TorreBalista) getTorre()).getMira();
		Line2D.Float l = new Line2D.Float(centro, attackPoint);

		g.setComposite(transp);
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(20));
		g.draw(l);
		g.setComposite(oldComp);
	}
}
