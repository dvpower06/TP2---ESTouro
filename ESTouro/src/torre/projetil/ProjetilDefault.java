package torre.projetil;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import bloon.Bloon;
import prof.jogos2D.image.ComponenteVisual;

/**
 * classe que define os comportamentos e variáveis comuns aos projéteis
 */
public class ProjetilDefault implements Projetil {

	private ComponenteVisual image; // aspeto do projétil

	private double dirAngulo; // direção do movimento
	private double velocidade; // velocidade do movimento
	private double x, y; // posição do projétil em double, por causa dos erros de precisão
	private int alcance; // raio de alcance
	private int distanciaViajada = 0; // quanto é que já viajou
	private int estrago; // qual o estrago que inflige

	/**
	 * Para criar um projétil deve-se indicar qual a sua imagem, direção, velocidade
	 * e estrago
	 * 
	 * @param img      imagem do projétil
	 * @param dirAngle direção do movimento
	 * @param veloc    velocidade de deslocamento
	 * @param estrago  estrago que inflige
	 */
	protected ProjetilDefault(ComponenteVisual img, double dirAngle, double veloc, int estrago) {
		this.image = img;
		this.dirAngulo = dirAngle;
		this.velocidade = veloc;
		this.estrago = estrago;
	}

	@Override
	public void atualiza(List<Bloon> bloons) {
		// actualizar a distância viajada
		distanciaViajada += velocidade;

		// se já andou mais do que o alcance permite pára de fazer estragos
		if (distanciaViajada > alcance)
			setEstrago(0);

		// actualiza a posição
		setPosition(x + velocidade * Math.cos(dirAngulo), y + velocidade * Math.sin(dirAngulo));
	}

	/**
	 * alterar a posição do projétil usando double para uma maior precisão
	 * 
	 * @param x a nova posição em x
	 * @param y a nova posição em y
	 */
	protected void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		// define qual a posição no écran
		getImagem().setPosicaoCentro(new Point((int) x /* + offsetx */, (int) y /* + offsety */));
	}

	@Override
	public void setPosicao(Point p) {
		setPosition(p.x, p.y);
	}

	@Override
	public void draw(Graphics2D g) {
		image.desenhar(g);
	}

	@Override
	public ComponenteVisual getComponente() {
		return image;
	}

	@Override
	public int getAlcance() {
		return alcance;
	}

	@Override
	public void setAlcance(int alcance) {
		this.alcance = alcance;
	}

	/**
	 * retorna o ângulo da direção de movimento
	 * 
	 * @return o ângulo da direção de movimento
	 */
	public double getDirAngulo() {
		return dirAngulo;
	}

	/**
	 * Define o ângulo da direção de movimento
	 * 
	 * @param anguloDir o ângulo da direção
	 */
	public void setDirAngulo(double anguloDir) {
		this.dirAngulo = anguloDir;
	}

	/**
	 * Retorna a imagem do projétil
	 * 
	 * @return a imagem do projétil
	 */
	public ComponenteVisual getImagem() {
		return image;
	}

	/**
	 * Define a imagem do projétil
	 * 
	 * @param desenho a nova imagem
	 */
	public void setImage(ComponenteVisual desenho) {
		this.image = desenho;
	}

	@Override
	public int getEstrago() {
		return estrago;
	}

	/**
	 * Altera o estrago do projétil
	 * 
	 * @param estrago o novo estrago
	 */
	public void setEstrago(int estrago) {
		this.estrago = estrago;
	}

	/**
	 * retorna a velocidade de movimento
	 * 
	 * @return a velocidade de movimento
	 */
	public double getVelocidade() {
		return velocidade;
	}

	/**
	 * define a velocidadde de movimento
	 * 
	 * @param veloc a nova velocidade
	 */
	public void setVelocidade(double veloc) {
		this.velocidade = veloc;
	}
}
