package torre;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import bloon.Bloon;
import prof.jogos2D.image.*;
import prof.jogos2D.util.DetectorColisoes;
import prof.jogos2D.util.ImageLoader;
import torre.projetil.Dardo;
import torre.projetil.Projetil;

/**
 * Classe que representa uma torre Macaco. Esta torre manda um dardo com dano de
 * 2. Só dispara quando tem bloons dentro do seu raio de ação e atira para o
 * bloon de acordo com o seu modo de ataque.
 */
public class TorreMacaco extends TorreDefault {

	/**
	 * Cria a torre macaco
	 * 
	 * @param img a imagem da torre
	 */
	public TorreMacaco(BufferedImage img) {
		super(new ComponenteMultiAnimado(new Point(50, 50), img, 2, 4, 3), 30, 8, new Point(15, 15), 100);
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

		// ver quais os bloons que estão ao alcance
		List<Bloon> alvosPossiveis = getBloonsInRadius(bloons, getComponente().getPosicaoCentro(), getRaioAcao());
		if (alvosPossiveis.size() == 0)
			return new Projetil[0];
		// TODO FEITO remover este switch e suportar os restantes modos de ataque
		// ver a posição do centro para o teste de estar perto
		Point centro = getComponente().getPosicaoCentro();
		
		// determinar a posição do bloon alvo, consoante o método de ataque
		Point posAlvo = getAtaque().escolherPosicao(alvosPossiveis, centro);


		if (posAlvo == null)
			return new Projetil[0];

		// ver o ângulo que o alvo faz com a torre, para assim rodar esta
		double angle1 = DetectorColisoes.getAngulo(posAlvo, anim.getPosicaoCentro());
		anim.setAngulo(angle1);

		// ajustar o ângulo
		double angle = angle1;

		// se vai disparar daqui a pouco, começamos já com a animação de ataque
		// para sincronizar a frame de disparo com o disparo real
		sincronizarFrameDisparo(anim);

		// se ainda não está na altura de disparar, não dispara
		if (!podeDisparar())
			return new Projetil[0];

		// disparar
		resetTempoDisparar();

		// primeiro calcular o ponto de disparo
		Point disparo = getPontoDisparo();
		double cosA = Math.cos(angle);
		double senA = Math.sin(angle);
		int px = (int) (disparo.x * cosA - disparo.y * senA);
		int py = (int) (disparo.y * cosA + disparo.x * senA); // repor o tempo de disparo
		Point shoot = new Point(centro.x + px, centro.y + py);

		// depois criar os projéteis
		Projetil p[] = new Projetil[1];
		ComponenteVisual img = new ComponenteAnimado(new Point(),
				(BufferedImage) ImageLoader.getLoader().getImage("data/torres/dardo.gif"), 2, 2);
		p[0] = new Dardo(img, angle, 10, 2);
		p[0].setPosicao(shoot);
		p[0].setAlcance(getRaioAcao() + 30);
		return p;
	}

}
