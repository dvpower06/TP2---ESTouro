package bloon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import mundo.Caminho;
import mundo.Mundo;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Interface que define quais os métodos que todos os bloons devem implementar
 */
public interface Bloon {

	/**
	 * desenha o bloon
	 * 
	 * @param g onde desenhar o bloon
	 */
	public void desenhar(Graphics2D g);

	/**
	 * move o bloon
	 */
	public void mover();

	/**
	 * devolve o componente visual que representa este bloon
	 * 
	 * @return o componente visual que representa este bloon
	 */
	public ComponenteVisual getComponente();

	/**
	 * Retorna o componente visual para o efeito especial do balão rebentar
	 * 
	 * @return o componente visual do balão rebentar
	 */
	public ComponenteVisual getPopComponente();

	/**
	 * Define qual o caminho que o bloon percorre
	 * 
	 * @param rua o caminho a percorrer
	 */
	public void setCaminho(Caminho rua);

	/**
	 * Retorna o caminho em que o bloon se move
	 * 
	 * @return o caminho em que o bloon se move
	 */
	public Caminho getCaminho();

	/**
	 * indica em que posição do caminho está o bloon.
	 * 
	 * @return a posição do caminho em que está o bloon
	 */
	public int getPosicaoNoCaminho();

	/**
	 * coloca o bloon numa nova posição do caminho.
	 * 
	 * @param pos a nova posição
	 */
	public void setPosicaoNoCaminho(int pos);

	/**
	 * Altera a velocidade do bloon
	 * 
	 * @param veloc a nova velocidade
	 */
	public void setVelocidade(float veloc);

	/**
	 * Retorna a velocidade de deslocamento
	 * 
	 * @return a velocidade de deslocamento
	 */
	public float getVelocidade();

	/**
	 * define em que mundo se move o balão
	 * 
	 * @param w o novo mundo
	 */
	public void setMundo(Mundo w);

	/**
	 * Retorna o mundo onde se move o bloon
	 * 
	 * @return o mundo onde se move o bloon
	 */
	public Mundo getMundo();

	/**
	 * define a posição do écran onde se coloca o bloon
	 * 
	 * @param p a nova posição
	 */
	public void setPosicao(Point p);

	/**
	 * devolve o retângulo ocupado pelo bloon no écran
	 * 
	 * @return o retângulo ocupado pelo bloon no écran
	 */
	public Rectangle getBounds();

	/**
	 * O balão foi atingido por um projétil perfurante e pode estourar. Cada bloon
	 * suporta até um dado estrago, valor que é dado pela sua resistência.
	 * Se o bloon não aguentar o estrago estoura e devolve qual o
	 * estrago que não suportou.
	 * 
	 * @param estrago valor de estrago que atingiu o bloon
	 * @return o valor de estrago que o bloon não aguentou
	 */
	public int pop(int estrago);

	/**
	 * O balão foi atingido por uma explosão. Cada bloon suporta
	 * até um dado estrago, valor que é dado pela sua resistência.
	 * Se o bloon não aguentar o estrago estoura.
	 * 
	 * @param estrago valor de estrago que atingiu o bloon
	 */
	public void explode(int estrago);

	/**
	 * devolve a resistência atual do bloon.
	 * 
	 * @return a resistência atual do bloon.
	 */
	public int getResistencia();

	/**
	 * devolve o valor, ou seja, quanto vale o bloon em termos de pontos, dinheiro,
	 * etc
	 * 
	 * @return o valor do bloon
	 */
	public int getValor();

	/**
	 * define o valor do bloon, ou seja, quanto ele vale em termos de pontos,
	 * dinheiro, etc
	 * 
	 * @param val o novo valor do bloon
	 */
	public void setValor(int val);

	/**
	 * adiciona um observador ao bloon
	 * 
	 * @param bo o observador a adicionar
	 */
	public void addBloonObserver(BloonObserver bo);

	/**
	 * remove um observador do bloon
	 * 
	 * @param bo o observador a remover
	 */
	public void removeBloonObserver(BloonObserver bo);
}
