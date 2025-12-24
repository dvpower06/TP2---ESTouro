package bloon;

import java.util.ArrayList;
import java.util.List;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que representa um bloon que quando estoura liberta vários bloons que
 * estavam no seu interior.
 */
public class BloonMultiCamada extends BloonSimples {

	/** A lista com os bloons que serão libertados quando este se estoura */
	private List<Bloon> bloons = new ArrayList<>();

	/**
	 * Cria um bloon multicamada
	 * 
	 * @param img    a imagem do blon
	 * @param imgPop a imagem do bloon quando estoura
	 * @param veloc  a velocidade de movimento
	 * @param resist a resistência
	 * @param valor  o valor
	 */
	public BloonMultiCamada(ComponenteVisual img, ComponenteVisual imgPop, float veloc, int resist, int valor) {
		super(img, imgPop, veloc, resist, valor);
	}

	/**
	 * Adicona um bloon à lista dos que serão libertados quando este estoura.
	 * 
	 * @param b o bloon a adicionar
	 */
	public void addBloon(Bloon b) {
		bloons.add(b);
	}

	@Override
	public int pop(int damage) {
		if (getResistencia() <= 0)
			return damage;

		damage = sofreEstrago(damage);

		// ver se já saiu da pista, senão não faz nada
		if (getCaminho().getPoint(getPosicaoNoCaminho()) == null)
			return damage;

		if (getResistencia() > 0)
			return damage;

		libertarBloons();

		// assinalar este como estando rebentado
		notificarBloonEstourou();

		getPopComponente().setPosicaoCentro(getComponente().getPosicaoCentro());
		getMundo().addFx(getPopComponente());

		return damage;
	}

	@Override
	public void explode(int damage) {
		if (getResistencia() <= 0)
			return;

		damage = sofreEstrago(damage);

		// ver se já saiu da pista, senão não faz nada
		if (getCaminho().getPoint(getPosicaoNoCaminho()) == null)
			return;

		if (getResistencia() > 0)
			return;

		libertarBloons();

		// assinalar este como estando rebentado
		notificarBloonEstourou();

		getPopComponente().setPosicaoCentro(getComponente().getPosicaoCentro());
		getMundo().addFx(getPopComponente());
	}

	private void libertarBloons() {
		// colocar os bloons no local deste, ou à frente e atrás
		int pathOffset = 0;
		for (Bloon b : bloons) {
			b.setCaminho(getCaminho());
			getMundo().addBloonPendente(b);
			int pos = getPosicaoNoCaminho();
			b.setPosicaoNoCaminho(pos + pathOffset);
			pathOffset = pathOffset > 0 ? -pathOffset : -pathOffset + 2;
			// se estiver fora do caminho recoloca na posição inicial
			if (getCaminho().getPoint(pos + pathOffset) == null)
				pathOffset = 0;
			getObservers().forEach(o -> b.addBloonObserver(o));
		}
	}
}
