package bloon;

import java.awt.Point;
import java.awt.image.BufferedImage;

import prof.jogos2D.image.*;
import prof.jogos2D.util.ImageLoader;

/**
 * Classe respsonável por criar os vários bloons presentes no jogo
 */
public class BloonCreator {

	private ImageLoader loader = ImageLoader.getLoader();

	/**
	 * método que cria o bloon com um determinado nome de código
	 * 
	 * @param nome nome de código do bloon a criar
	 * @return o bloon criado, ou null caso seja um nome inválido
	 */
	public Bloon criarBloon(String nome) {
		switch (nome) {
			case "vermelho":
				return criarVermelho();
			case "azul":
				return criarAzul();
			case "verde":
				return criarVerde();
			case "amarelo":
				return criarAmarelo();
			case "rosa":
				return criarRosa();
			case "metal":
				return criarMetal();
			case "barro":
				return criarBarro();
			case "preto":
				return criarPreto();
			case "branco":
				return criarBranco();
			case "zep_azul":
				return criarZeppelinAzul();
			case "zep_verde":
				return criarZeppelinVerde();
			case "zep_amarelo":
				return criarZeppelinAmarelo();
			case "zep_rosa":
				return criarZeppelinRosa();
			case "zep_metal":
				return criarZeppelinMetal();
			case "zep_preto":
				return criarZeppelinPreto();
		}
		return null;
	}

	private ComponenteVisual getImagem(String nome) {
		return new ComponenteSimples(loader.getImage("data/bloons/" + nome + "/imagem.gif"));
	}

	private ComponenteVisual getImagemPop() {
		return new ComponenteAnimado(new Point(),
				(BufferedImage) loader.getImage("data/misc/pop.png"), 2, 2);
	}

	private ComponenteVisual getImagemPopZep() {
		return new ComponenteAnimado(new Point(),
				(BufferedImage) loader.getImage("data/misc/pop_zep.png"), 2, 2);
	}

	private ComponenteVisual getImagemArmadura() {
		return new ComponenteSimples(new Point(), loader.getImage("data/misc/armadura.png"));
	}

	private ComponenteVisual getImagemEscudo() {
		return new ComponenteSimples(new Point(), loader.getImage("data/misc/escudo.png"));
	}

	public Bloon criarVermelho() {
		ComponenteVisual imagem = new ComponenteSimples(loader.getImage("data/bloons/vermelho/imagem.gif"));
		ComponenteVisual imagemPop = new ComponenteAnimado(new Point(),
				(BufferedImage) loader.getImage("data/misc/pop.png"), 2, 2);
		return new BloonSimples(imagem, imagemPop, 3, 1, 2);
	}

	public Bloon criarVermelhoRapido() {
		ComponenteVisual imagem = new ComponenteSimples(loader.getImage("data/bloons_fast/vermelho/imagem.gif"));
		ComponenteVisual imagemPop = getImagemPop();
		return new BloonSimples(imagem, imagemPop, 8, 1, 2);
	}

	public Bloon createAzulRapido() {
		ComponenteVisual imagem = new ComponenteSimples(loader.getImage("data/bloons_fast/azul/imagem.gif"));
		ComponenteVisual imagemPop = getImagemPop();
		return new BloonSimples(imagem, imagemPop, 10, 2, 3);
	}

	public Bloon criarAzul() {
		ComponenteVisual imagem = new ComponenteSimples(loader.getImage("data/bloons/azul/imagem.gif"));
		ComponenteVisual imagemPop = new ComponenteAnimado(new Point(),
				(BufferedImage) loader.getImage("data/misc/pop.png"), 2, 2);

		BloonMultiCamada blue = new BloonMultiCamada(imagem, imagemPop, 4, 2, 3);
		blue.addBloon(criarVermelho());
		return blue;
	}

	public Bloon criarVerde() {
		ComponenteVisual imagem = new ComponenteSimples(loader.getImage("data/bloons/verde/imagem.gif"));
		ComponenteVisual imagemPop = new ComponenteAnimado(new Point(),
				(BufferedImage) loader.getImage("data/misc/pop.png"), 2, 2);
		BloonMultiCamada green = new BloonMultiCamada(imagem, imagemPop, 4.5f, 3, 4);
		green.addBloon(criarAzul());
		return green;
	}

	public Bloon criarAmarelo() {
		ComponenteVisual imagem = new ComponenteSimples(loader.getImage("data/bloons/amarelo/imagem.gif"));
		ComponenteVisual imagemPop = new ComponenteAnimado(new Point(),
				(BufferedImage) loader.getImage("data/misc/pop.png"), 2, 2);
		BloonMultiCamada yellow = new BloonMultiCamada(imagem, imagemPop, 5, 4, 5);
		yellow.addBloon(criarVerde());
		yellow.addBloon(criarVermelhoRapido());
		return yellow;
	}

	public Bloon criarRosa() {
		ComponenteVisual imagem = getImagem("rosa");
		ComponenteVisual imagemPop = getImagemPop();
		BloonMultiCamada pink = new BloonMultiCamada(imagem, imagemPop, 5, 4, 6);
		pink.addBloon(criarAmarelo());
		pink.addBloon(criarVermelhoRapido());
		pink.addBloon(criarVermelhoRapido());
		pink.addBloon(createAzulRapido());
		return pink;
	}

