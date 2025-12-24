package game;

import javax.swing.JPanel;
import java.awt.Rectangle;
import javax.swing.JLabel;

import java.awt.Graphics;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JTextArea;

/**
 * Esta classe representa o painel onde aparecem as informações sobre a
 * torre que está a ser selecionada na zona de compras
 */
public class InfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel nomeLBL = null; // label onde aparece o nome da torre
	private JLabel custoLbl = null; // label onde aparece o custo da torre
	private JTextArea descricaoTA = null; // label onde aparece a descrição da torre

	/**
	 * Define qual a informação que aparece no painel de informações
	 * 
	 * @param nome        nome da torre
	 * @param preco       preco da torre
	 * @param desc        descrição da torre
	 * @param temDinheiro indica se o jogador tem dinheiro para comprar a torre
	 */
	public void setInfo(String nome, int preco, String desc, boolean temDinheiro) {
		nomeLBL.setText(nome);
		custoLbl.setText("" + preco);
		custoLbl.setForeground(temDinheiro ? Color.BLACK : Color.red);
		descricaoTA.setText(desc);
	}

	/**
	 * Cria o painel
	 */
	public InfoPanel() {
		super();
		setupInterface();
	}

	/**
	 * Prepara a interface gráfica
	 */
	private void setupInterface() {
		custoLbl = new JLabel();
		custoLbl.setBounds(new Rectangle(53, 31, 86, 23));
		custoLbl.setText("Custo:");
		custoLbl.setFont(new Font("Dialog", Font.BOLD, 14));
		JLabel textoCustoLbl = new JLabel();
		textoCustoLbl.setBounds(new Rectangle(3, 31, 50, 23));
		textoCustoLbl.setFont(new Font("Dialog", Font.BOLD, 14));
		textoCustoLbl.setText("Custo:");
		nomeLBL = new JLabel();
		nomeLBL.setText("JLabel");
		nomeLBL.setFont(new Font("Dialog", Font.BOLD, 14));
		nomeLBL.setHorizontalTextPosition(SwingConstants.CENTER);
		nomeLBL.setHorizontalAlignment(SwingConstants.CENTER);
		nomeLBL.setBounds(new Rectangle(3, 3, 136, 26));
		this.setLayout(null);
		this.setBounds(new Rectangle(0, 0, 140, 200));
		this.setBackground(new Color(241, 241, 241));
		this.add(nomeLBL, null);
		this.add(textoCustoLbl, null);
		this.add(custoLbl, null);
		this.add(getDescricaoTA(), null);
	}

	/**
	 * Cria a área de texto onde aparecem as informações
	 */
	private JTextArea getDescricaoTA() {
		if (descricaoTA == null) {
			descricaoTA = new JTextArea();
			descricaoTA.setEditable(false);
			descricaoTA.setLineWrap(true);
			descricaoTA.setWrapStyleWord(true);
			descricaoTA.setBounds(new Rectangle(0, 56, 135, 143));
			descricaoTA.setBackground(new Color(241, 241, 241));
		}
		return descricaoTA;
	}

	/** Desenha o painel */
	public void paint(Graphics g) {
		super.paint(g);
		g.drawLine(3, 29, getWidth() - 6, 29); // desenha a linha no painel
	}
}
