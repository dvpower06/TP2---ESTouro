package bloon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Bloon que cria outros bloons. A criação de bloons é aleatória, dentro de uma
 * lista de bloons prováveis.
 */
public class BloonFabricante extends BloonSimples {
    // a lista de bloons prováveis de serem criados
    private List<Bloon> provaveis = new ArrayList<>();
    private int ritmoCriacao; // ritmo de criação
    private int proximaCriacao; // próximo ciclo de criação

    /**
     * Cria um bloon que fabrica outros bloons
     * 
     * @param imagem       imagem do bloon
     * @param imagemPop    imagem de quando o bloon rebenta
     * @param veloc        velocidade de deslocamento
     * @param resist       resistência do bloon
     * @param valor        valor
     * @param ritmoCriacao de quantos em quantos ciclos cria um novo bloon
     */
    public BloonFabricante(ComponenteVisual imagem, ComponenteVisual imagemPop, float veloc, int resist, int valor,
            int ritmoCriacao) {
        super(imagem, imagemPop, veloc, resist, valor);
        this.ritmoCriacao = ritmoCriacao;
        this.proximaCriacao = ritmoCriacao;
    }

    /**
     * Adiciona um bloon à lista dos bloons prováveis
     * 
     * @param b o bloon a poder ser criado
     */
    public void addBloonProvavel(Bloon b) {
        provaveis.add(b);
    }

    @Override
    public void mover() {
        super.mover();
        // se por acaso já saiu não faz nada
        if (getResistencia() <= 0)
            return;
        proximaCriacao--;
        if (proximaCriacao <= 0) {
            // decidir aleatoriamente qual o bloon a "disparar"
            int idx = ThreadLocalRandom.current().nextInt(provaveis.size());
            // colocar o bloon um pouco à frente deste
            int pathOffset = 3;
            int pos = getPosicaoNoCaminho();
            if (getCaminho().getPoint(pos + pathOffset) == null)
                pathOffset = 0;
            // TODO esta parte tem de ser revista pois está a usar repetidamente os mesmos
            // bloons
            Bloon escolhido = provaveis.get(idx);
            escolhido.setCaminho(getCaminho());
            getMundo().addBloonPendente(escolhido);
            escolhido.setPosicaoNoCaminho(pos + pathOffset);
            getObservers().forEach(o -> escolhido.addBloonObserver(o));
            proximaCriacao = ritmoCriacao;
        }
    }
}
