package torre.projetil;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import bloon.Bloon;
import prof.jogos2D.image.*;
import prof.jogos2D.util.DetectorColisoes;
import prof.jogos2D.util.ImageLoader;
import mundo.Mundo;

/**
 * Classe que representa uma bomba que explode quando chega a uma área
 * (exemplo: é lançada pelo morteiro)
 */
public class BombaDirigida extends ProjetilDefault {
	private int raioExplosao = 50; // raio da explosão
	private boolean explodiu = false; // indica se a bomba explodiu ou não
										// usada para a animação da explosão

	private Point destino; // destino da bomba
	private ComponenteVisual explosao; // imagem visual da exposão
	private Mundo mundo; // mundo onde a bomba se move

	/**
	 * Cria a bomba
	 * 
	 * @param img     imagem da bomba
	 * @param dir     direção do movimento
	 * @param veloc   velocidade do movimento
	 * @param estrago estrago que inflinge nos bloons
	 * @param m       mundo onde se movimenta
	 */
	public BombaDirigida(ComponenteVisual img, double dir, double veloc, int estrago, Mundo m) {
		super(img, dir, veloc, estrago);
		BufferedImage exp = (BufferedImage) ImageLoader.getLoader().getImage("data/misc/explosao.png");
		explosao = new ComponenteAnimado(new Point(), exp, 2, 2);
		destino = new Point(100, 100); // ponto por defeito, irá ser alterado pela torre
		this.mundo = m;
	}

	/**
	 * define o ponto de ataque da bomba
	 * 
	 * @param p ponto de ataque
	 */
	public void setDestino(Point p) {
		destino = p;
	}

	@Override
	public void atualiza(List<Bloon> bloons) {
		if (explodiu)
			return;
		super.atualiza(bloons);

		// ver se já chegou ao destino
		if (getComponente().getPosicaoCentro().distance(destino) <= getVelocidade()) {
			// a bomba explode e tira 2 a cada balão nas redondezas
			Point explosionCenter = getComponente().getPosicaoCentro();
			for (int k = bloons.size() - 1; k >= 0; k--) {
				Bloon be = bloons.get(k);
				if (DetectorColisoes.intersectam(be.getBounds(), explosionCenter, raioExplosao)) {
					be.explode(getEstrago());
				}
			}
			explosao.setPosicaoCentro(getComponente().getPosicaoCentro());
			mundo.addFx(explosao);
			explodiu = true;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (!explodiu)
			super.draw(g);
	}
}
