package game.manipulator;

import java.awt.Point;

import prof.jogos2D.util.DetectorColisoes;
import torre.*;

/**
 * Manipulador para a torre octogonal. Permite rodar a torre para obter uma
 * direção mais conveniente.
 */
public class ManipuladorOcto extends ManipuladorVazio {

	public ManipuladorOcto(Torre t) {
		super(t);
	}

	@Override
	public void mouseDragged(Point p) {
		double angulo = DetectorColisoes.getAngulo(getTorre().getComponente().getPosicaoCentro(), p);
		((TorreOctogonal) getTorre()).setAngle(angulo);
	}
}
