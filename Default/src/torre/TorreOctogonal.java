package torre;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import bloon.Bloon;
import prof.jogos2D.image.*;
import prof.jogos2D.util.ImageLoader;
import torre.projetil.Dardo;
import torre.projetil.Projetil;

/**
 * Classe que representa a torre octogonal. Esta torre dispara 8 dardos, um em
 * cada direção dos seus lançadores. Só dispara quando tem bloons dentro do seu
 * raio de ação.
 */
public class TorreOctogonal extends TorreDefault {

	private double baseAngle = 0;

	public TorreOctogonal(BufferedImage img) {
		super(new ComponenteMultiAnimado(new Point(), img, 2, 4, 2),
				20, 6, new Point(0, 0), 100);
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
		Point posAlvo = null;
		// ver quais os bloons que estão ao alcance
		List<Bloon> alvosPossiveis = getBloonsInRadius(bloons, getComponente().getPosicaoCentro(), getRaioAcao());
		if (alvosPossiveis.size() == 0)
			return new Projetil[0];
		// TODO remover este switch e suportar os restantes modos de ataque
		// ver a posição do centro para o teste de estar perto
		Point centro = getComponente().getPosicaoCentro();
		switch (getModoAtaque()) {
			case ATACA_PRIMEIRO:
				Bloon bp = bloons.stream().max((b1, b2) -> b1.getPosicaoNoCaminho() - b2.getPosicaoNoCaminho()).get();
				posAlvo = bp.getComponente().getPosicaoCentro();
				break;
			case ATACA_ULTIMO:
				Bloon bu = bloons.stream().min((b1, b2) -> b1.getPosicaoNoCaminho() - b2.getPosicaoNoCaminho()).get();
				posAlvo = bu.getComponente().getPosicaoCentro();
				break;
			case ATACA_JUNTOS:
				Map<Integer, List<Bloon>> posicoes = bloons.stream()
						.collect(Collectors.groupingBy(b -> b.getPosicaoNoCaminho() / 20));
				int posicaoComMais = Collections.max(posicoes.keySet(),
						(k1, k2) -> posicoes.get(k1).size() - posicoes.get(k2).size());
				Bloon bj = posicoes.get(posicaoComMais).getFirst();
				posAlvo = bj.getComponente().getPosicaoCentro();
				break;
			case ATACA_PERTO:
				posAlvo = bloons.stream().map(b -> b.getComponente().getPosicaoCentro())
						.min((p1, p2) -> Double.compare(p1.distance(centro), p2.distance(centro)))
						.get();
				break;
		}
		if (posAlvo == null)
			return new Projetil[0];

		// ver o ângulo que o alvo faz com a torre, para assim rodar esta
		double angle = (double) 0; // neste caso o ângulo não interessa pois são 8

		// se vai disparar daqui a pouco, começamos já com a animação de ataque
		// para sincronizar a frame de disparo com o disparo real
		sincronizarFrameDisparo(anim);

		// se ainda não está na altura de disparar, não dispara
		if (!podeDisparar())
			return new Projetil[0];

		// disparar
		resetTempoDisparar();

		// primeiro calcular o ponto de disparo
		Point centro1 = getComponente().getPosicaoCentro();
		Point disparo = getPontoDisparo();
		double cosA = Math.cos(angle);
		double senA = Math.sin(angle);
		int px = (int) (disparo.x * cosA - disparo.y * senA);
		int py = (int) (disparo.y * cosA + disparo.x * senA); // repor o tempo de disparo
		Point shoot = new Point(centro1.x + px, centro1.y + py);

		// depois criar os projéteis
		/// disparar os 8 dardos
		Projetil p[] = new Projetil[8];
		double angulo = baseAngle + Math.PI / 2;
		double incAng = Math.PI / 4;
		for (int i = 0; i < 8; i++) {
			ComponenteVisual img = new ComponenteAnimado(new Point(),
					(BufferedImage) ImageLoader.getLoader().getImage("data/torres/dardo.gif"), 2, 2);
			p[i] = new Dardo(img, angulo, 8, 1);
			p[i].setPosicao(shoot);
			p[i].setAlcance(getRaioAcao() + 15);
			angulo -= incAng;
		}
		return p;
	}

	/**
	 * Altera o ângulo da octo
	 * 
	 * @param angle o novo ângulo
	 */
	public void setAngle(double angle) {
		getComponente().setAngulo(angle);
		baseAngle = angle;
	}
}
