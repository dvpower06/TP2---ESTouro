package io;

import java.awt.Point;
import java.io.*;
import java.util.List;

import torre.*;
import mundo.Mundo;

/**
 * Classe responsável pela gravação dos ficheiros de jogo
 */
public class GameWriter {

	/**
	 * grava o jogo no seu estado atual
	 * 
	 * @param nomeFicheiro ficheiro onde guardar o jogo
	 * @param round        nível a que diz respeito o jogo gravado
	 * @param dinheiro     quanto dinheiro se tem
	 * @param vidas        as vidas que existiam
	 * @param m            o mundo tal como estava
	 * @throws IOException quando acontece algum erro de gravação
	 */
	public static void gravarJogo(String nomeFicheiro, int pista, int round, int dinheiro, int vidas, Mundo m)
			throws IOException {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(nomeFicheiro)))) {
			out.println(pista);
			out.println(round);
			out.println(dinheiro);
			out.println(vidas);

			List<Torre> torres = m.getTorres();
			out.println(torres.size());
			// TODO remover estes instanceof
			for (Torre t : torres) {
				Point p = t.getComponente().getPosicaoCentro();
				// escrever a posição e o tipo de torre
				out.print(p.x + "\t" + p.y + "\t");
				if (t instanceof TorreMacaco)
					out.println("macaco");
				else if (t instanceof TorreOctogonal) {
					out.print("octo\t");
					out.println(t.getComponente().getAngulo());
				} else if (t instanceof TorreCanhao)
					out.println("canhao");
				else if (t instanceof TorreMorteiro) {
					out.print("morteiro\t");
					Point ataque = ((TorreMorteiro) t).getAreaAlvo();
					out.println(ataque.x + "\t" + ataque.y);
				} else if (t instanceof TorreNinja)
					out.println("ninja");
				else if (t instanceof TorreBalista) {
					out.print("balista\t");
					out.println(t.getComponente().getAngulo());
				}
			}
		}
	}
}
