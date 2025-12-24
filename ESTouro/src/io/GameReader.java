package io;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import torre.*;

/**
 * Classe usada para ler a informação sobre um jogo gravado
 */
public class GameReader {

	private int pistaIdx; // índice da pista do jogo
	private int round; // número do round (nível) onde se gravou o jogo
	private int vidas; // vidas que tinha o jogador
	private int dinheiro; // dinheiro que havia
	private Torre torres[]; // torres que o jogador possuia

	/**
	 * lê o ficheiro com a informação do jogo
	 * 
	 * @param file nome do ficheiro com o jogo gravado
	 * @throws IOException quando corre algo mal na leitura
	 */
	public void readFile(String file) throws IOException {
		TorreCreator creator = new TorreCreator();

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			// ler as infos do jogo gravado
			pistaIdx = Integer.parseInt(in.readLine());
			round = Integer.parseInt(in.readLine());
			dinheiro = Integer.parseInt(in.readLine());
			vidas = Integer.parseInt(in.readLine());
			// ler quantas torres o jogador tinha
			int nTorres = Integer.parseInt(in.readLine());
			torres = new Torre[nTorres];
			// ler e criar as torres
			for (int i = 0; i < nTorres; i++) {
				String info[] = in.readLine().split("\t");
				int x = Integer.parseInt(info[0]);
				int y = Integer.parseInt(info[1]);
				torres[i] = creator.criarTorrePorNome(info[2]);
				torres[i].setPosicao(new Point(x, y));
				switch (info[2]) {
					case "octo":
					case "balista":
						double angulo = Double.parseDouble(info[3]);
						torres[i].getComponente().setAngulo(angulo);
						break;
					case "morteiro":
						int ax = Integer.parseInt(info[3]);
						int ay = Integer.parseInt(info[4]);
						((TorreMorteiro) torres[i]).setAreaAlvo(new Point(ax, ay));
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
	}

	/** retorna a pista que está guardada */
	public int getPistaIdx() {
		return pistaIdx;
	}

	/** retorna qual o número do round que está guardado */
	public int getRound() {
		return round;
	}

	/** retorna qual o número do vidas que está guardado */
	public int getVidas() {
		return vidas;
	}

	/** retorna qual o dinheiro que está guardado */
	public int getDinheiro() {
		return dinheiro;
	}

	/** retorna as torres que estão guardadas */
	public Torre[] getTorres() {
		return torres;
	}
}
