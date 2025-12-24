package mundo;

/**
 * Classe que guarda a informação sobre um ciclo de criação de bloons.
 * Um ciclo de criação indica em que ciclo do jogo é para ser criado um bloon, a
 * que caminho deve ser adicionado e qual o nome do bloon a criar.
 * Esta classe é imutável.
 */
public final class CicloCriacaoInfo {

	private final int ciclo; // em que ciclo vai ser criado o bloon
	private final int caminhoIdx; // índice do caminho a que o bloon vai ser adicionado
	private final String nomeBloon; // nome do bloon a ser criado

	/**
	 * Constroi um ciclo de criação
	 * 
	 * @param quando        altura (em ciclos de processamento) em que deve ser
	 *                      criado o bloon
	 * @param indiceCaminho em que caminho deve ser criado o bloon
	 * @param nomeBloon     o nome do bloon a criar
	 */
	public CicloCriacaoInfo(int quando, int indiceCaminho, String nomeBloon) {
		this.ciclo = quando;
		this.caminhoIdx = indiceCaminho;
		this.nomeBloon = nomeBloon;
	}

	/** devolve o ciclo de criação */
	public int getCiclo() {
		return ciclo;
	}

	/** devolve o indice do caminho ao qual o bloon será adicionado */
	public int getCaminhoIdx() {
		return caminhoIdx;
	}

	/** devolve o nome do bloon a ser criado */
	public String getNomeBloon() {
		return nomeBloon;
	}
}