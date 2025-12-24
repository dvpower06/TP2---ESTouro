package io;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import mundo.*;

/**
 * Classe usada para ler um ficheiro com a informação de um nível
 */
public class RoundReader {

	/**
	 * Lê o ficheiro de nível
	 * 
	 * @param file ficheiro a ler
	 * @return o gestor de nível criado com a informação do ficheiro
	 * @throws IOException quando a leitura corre mal
	 */
	public GestorNivel lerRound(String file) throws IOException {
		GestorNivel level = new GestorNivel();

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			level.setPremioRound(Integer.parseInt(in.readLine())); // lê o valor do round
			String backgroundFile = in.readLine(); // lê o nome da imagem de fundo
			level.setNomeImagemFundo(backgroundFile);
			BufferedImage back = ImageIO.read(new File("data/pistas/" + backgroundFile));
			level.setFundo(back);
			int nCaminhos = Integer.parseInt(in.readLine()); // lê o número de caminhos
			in.readLine(); // lê um parâmetro que está obsoleto, mas que ainda consta dos ficheiros

			// ler a informação do caminho
			for (int p = 0; p < nCaminhos; p++) {
				if (!in.readLine().startsWith("<caminho>"))
					throw new IOException();
				Caminho caminho = new Caminho();
				String linha = in.readLine();
				while (!linha.startsWith("</caminho>")) {
					String xy[] = linha.split("\t");
					int x = Integer.parseInt(xy[0]);
					int y = Integer.parseInt(xy[1]);
					Point pt = new Point(x, y);
					caminho.addSegment(pt);

					linha = in.readLine();
				}
				level.addCaminho(caminho);
			}
			if (!in.readLine().startsWith("<bloons>"))
				throw new IOException();

			// ler a informação da criação de bloons
			String linha = in.readLine();
			while (!linha.startsWith("</bloons>")) {
				String info[] = linha.split("\t");
				int ciclo = Integer.parseInt(info[0]);
				int caminho = Integer.parseInt(info[1]);
				CicloCriacaoInfo cinfo = new CicloCriacaoInfo(ciclo, caminho, info[2]);

				level.addCicloCriação(cinfo);
				linha = in.readLine();
			}
			return level;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
	}
}
