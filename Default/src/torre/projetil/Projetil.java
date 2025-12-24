package torre.projetil;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import bloon.Bloon;
import prof.jogos2D.image.*;

/**
 * Interface que define todo o comportamento que os projéteis devem ter.
 * Um projétil é atirado por uma torre e inflingirá um certo estrago nos bloons
 * (se lhes acertar)
 *
 */
public interface Projetil {

	/**
	 * desenha o projétil
	 * 
	 * @param g ambiente de desenho
	 */
	public void draw(Graphics2D g);

	/**
	 * efectua um ciclo de processamento
	 * 
	 * @param bloons bloons que estão presentes no mundo
	 */
	public void atualiza(List<Bloon> bloons);

	/**
	 * devolve o componente visual associado ao projétil
	 * 
	 * @return o componente visual associado ao projétil
	 */
	public ComponenteVisual getComponente();

	/**
	 * Indica qual o estrago que o projétil causa nos bloons
	 * 
	 * @return o estrago que o projétil causa nos bloons
	 */
	public int getEstrago();

	/**
	 * define em que posição do écran se deve colocar o projétil
	 * 
	 * @param p a nova posição
	 */
	public void setPosicao(Point p);

	/**
	 * devolve o alcance que o projétil possui
	 * 
	 * @return o alcance que o projétil possui
	 */
	public int getAlcance();

	/**
	 * define o alcance do projétil
	 * 
	 * @param alcance novo alcance do projétil
	 */
	public void setAlcance(int alcance);

}
