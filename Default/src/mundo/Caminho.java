package mundo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Um caminho é uma sequência de pontos. O primeiro ponto na sequência é o ponto
 * de partida e o último é o ponto de chegada. Para andar no caminho basta
 * passar para o ponto seguinte na sequência ou seja, passar do ponto n para o
 * ponto n+1.
 * A posição no caminho é assim dada por um valor inteiro em que 0 representa o
 * início do caminho. Isto permite especificar um caminho sem indicar qual a
 * posição no écran de um elemento.
 */
public class Caminho {

	/**
	 * Lista com todos os pontos do caminho
	 * isto é um bocado força bruta... mas funciona...
	 */
	private List<Point> pontos = new ArrayList<>();
	private List<Point> ancoras = new ArrayList<>();

	public Caminho() {
	}

	/**
	 * pemite adicionar um novo segmento de recta ao caminho. Os pontos intermédios
	 * são adicionados automaticamente ao caminho. Assume que o segmento de recta
	 * começa no último ponto conhecido do caminho
	 * 
	 * @param p o ponto onde termina o segmento de recta. O início do segmento de
	 *          reta assume-se que seja o ponto anterior.
	 */
	public void addSegment(Point p) {
		ancoras.add(p);
		if (pontos.size() == 0) {
			pontos.add(p);
			return;
		}

		// utilizar os algoritmos de computaçã gráfica para determinar os pontos
		// intermédios
		// ver qual o ponto anterior
		Point last = pontos.getLast();

		int dx = p.x - last.x;
		int dy = p.y - last.y;

		if (dx == 0) { // é vertical
			int step = dy > 0 ? 1 : -1;
			for (int y = last.y; y != p.y; y += step) {
				Point novo = new Point(p.x, y);
				pontos.add(novo);
			}
		} else if (dy == 0) { // é horizontal
			int step = dx > 0 ? 1 : -1;
			for (int x = last.x; x != p.x; x += step) {
				Point novo = new Point(x, p.y);
				pontos.add(novo);
			}
		} else if (Math.abs(dx) > Math.abs(dy)) {
			// controla o x
			int step = dx > 0 ? 1 : -1;
			float m = (float) dy / dx * step;
			float x = last.x;
			float y = last.y;
			int nX = Math.abs(dx);
			for (int i = 0; i < nX; i++) {
				y += m;
				x += step;
				Point novo = new Point((int) x, (int) y);
				pontos.add(novo);
			}
		} else {
			// controla o y
			int step = dy > 0 ? 1 : -1;
			float m = (float) dx / dy * step;
			float x = last.x;
			float y = last.y;
			int nY = Math.abs(dy);
			for (int i = 0; i < nY; i++) {
				y += step;
				x += m;
				Point novo = new Point((int) Math.round(x), (int) Math.round(y));
				pontos.add(novo);
			}
		}
	}

	/**
	 * devolve o ponto (coordenada do écran) de início do caminho
	 * 
	 * @return a coordenada (pixeis) onde começa o caminho
	 */
	public Point getInicio() {
		return pontos.getFirst();
	}

	/**
	 * devolve o ponto (coordenada do écran) de término do caminho
	 * 
	 * @return a coordenada (pixeis) onde termina o caminho
	 */
	public Point getFim() {
		return pontos.getLast();
	}

	/**
	 * devolve a cooredenada do ponto i do caminho, ou seja, onde se situa o ponto i
	 * no écran
	 * 
	 * @param i o índice do ponto no caminho
	 * @return a coordenada do iº ponto do caminho, null se o ponto não fizer parte
	 *         do caminho
	 */
	public Point getPoint(int i) {
		return i < pontos.size() ? pontos.get(i) : null;
	}

	/**
	 * devolve qual o próximo ponto no caminho a seguir a outro ponto
	 * 
	 * @param p     o ponto onde se está
	 * @param veloc a velocidade de movimento
	 * @return o ponto onde se vai estar, ou null caso não exista esse ponto
	 */
	public Point getProximo(Point p, int veloc) {
		int idx = pontos.indexOf(p);
		// verificar se está antes do primeiro ou depois do último ponto do caminho
		int proxPonto = idx + veloc;
		if (idx < 0 || proxPonto >= pontos.size())
			return null;
		return pontos.get(proxPonto);
	}

	/**
	 * devolve uma âncora do caminho
	 * 
	 * @param idx indice da âncora a retornar
	 * @return âncora existente no índice idx
	 */
	public Point getAncora(int idx) {
		return ancoras.get(idx);
	}

	/** devolve o número de âncoras */
	public int getNumeroAncoras() {
		return ancoras.size();
	}

	/** devolve a lista de âncoras */
	public List<Point> getAncoras() {
		return Collections.unmodifiableList(ancoras);
	}

	/**
	 * altera uma âncora
	 * 
	 * @param idx o índice da âncora a alterar
	 * @param p   o novo ponto da âncora
	 */
	public void setAnchor(int idx, Point p) {
		ancoras.set(idx, p);
	}

	/** remove a âncora com o índice idx */
	public void removeAnchor(int idx) {
		ancoras.remove(idx);
	}

	/**
	 * insere uma âncora no caminho
	 * 
	 * @param idx índice onde inserir a âncora
	 * @param p   âncora a inserir
	 */
	public void insertAchor(int idx, Point p) {
		ancoras.add(idx, p);
	}
}
