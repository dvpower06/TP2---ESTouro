package game;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.LinkedHashMap;
import torre.Torre;

import javax.swing.border.TitledBorder;

/**
 * Classe que define o painel de escolha do modo de ataque
 */
public class ConfiguradorTorres extends JPanel {

	private static final long serialVersionUID = 1L;

	// botões usados para escolher o modo de ataque
	private ButtonGroup btGrp = new ButtonGroup();

	// torre que está a ser controlada por este painel
	private Torre escolhida;

	/** mapeia os modos de ataque ao respetivo botão */
	private LinkedHashMap<Integer, JToggleButton> botoes = new LinkedHashMap<>();

	/**
	 * Cria os vários botões para os vários modos de ataque
	 */
	private void criarBotoesAtaques(JPanel painelAtaques) {
		// TODO acrescentar os novos modos de ataque
		painelAtaques.add(criarBotaoAtaque("Primeiro", Torre.ATACA_PRIMEIRO));
		painelAtaques.add(criarBotaoAtaque("Último", Torre.ATACA_ULTIMO));

		painelAtaques.add(criarBotaoAtaque("Perto", Torre.ATACA_PERTO));
		painelAtaques.add(criarBotaoAtaque("Longe", -1));

		painelAtaques.add(criarBotaoAtaque("Forte", -1));
		painelAtaques.add(criarBotaoAtaque("Juntos", Torre.ATACA_JUNTOS));
	}

	/**
	 * método utilizado para definir qual a torre escolhida, ou seja, qual a que
	 * está a ser modificada
	 * 
	 * @param t a torre a ser modificada
	 */
	public void setSelecionada(Torre t) {
		escolhida = t;

		JToggleButton bt = botoes.get(t.getModoAtaque());
		if (bt != null)
			bt.setSelected(true);
		else
			botoes.firstEntry().getValue().setSelected(true);
	}

	/**
	 * Cria um botão para um modo de ataque
	 * 
	 * @param texto      o texto a colocar no botão
	 * @param modoAtaque o modo de ataque
	 * @return o botão criado
	 */
	private JToggleButton criarBotaoAtaque(String texto, int modoAtaque) {
		JToggleButton button = new JToggleButton(texto);
		button.setPreferredSize(new Dimension(60, 18));
		button.setMargin(new Insets(0, 0, 0, 0));
		button.addActionListener(e -> escolhida.setModoAtaque(modoAtaque));
		btGrp.add(button);
		botoes.put(modoAtaque, button);
		return button;
	}

	/**
	 * cria o configurador
	 */
	public ConfiguradorTorres() {
		super();
		setupInterface();
	}

	/**
	 * Prepara a zona de interface
	 */
	private void setupInterface() {
		JPanel painelAtaques = new JPanel();
		painelAtaques.setBorder(new TitledBorder("Modo de Ataque"));
		painelAtaques.setBounds(new Rectangle(2, 5, 145, 90));
		criarBotoesAtaques(painelAtaques);
		this.setLayout(null);
		this.setBounds(new Rectangle(0, 0, 140, 200));
		this.add(painelAtaques);
	}
}