	public Bloon criarMetal() {
		ComponenteVisual imagem = getImagem("metal");
		ComponenteVisual imagemPop = getImagemPop();
		// TODO metal tem de ser imune a perfurantes
		BloonMultiCamada metal = new BloonMultiCamada(imagem, imagemPop, 3, 5, 7);
		metal.addBloon(criarRosa());
		metal.addBloon(criarRosa());
		return metal;
	}

	public Bloon criarBarro() {
		ComponenteVisual imagem = getImagem("barro");
		ComponenteVisual imagemPop = getImagemPop();
		// TODO barro tem de ser imune a perfurantes
		BloonMultiCamada barro = new BloonMultiCamada(imagem, imagemPop, 3.5f, 4, 7);
		barro.addBloon(criarPreto());
		barro.addBloon(criarPreto());
		return barro;
	}

	public Bloon criarPreto() {
		ComponenteVisual imagem = getImagem("preto");
		ComponenteVisual imagemPop = getImagemPop();
		// TODO preto tem de ser imune a explosões
		BloonMultiCamada black = new BloonMultiCamada(imagem, imagemPop, 4, 6, 7);
		black.addBloon(criarRosa());
		black.addBloon(criarRosa());
		return black;
	}

	public Bloon criarBranco() {
		ComponenteVisual imagem = getImagem("branco");
		ComponenteVisual imagemPop = getImagemPop();
		// TODO branco tem de ser imune a explosões
		BloonMultiCamada black = new BloonMultiCamada(imagem, imagemPop, 4, 6, 7);
		black.addBloon(criarMetal());
		black.addBloon(criarMetal());
		return black;
	}

	public Bloon criarZeppelinAzul() {
		ComponenteVisual imagem = getImagem("zep_azul");
		ComponenteVisual imagemPop = getImagemPopZep();
		BloonFabricante azulZep = new BloonFabricante(imagem, imagemPop, 2f, 18, 12, 30);
		azulZep.addBloonProvavel(criarVermelho());
		azulZep.addBloonProvavel(criarAzul());
		return azulZep;
	}

	public Bloon criarZeppelinVerde() {
		ComponenteVisual imagem = getImagem("zep_verde");
		ComponenteVisual imagemPop = getImagemPopZep();
		BloonFabricante verdeZep = new BloonFabricante(imagem, imagemPop, 2.3f, 22, 20, 30);
		verdeZep.addBloonProvavel(criarVermelho());
		verdeZep.addBloonProvavel(criarAzul());
		verdeZep.addBloonProvavel(criarVerde());
		verdeZep.addBloonProvavel(criarVerde());
		return verdeZep;
	}

	public Bloon criarZeppelinAmarelo() {
		ComponenteVisual imagem = getImagem("zep_amarelo");
		ComponenteVisual imagemPop = getImagemPopZep();
		BloonFabricante verdeZep = new BloonFabricante(imagem, imagemPop, 2.8f, 24, 25, 30);
		verdeZep.addBloonProvavel(criarAzul());
		verdeZep.addBloonProvavel(criarVerde());
		verdeZep.addBloonProvavel(criarAmarelo());
		return verdeZep;
	}

	public Bloon criarZeppelinRosa() {
		ComponenteVisual imagem = getImagem("zep_rosa");
		ComponenteVisual imagemPop = getImagemPopZep();
		BloonFabricante verdeZep = new BloonFabricante(imagem, imagemPop, 3.4f, 28, 30, 30);
		verdeZep.addBloonProvavel(criarAzul());
		verdeZep.addBloonProvavel(criarVerde());
		verdeZep.addBloonProvavel(criarAmarelo());
		verdeZep.addBloonProvavel(criarAmarelo());
		verdeZep.addBloonProvavel(criarRosa());
		verdeZep.addBloonProvavel(criarRosa());
		return verdeZep;
	}

	public Bloon criarZeppelinMetal() {
		ComponenteVisual imagem = getImagem("zep_metal");
		ComponenteVisual imagemPop = getImagemPopZep();
		ComponenteVisual armaduraImg = getImagemArmadura();

		// TODO o Zeppelin metal tem de ser imune a perfurantes
		BloonFabricante metalZep = new BloonFabricante(imagem, imagemPop, 2.3f, 30, 45, 30);

		// TODO o verde e o amarelo têm de ter uma armadura que rebenta ao fim de 8
		// contactos
		Bloon verde = criarVerde();
		metalZep.addBloonProvavel(verde);
		Bloon amarelo = criarAmarelo();
		metalZep.addBloonProvavel(amarelo);
		return metalZep;
	}

	public Bloon criarZeppelinPreto() {
		ComponenteVisual imagem = getImagem("zep_preto");
		ComponenteVisual imagemPop = getImagemPopZep();
		ComponenteVisual escudoImg = getImagemEscudo();

		// TODO o Zeppelin preto tem de ser imune a explosões
		BloonFabricante pretoZep = new BloonFabricante(imagem, imagemPop, 2.7f, 30, 45, 30);

		// TODO o amarelo e o rosa devem ter um escudo que parte ao fim de 12 contactos
		Bloon amarelo = criarAmarelo();
		pretoZep.addBloonProvavel(amarelo);
		Bloon rosa = criarRosa();
		pretoZep.addBloonProvavel(rosa);
		return pretoZep;
	}
}
