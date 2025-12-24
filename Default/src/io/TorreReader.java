package io;

import game.TowerInfo;

import java.io.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import torre.*;

/**
 * classe usada para fazer a leitura da configurações das torres que
 * são suportadas no jogo e os respetivos icons que se vão colocar no painel.
 * A ordem em que aparecem no ficheiro é a ordem em que serão apresentadas no
 * painel
 */
public class TorreReader {

	/**
	 * lê o ficheiro de configuração das torres
	 * 
	 * @param file ficheiro com a informação
	 * @return a informação sobre as torres
	 * @throws IOException quando algo corre mal na leitura
	 */
	public TowerInfo[] lerficheiro(File file) throws IOException {
		TowerInfo[] towers;
		TorreCreator tcreator = new TorreCreator();

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			// ler quantas torres existem no ficheiro
			int nTorres = Integer.parseInt(in.readLine());

			// ler a informação de cada torre
			towers = new TowerInfo[nTorres];
			for (int i = 0; i < nTorres; i++) {
				if (!in.readLine().startsWith("<torre>"))
					throw new IOException();
				String codeName = in.readLine();
				Icon icon = new ImageIcon("data/torres/" + codeName + "/icon.gif");
				String name = in.readLine();
				int preco = Integer.parseInt(in.readLine());
				String desc = in.readLine();
				String linha = in.readLine();
				while (!linha.startsWith("</torre>")) {
					desc = desc + "\n" + linha;
					linha = in.readLine();
				}
				Torre t = tcreator.criarTorrePorNome(codeName);
				towers[i] = new TowerInfo(t, icon, name, preco, desc);
			}
			return towers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
	}
}
