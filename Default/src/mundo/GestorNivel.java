package mundo;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import bloon.Bloon;
import bloon.BloonCreator;

/**
 * Classe que vai ser responsável pela gestão do nivel (ou round) como lê-lo e
 * gerar os bloons após o começo do round. A indicação da criação dos bloons é
 * dada por ciclos. A cada ciclo o gerador define uma lista dos bloons a criar
 */
public class GestorNivel {
	private int cicloAtual; // ciclo de criação atual
	private int proximoCiclo; // qual a próxima lista a ser usada para a criação
	private int premioRound; // qual o valor que o round tem se for ganho
	private BufferedImage fundo; // imagem de fundo do nível
	private String nomeImagemFundo; // nome da imagem de fundo do nível
	private List<CicloCriacaoInfo> ciclos = new ArrayList<>(); // lista das infos de criação
	private List<Caminho> caminhos = new ArrayList<>(); // Lista com os caminhos
	private BloonCreator bCreator = new BloonCreator(); // criador de bloons

	public GestorNivel() {
	}

	/**
	 * adiciona um caminho ao nível
	 * 
	 * @param c caminho a adicionar
	 */
	public void addCaminho(Caminho c) {
		caminhos.add(c);
	}

	/**
	 * remove um caminho
	 * 
	 * @param idx índice do caminho a remover
	 */
	public void removeCaminho(int idx) {
		caminhos.remove(idx);
	}

	/**
	 * devolve o número de caminhos
	 * 
	 * @return o número de caminhos
	 */
	public int getNumeroCaminhos() {
		return caminhos.size();
	}

	/**
	 * Retorna o caminho com um dado índice
	 * 
	 * @param idx o índice do caminho a pesquisar
	 * @return o caminho com o índice indicado
	 */
	public Caminho getCaminho(int idx) {
		return caminhos.get(idx);
	}

	/** limpa todos os caminhos do nível */
	public void limparCaminhos() {
		caminhos.clear();
	}

	/**
	 * define a imagem de fundo
	 * 
	 * @param fundo a nova imagem de fundo
	 */
	public void setFundo(BufferedImage fundo) {
		this.fundo = fundo;
	}

	/**
	 * retorna a imagem de fundo
	 * 
	 * @return a imagem de fundo
	 */
	public BufferedImage getFundo() {
		return fundo;
	}

	/**
	 * define o nome do ficheiro da imagem de fundo
	 * 
	 * @param nomeFundo o nome da nova imagem de fundo
	 */
	public void setNomeImagemFundo(String nomeFundo) {
		this.nomeImagemFundo = nomeFundo;
	}

	/**
	 * Retorna o nome do ficheiro da imagem de fundo
	 * 
	 * @return o nome do ficheiro da imagem de fundo
	 */
	public String getNomeImagemFundo() {
		return nomeImagemFundo;
	}

	/**
	 * Define o prémio que se recebe por terminar o nível
	 * 
	 * @param premio o prémio por terminar o nível
	 */
	public void setPremioRound(int premio) {
		this.premioRound = premio;
	}

	/**
	 * Retorna o prémio por terminar o nível
	 * 
	 * @return o prémio por terminar o nível
	 */
	public int getPremioRound() {
		return premioRound;
	}

	/** começar a geração de bloons */
	public void start() {
		cicloAtual = -1; // começar em -1 para que no método de criar se comece a somar logo os ticks
		proximoCiclo = 0; // o próximo tick a criar vai ser o zero
	}

	/**
	 * Retorna o ciclo de criação com o índice idx
	 * 
	 * @param idx o índice do ciclo de criação pretendido
	 * @return o ciclo de criação com o índice indicado
	 */
	public CicloCriacaoInfo getCicloCriacaoInfo(int idx) {
		return ciclos.get(idx);
	}

	/**
	 * Retorna o número de ciclos de criação que existem
	 * 
	 * @return o número de ciclos de criação que existem
	 */
	public int getNumeroCiclos() {
		return ciclos.size();
	}

	/**
	 * devolve uma lista com os bloons a criar neste ciclo
	 * 
	 * @return uma lista com os bloons a criar neste ciclo
	 */
	public List<Bloon> criarBloons() {
		// ver se ainda tem bloons para criar
		if (!aindaTemBloons())
			return List.of();

		// passou um ciclo
		cicloAtual++;

		// lista para os bloons a serem criados
		ArrayList<Bloon> criados = new ArrayList<Bloon>();

		// ver a informação neste ciclo sobre o que se cria
		// se esta linha for para ser criada numa altura diferente nao faz nada
		CicloCriacaoInfo ticker = ciclos.get(proximoCiclo);
		while (ticker.getCiclo() <= cicloAtual) {
			// cria um bloon igual ao que está na lista a criar
			Bloon criar = bCreator.criarBloon(ticker.getNomeBloon());
			if (criar != null) {
				// inicializar o bloon
				criar.setCaminho(caminhos.get(ticker.getCaminhoIdx())); // associar o bloon ao caminho
				criar.setPosicaoNoCaminho(0); // colocá-lo no início do caminho
				criados.add(criar);
			}
			// esta linha foi processada, passa para a próxima
			proximoCiclo++;
			if (proximoCiclo >= ciclos.size())
				break;
			ticker = ciclos.get(proximoCiclo);
		}

		// devolve os bloons
		return criados;
	}

	/**
	 * Retorna o ciclo onde ocorre o último ciclo de criação
	 * 
	 * @return o ciclo onde ocorre o último ciclo de criação
	 */
	public int getUltimoCicloCriacao() {
		return ciclos.getLast().getCiclo();
	}

	/**
	 * indica se ainda faltam criar bloons
	 * 
	 * @return true se ainda faltam criar bloons
	 */
	public boolean aindaTemBloons() {
		return proximoCiclo < ciclos.size();
	}

	/**
	 * adicionar um novo ciclo de criação
	 * 
	 * @param cinfo ciclo de criação a ser adicionado
	 */
	public void addCicloCriação(CicloCriacaoInfo cinfo) {
		int i = ciclos.size();
		// garantir que estão sempre por ordem dos ticks
		while (i > 0 && ciclos.get(i - 1).getCiclo() > cinfo.getCiclo())
			i--;
		ciclos.add(i, cinfo);
	}

	/**
	 * Remove um ciclo de criação de um dado caminho
	 * 
	 * @param cicloIdx   índice do ciclo de criação a remover
	 * @param caminhoIdx índice do caminho de onde remover o ciclo
	 */
	public void removeCicloCriacao(int cicloIdx, int caminhoIdx) {
		for (int i = ciclos.size() - 1; i >= 0; i--) {
			CicloCriacaoInfo ci = ciclos.get(i);
			if (ci.getCiclo() == cicloIdx && ci.getCaminhoIdx() == caminhoIdx) {
				ciclos.remove(i);
				return;
			}
		}
	}

	/** limpar os ciclos de criação */
	public void limparCiclosCriacao() {
		ciclos.clear();
	}
}
