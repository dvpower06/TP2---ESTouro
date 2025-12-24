package torre;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import bloon.Bloon;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.DetectorColisoes;
import prof.jogos2D.util.ImageLoader;
import torre.projetil.BombaImpacto;
import torre.projetil.Dardo;
import torre.projetil.Projetil;

/**
 * Classe que representa a torre ninja. Esta torre dispara alternadamente 3
 * dardos ou 1 granada para os bloons de acordo com o seu modo de ataque.
 */
public class TorreNinja extends TorreDefault {

    private boolean dardos = false;

    /**
     * Cria uma torre ninja
     * 
     * @param img a imagem da torre
     */
    public TorreNinja(BufferedImage img) {
        super(new ComponenteMultiAnimado(new Point(50, 50), img, 2, 4, 3), 30, 8, new Point(20, 0), 100);
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
        dardos = !dardos; // inverter a vez
        if (dardos) {
            Projetil p[] = new Projetil[3];
            for (int i = 0; i < 3; i++) {
                ComponenteVisual img = new ComponenteAnimado(new Point(),
                        (BufferedImage) ImageLoader.getLoader().getImage("data/torres/dardo.gif"), 2, 2);
                p[i] = new Dardo(img, angle + (i - 1) * Math.PI / 6, 10, 3);
                p[i].setPosicao(shoot);
                p[i].setAlcance(getRaioAcao() + 30);
            }
            return p;
        } else {
            Projetil p[] = new Projetil[1];
            ComponenteVisual img = new ComponenteSimples(ImageLoader.getLoader().getImage("data/torres/bomba.gif"));
            p[0] = new BombaImpacto(img, angle, 12, 1, getMundo());
            p[0].setPosicao(shoot);
            p[0].setAlcance(getRaioAcao() + 20);
            return p;
        }
    }
}
