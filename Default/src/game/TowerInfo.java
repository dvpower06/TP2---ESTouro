package game;

import java.util.Objects;

import javax.swing.Icon;

import torre.Torre;

/**
 * Classe que agrega a informação da torre que é da responsabildiade do
 * jogo, como preços, etc. Esta classe é imutável.
 */
public final class TowerInfo {

	private final String name;
	private final int price;
	private final String description;
	private final Icon icon;
	private final Torre tower;

	public TowerInfo(Torre tower, Icon icon, String nome, int preco, String descricao) {
		this.tower = Objects.requireNonNull(tower);
		this.icon = Objects.requireNonNull(icon);
		this.name = Objects.requireNonNull(nome);
		this.price = preco;
		this.description = Objects.requireNonNull(descricao);
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public Torre getTower() {
		return tower;
	}

	public Icon getIcon() {
		return icon;
	}
}
