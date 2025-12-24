package game.manipulator;

import torre.*;

/**
 * Classe responsável por criar todos os manipuladores. Esta classe existe para
 * que qualquer alteração nos manipuladores seja feita aqui, de modo a
 * permitir que apenas esta classe seja alterada em todo o sistema
 */
public class ManipuladorCreator {

	/**
	 * cria um manipulador para a torre indicada
	 * 
	 * @param t a torre para a qual se pretende um manipulador
	 * @return o manipulador adequado à torre
	 */
	public static ManipuladorTorre criarManipulador(Torre t) {
		// TODO remover estes instanceof
		if (t instanceof TorreMacaco)
			return new ManipuladorVazio(t);
		else if (t instanceof TorreOctogonal)
			return new ManipuladorOcto(t);
		else if (t instanceof TorreCanhao)
			return new ManipuladorVazio(t);
		else if (t instanceof TorreMorteiro)
			return new ManipuladorMorteiro((TorreMorteiro) t);
		else if (t instanceof TorreBalista)
			return new ManipuladorBalista(t);
		else if (t instanceof TorreNinja)
			return new ManipuladorVazio(t);
		return null;
	}

}
