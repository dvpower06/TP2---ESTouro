package torre;

import java.awt.Image;
import java.awt.image.BufferedImage;

import prof.jogos2D.util.ImageLoader;

/**
 * Classe que trata da criação das várias torres. Esta classe existe que lidar
 * com a criação de todas as torres e assim impedir que as outras classes tenham
 * de ser alteradas quando se criam novas torres.
 */
public class TorreCreator {
	private ImageLoader loader = ImageLoader.getLoader();

	/**
	 * cria a torre com um dado nome
	 * 
	 * @param nome nome da torre a criar
	 * @return a torre criada, ou null se não existir torre com o nome dado
	 */
	public Torre criarTorrePorNome(String nome) {
		// TODO suportar também a Sniper
		switch (nome) {
			case "octo":
				return criarOctogonal();
			case "macaco":
				return criarMacaco();
			case "canhao":
				return criarCanhao();
			case "morteiro":
				return criarMorteiro();
			case "balista":
				return criarBalista();
			case "ninja":
				return criarNinja();
		}
		return null;
	}

	/** Cria uma torre octogonal */
	public Torre criarOctogonal() {
		Image img = loader.getImage("data/torres/octo/imagem.gif");
		return new TorreOctogonal((BufferedImage) img);
	}

	/** Cria uma torre macaco */
	public Torre criarMacaco() {
		Image img = loader.getImage("data/torres/macaco/imagem.gif");
		return new TorreMacaco((BufferedImage) img);
	}

	/** Cria uma torre canhão */
	public Torre criarCanhao() {
		Image img = loader.getImage("data/torres/canhao/imagem.gif");
		return new TorreCanhao((BufferedImage) img);
	}

	/** Cria uma torre morteiro */
	public Torre criarMorteiro() {
		Image img = loader.getImage("data/torres/morteiro/imagem.gif");
		return new TorreMorteiro((BufferedImage) img);
	}

	/** Cria uma torre balista */
	public Torre criarBalista() {
		Image img = loader.getImage("data/torres/balista/imagem.gif");
		return new TorreBalista((BufferedImage) img);
	}

	/** Cria uma torre ninja */
	public Torre criarNinja() {
		Image img = loader.getImage("data/torres/ninja/imagem.gif");
		return new TorreNinja((BufferedImage) img);
	}

}
