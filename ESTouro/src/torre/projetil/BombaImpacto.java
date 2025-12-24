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
 * classe que representa uma bomba que explode quando colide com um
 * bloon (exemplo: lançada pelo canhão, ninja, etc)
 */
public class BombaImpacto extends ProjetilDefault {

	private int raioExplosao = 50; // raio da explosão
	private boolean explodiu = false; // indica se a bomba explodiu ou não
										// usada para a animação da explosão

	private ComponenteVisual explosao; // imagem da explosão
	private Mundo mundo; // mundo onde a bomba se desloca

	/**
	 * Cria a bomba.
	 * 
	 * @param img     imagem da bomba
	 * @param dir     direção de deslocamento
	 * @param veloc   velocidade de deslocamento
	 * @param estrago quantidade de estrago que provoca nos bloons
	 * @param m       mundo onde se movimenta
	 */
	public BombaImpacto(ComponenteVisual img, double dir, double veloc, int estrago, Mundo m) {
		super(img, dir, veloc, estrago);
		BufferedImage exp = (BufferedImage) ImageLoader.getLoader().getImage("data/misc/explosao.png");
		explosao = new ComponenteAnimado(new Point(), exp, 2, 2);
		this.mundo = m;
	}

	@Override
	public void atualiza(List<Bloon> bloons) {
		if (explodiu)
			return;
		super.atualiza(bloons);

		// para cada bloon vai verificar se lhe bateu
		for (Bloon b : bloons) {
			if (b.getBounds().intersects(getComponente().getBounds())) {
				// a bomba explode e tira 2 a cada balão nas redondezas

				Point explosionCenter = getComponente().getPosicaoCentro();
				bloons.stream()
						.filter(be -> DetectorColisoes.intersectam(be.getBounds(), explosionCenter, raioExplosao))
						.forEach(be -> be.explode(getEstrago()));
				explodiu = true;
				explosao.setPosicaoCentro(getComponente().getPosicaoCentro());
				mundo.addFx(explosao);
				break; // sai do for exterior
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (!explodiu)
			super.draw(g);
	}
}
