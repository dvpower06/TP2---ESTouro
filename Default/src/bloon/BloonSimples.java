package bloon;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import mundo.*;
import prof.jogos2D.image.*;

/**
 * Classe que representa um bloon simples. Também é a superclasse dos restantes
 * tipos de bloons, pelo que define vários métodos e campos comuns a todos os
 * bloons. Contém também alguns métodos auxiliares para as subclasses.
 */
public class BloonSimples implements Bloon {

	/**
	 * variáveis comuns aos bloons
	 */
	private ComponenteVisual imagem; // o desenho do bloon
	private ComponenteVisual imagemPop; // o desenho do bloon quando rebenta
	private float velocidade = 0; // a velocidade do bloon
	private Caminho caminho; // o caminho onde o bloon se move
	private int posCaminho = 0; // a posição no caminho
	private Mundo mundo; // o mundo em que se move o bloon
	private int resistencia; // o valor de resistência do bloon
	private int valor; // o valor do bloon

	private List<BloonObserver> obs = new ArrayList<BloonObserver>();

	BloonSimples(ComponenteVisual imagem, ComponenteVisual imagemPop, float speed, int resistence, int value) {
		this.imagem = Objects.requireNonNull(imagem);
		this.imagemPop = Objects.requireNonNull(imagemPop);
		this.velocidade = speed;
		this.resistencia = resistence;
		this.valor = value;
	}

	@Override
	public void desenhar(Graphics2D g) {
		imagem.desenhar(g);
	}

	/**
	 * permite a um componente definir qual o seu aspeto
	 * 
	 * @param v o novo aspeto do componente
	 */
	protected void setComponente(ComponenteVisual v) {
		imagem = v;
	}

	@Override
	public ComponenteVisual getComponente() {
		return imagem;
	}

	@Override
	public ComponenteVisual getPopComponente() {
		return imagemPop;
	}

	@Override
	public void setVelocidade(float veloc) {
		this.velocidade = veloc;
	}

	@Override
	public float getVelocidade() {
		return velocidade;
	}

	@Override
	public int getPosicaoNoCaminho() {
		return posCaminho;
	}

	@Override
	public void setPosicaoNoCaminho(int pos) {
		this.posCaminho = pos;
		setPosicao(caminho.getPoint(pos));
	}

	@Override
	public void setMundo(Mundo m) {
		mundo = m;
	}

	@Override
	public Mundo getMundo() {
		return mundo;
	}

	@Override
	public int getResistencia() {
		return resistencia;
	}

	@Override
	public void setPosicao(Point p) {
		imagem.setPosicaoCentro(p);
	}

	@Override
	public void mover() {
		posCaminho += velocidade;
		Point p = caminho.getPoint(posCaminho);
		if (p == null) {
			// dizer que saiu do caminho,
			notificarBloonEscapou();

			// colocar a resistencia a 0 para indicar que deve ser apagado
			resistencia = 0;
			return;
		}
		setPosicao(p);
	}

	@Override
	public void setCaminho(Caminho caminho) {
		this.caminho = caminho;
	}

	@Override
	public Caminho getCaminho() {
		return caminho;
	}

	@Override
	public Rectangle getBounds() {
		return imagem.getBounds();
	}

	@Override
	public int getValor() {
		return valor;
	}

	@Override
	public void setValor(int val) {
		valor = val;
	}

	@Override
	public int pop(int estrago) {
		if (resistencia <= 0)
			return estrago;
		estrago = sofreEstrago(estrago);
		// se não aguentou o estrago todo estoura
		if (resistencia == 0) {
			notificarBloonEstourou();
			imagemPop.setPosicaoCentro(imagem.getPosicaoCentro());
			getMundo().addFx(imagemPop);
		}
		// retorna o estrago que sobrou
		return estrago;
	}

	@Override
	public void explode(int damage) {
		if (resistencia <= 0)
			return;
		sofreEstrago(damage);
		// se não aguentou o estrago todo estoura
		if (resistencia == 0) {
			notificarBloonEstourou();
			imagemPop.setPosicaoCentro(imagem.getPosicaoCentro());
			getMundo().addFx(imagemPop);
		}
	}

	/**
	 * Diminui a resistência do bloon de acordo com o impacto que recebeu
	 * 
	 * @param estrago o estrago infligido pelo impacto
	 * @return o estrago sobrante
	 */
	protected int sofreEstrago(int estrago) {
		resistencia -= estrago;
		if (resistencia < 0) {
			estrago = -resistencia;
			resistencia = 0;
			return estrago;
		}
		return 0;
	}

	@Override
	public void addBloonObserver(BloonObserver bo) {
		obs.add(bo);
	}

	@Override
	public void removeBloonObserver(BloonObserver bo) {
		obs.remove(bo);
	}

	/**
	 * retorna a lista de observadores
	 * 
	 * @return a lista de observadores
	 */
	protected List<BloonObserver> getObservers() {
		return Collections.unmodifiableList(obs);
	}

	/**
	 * notifica que o bloon estourou
	 */
	protected void notificarBloonEstourou() {
		for (int i = obs.size() - 1; i >= 0; i--)
			obs.get(i).bloonEstourou(this);
	}

	/**
	 * Notifica que o bloon consegiu chegar ao fim do caminho
	 */
	protected void notificarBloonEscapou() {
		for (int i = obs.size() - 1; i >= 0; i--)
			obs.get(i).bloonEscapou(this);
	}
}
