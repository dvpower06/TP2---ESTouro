package torre;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.List;

import bloon.Bloon;
import prof.jogos2D.image.*;
import prof.jogos2D.util.ImageLoader;
import torre.projetil.Dardo;
import torre.projetil.Projetil;

/**
 * Classe que representa a torre balista. Esta torre dispara 1 dardo enorme e
 * potente na direção especificada pelo jogador.
 */
public class TorreBalista extends TorreDefault {

	/** ponto para onde a balista faz pontaria */
	private Point mira;

	/**
	 * Cria uma balista.
	 * 
	 * @param img imagem da balista
	 */
	public TorreBalista(BufferedImage img) {
		super(new ComponenteMultiAnimado(new Point(), img, 2, 4, 2),
				20, 0, new Point(20, -3), 100);
		setAnguloDisparo(0);
	}

	/**
	 * Define o ângulo de disparo da balista
	 * 
	 * @param angulo o novo ângulo
	 */
	public void setAnguloDisparo(float angulo) {
		getComponente().setAngulo(angulo);
		definirMira(angulo);
	}

	/**
	 * Define a pontaria, isto é, a posição para onde a balusta irá apontar
	 * 
	 * @param angulo angulo do disparo, para poder calcular a área de ataque
	 */
	private void definirMira(double angulo) {
		double cos = Math.cos(angulo);
		double sin = Math.sin(angulo);
		Point centro = getComponente().getPosicaoCentro();
		mira = new Point((int) (centro.x + getRaioAcao() * cos), (int) (centro.y + getRaioAcao() * sin));
	}

	/**
	 * Retorna o ponto para onde a balista irá disparar
	 * 
	 * @return o ponto para onde a balista irá disparar
	 */
	public Point getMira() {
		return mira;
	}

	@Override
	public void setPosicao(Point p) {
		super.setPosicao(p);
		definirMira(getComponente().getAngulo());
	}

	@Override
	public void desenhaRaioAcao(Graphics2D g) {
		Point centro = getComponente().getPosicaoCentro();
		Point mira = getMira();
		Composite oldComp = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g.setColor(Color.BLUE);
		Line2D.Float l = new Line2D.Float(centro, mira);
		g.setStroke(new BasicStroke(20));
		g.draw(l);
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(18));
		g.draw(l);
		g.setComposite(oldComp);
	}

	@Override
	public Projetil[] atacar(List<Bloon> bloons) {
		atualizarCicloDisparo();

		// vamos buscar o desenho pois vai ser preciso várias vezes
		ComponenteMultiAnimado anim = getComponente();

		// já acabou a animação de disparar? volta à animação de pausa
		if (anim.getAnim() == ATAQUE_ANIM && anim.numCiclosFeitos() >= 1) {
			anim.setAnim(PAUSA_ANIM);
		}

		// determinar a posição do bloon alvo, consoante o método de ataque
		List<Bloon> alvosPossiveis = getBloonsInLine(bloons, getComponente().getPosicaoCentro(), getMira());
		Point posAlvo = (alvosPossiveis.size() == 0) ? null : mira;

		if (posAlvo == null)
			return new Projetil[0];

		// ver o ângulo que o alvo faz com a torre, para assim rodar esta
		double angle = anim.getAngulo();

		// se vai disparar daqui a pouco, começamos já com a animação de ataque
		// para sincronizar a frame de disparo com o disparo real
		sincronizarFrameDisparo(anim);

		// se ainda não está na altura de disparar, não dispara
		if (!podeDisparar())
			return new Projetil[0];

		// disparar
		resetTempoDisparar();

		// primeiro calcular o ponto de disparo
		Point centro = getComponente().getPosicaoCentro();
		Point disparo = getPontoDisparo();
		double cosA = Math.cos(angle);
		double senA = Math.sin(angle);
		int px = (int) (disparo.x * cosA - disparo.y * senA);
		int py = (int) (disparo.y * cosA + disparo.x * senA); // repor o tempo de disparo
		Point shoot = new Point(centro.x + px, centro.y + py);

		// depois criar os projéteis
		Projetil p[] = new Projetil[1];
		ComponenteVisual img = new ComponenteSimples(ImageLoader.getLoader().getImage("data/torres/seta.gif"));
		p[0] = new Dardo(img, angle, 10, 20);
		p[0].setPosicao(shoot);
		p[0].setAlcance(getRaioAcao() + 50);
		return p;
	}

	@Override
	public Torre clone() {
		TorreBalista copia = (TorreBalista) super.clone();
		copia.mira = new Point(mira);
		return copia;
	}
}
