package game;

import java.awt.*;

import javax.swing.*;

import bloon.Bloon;
import bloon.BloonObserver;
import game.manipulator.ManipuladorCreator;
import game.manipulator.ManipuladorTorre;

import java.io.*;
import java.awt.event.*;

import prof.jogos2D.image.*;
import torre.*;
import prof.jogos2D.util.ImageLoader;
import java.util.List;

import mundo.*;
import io.*;

/**
 * classe que representa o jogo
 */
public class EstouroJogo extends JFrame implements BloonObserver {
	private static final long serialVersionUID = 1L;

	// variáveis para os vários elementos gráficos
	private JPanel zonaJogo = null;
	private JPanel painelTorres = null;
	private JButton comecarBt = null;
	private JLabel dinheiroLbl = null;
	private JLabel vidasLbl = null;
	private JLabel roundLbl = null;
	private InfoPanel infoPanel = new InfoPanel();
	private ConfiguradorTorres towerConfigPanel = null;

	// variáveis para a gestão do mundo
	private Mundo mundo = null;
	private GestorNivel gestorNivel = new GestorNivel();

	// estado atual da aplicação (ver pattern State)
	private EstadoJogo estadoAtual;

	// array com as várias informações sobre as torres
	private TowerInfo towerInfo[];

	// variáveis sobre a informação das opções do jogador
	private int torresInserirIdx; // indice da torre a inserir
	private Torre torreSel = null; // indica qual a torre selecionada/a inserir

	// variáveis sobre o estado do jogo em si
	private ConfigPistas pistasConfig[];
	private int pistaAtual; // pista atual
	private int round; // número do round actual
	private boolean roundOver = true; // se está a decorrer ou não
	private int dinheiro; // dinheiro disponível
	private int vidas; // vidas disponíveis

	// variáveis auxiliares para o jogo
	private Thread atualizadora; // thread que vai actualizar o jogo de x em x milisegundos
	private OuveControlosTorres ouveTorres = new OuveControlosTorres(); // variável que representa os listeners para os
																		// botões de criação das torre

	// variáveis para as mensagens do jogo.
	private ComponenteTemporario msgVitoria;
	private ComponenteTemporario msgGameOver;
	private ComponenteTemporario msgRoundWin;
	private ComponenteTemporario msgTrackComplete;

	/**
	 * Inicializa o jogo
	 */
	public void play() {
		estadoAtual = new EstadoSelecionarTorre();
		mundo = new Mundo();

		lerConfiguracoesIniciais(); // ler a info sobre as condições iniciais e as pistas
		EscolhaPistasDialog seletorPista = new EscolhaPistasDialog(this, pistasConfig.length);
		seletorPista.setModal(true);
		seletorPista.setVisible(true);
		if (seletorPista.usarSave()) {
			try {
				lerUltimoJogo();
				repaint();
				return;
			} catch (IOException e1) {
				System.err.println("Erro na leitura do ficheiro de nível");
			}
		}
		pistaAtual = seletorPista.getPista();
		primeiroRound(); // Definir o primeiro nível

		// no fim está tudo pronto para mandar desenhar o mundo
		repaint();
	}

	/**
	 * Lê o último jogo gravado
	 * 
	 * @throws IOException se algo correr mal na leitura
	 */
	private void lerUltimoJogo() throws IOException {
		GameReader gr = new GameReader();
		gr.readFile("data/saves/lastsave.txt");
		pistaAtual = gr.getPistaIdx();
		setRound(gr.getRound());
		setVidas(gr.getVidas());
		setDinheiro(gr.getDinheiro());
		for (Torre t : gr.getTorres())
			mundo.addTower(t);
	}

	/**
	 * inicializa o jogo num dado ficheiro de nível, útil para testar esse nível
	 * (ver exemplo no main ao fundo deste ficheiro)
	 * 
	 * @param nomeNivel ficheiro com a descrição do nível
	 * @param dinheiro  dinheiro a dispor neste nível
	 * @param vidas     número de vidas admitidas no nível
	 */
	public void play(String nomeNivel, int dinheiro, int vidas) {
		mundo = new Mundo();
		lerRound(nomeNivel);
		setDinheiro(dinheiro);
		setVidas(vidas);

		pistasConfig = new ConfigPistas[1];
		pistasConfig[0] = new ConfigPistas();
		pistasConfig[0].firstRound = 1;
		pistasConfig[0].lastRound = 1;
		pistaAtual = 0;
		round = 1;

		estadoAtual = new EstadoSelecionarTorre();
		// no fim está tudo pronto para mandar desenhar o mundo
		repaint();
	}

	/** coloca o jogo no primeiro round */
	private void primeiroRound() {
		setRound(pistasConfig[pistaAtual].firstRound);
	}

	/** indica se se está no último round da pista atual */
	private boolean estaUltimoRoundPista() {
		return round >= pistasConfig[pistaAtual].lastRound;
	}

	/** indica se ainda há mais rounds para jogar */
	private boolean temProximoRound() {
		return !estaUltimoRoundPista() || pistaAtual < pistasConfig.length - 1;
	}

	/** passa para o próximo round */
	private void proximoRound() {
		if (!temProximoRound()) {
			return;
		}
		if (estaUltimoRoundPista()) {
			pistaAtual++;
			mundo = new Mundo();
			setRound(pistasConfig[pistaAtual].firstRound);
		} else {
			setRound(round + 1);
		}
	}

	/**
	 * lê o ficheiro com a informação do dinheiro inicial, vidas e das pistas que
	 * existem no jogo
	 */
	private void lerConfiguracoesIniciais() {
		String fileName = "data/niveis/tracks.txt";
		try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
			// definir as quantidades iniciais
			setDinheiro(Integer.parseInt(in.readLine()));
			setVidas(Integer.parseInt(in.readLine()));
			// ler info sobre as pistas
			int nTracks = Integer.parseInt(in.readLine());
			pistasConfig = new ConfigPistas[nTracks];
			for (int i = 0; i < nTracks; i++) {
				pistasConfig[i] = new ConfigPistas();
				pistasConfig[i].firstRound = 1;
				pistasConfig[i].lastRound = Integer.parseInt(in.readLine());
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro na leitura do ficheiro " + fileName, "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Desenha a zona de jogo
	 * 
	 * @param g onde desenhar
	 */
	private synchronized void drawGameArea(Graphics2D g) {
		if (mundo != null)
			mundo.draw(g);
		estadoAtual.desenhar(g);
	}

	/**
	 * ler a pista do jogo do ficheiro indicado
	 * 
	 * @param file ficheiro com a informação do nível
	 */
	private void lerRound(String file) {
		GestorNivel gestor; // gestor temporário

		RoundReader reader = new RoundReader();
		try {
			gestor = reader.lerRound(file);
			mundo.setFundo(new ComponenteSimples(gestor.getFundo()));
			gestorNivel = gestor; // se chegou aqui está tudo bem, assumir o nível
		} catch (IOException e) {
			System.out.println("erro na leitura do ficheiro " + file);
			e.printStackTrace();
		}
	}

	/** método chamado quando se começa um round */
	private void comecarRound() {
		salvarProgresso();
		roundOver = false;
		comecarBt.setEnabled(false);
		gestorNivel.start();

		msgVitoria.reset();
		msgGameOver.reset();
		msgRoundWin.reset();

		atualizadora = new Actualizador();
		atualizadora.start();
	}

	/** Gravar o estado atual do jogo para poder retomar mais tarde */
	private void salvarProgresso() {
		try {
			GameWriter.gravarJogo("data/saves/lastsave.txt", pistaAtual, round, dinheiro, vidas, mundo);
		} catch (IOException e) {
			System.err.println("Erro na escrita do ficheiro de jogo");
			e.printStackTrace();
		}
	}

	/** método chamado quando se termina um round */
	private void fimRound() {
		mundo.limparMoveis();
		if (vidas > 0 && temProximoRound()) {
			setDinheiro(dinheiro + gestorNivel.getPremioRound());
			proximoRound();
		} else {
			play();
		}
		comecarBt.setEnabled(true);
	}

	/**
	 * define qual o round que se vai jogar
	 * 
	 * @param r número do round a jogar
	 */
	private void setRound(int r) {
		round = r;
		roundLbl.setText("" + round);
		torreSel = null;

		// ler a pista
		lerRound("data/niveis/nivel_" + (pistaAtual + 1) + "_" + round + ".txt");
		zonaJogo.repaint();
	}

	/**
	 * Método chamado sempre que um bloon rebenta
	 * 
	 * @param b bloon que rebenta
	 */
	public void bloonEstourou(Bloon b) {
		setDinheiro(dinheiro + b.getValor());
	}

	/**
	 * Método chamado sempre que um bloon sai do mundo
	 * 
	 * @param b bloon que escapa
	 */
	public void bloonEscapou(Bloon b) {
		setVidas(vidas - b.getValor());
	}

	/**
	 * Classe responsável pela criação da thread que vai actualizar o mundo de x em
	 * x segundos
	 */
	class Actualizador extends Thread {
		public void run() {
			boolean finished = false;
			while (!roundOver) {
				// vai atualizar todos os elementos do jogo
				if (!finished) {
					mundo.atualizar();

					List<Bloon> criados = gestorNivel.criarBloons();
					for (Bloon b : criados) {
						b.addBloonObserver(EstouroJogo.this);
						mundo.addBloon(b);
					}

					// verificar se o nível acabou por falta de vidas
					if (vidas <= 0) {
						mundo.addFx(msgGameOver);
						finished = true;
					}
					// verificar se o round acabou com vitória
					else if (mundo.getNumeroBloons() == 0 && !gestorNivel.aindaTemBloons()) {
						finished = true;
						if (!temProximoRound())
							mundo.addFx(msgVitoria);
						else if (estaUltimoRoundPista())
							mundo.addFx(msgTrackComplete);
						else
							mundo.addFx(msgRoundWin);
					}
				}
				if (finished && mundo.isOver()) {
					roundOver = true;
				}
				// mandar atualizar o desenho das coisas
				zonaJogo.repaint();

				// esperar 50 milisegundos o que dá umas 20 frames por segundo
				try {
					sleep(50);
				} catch (InterruptedException e) {
				}
			}
			fimRound();
		}
	};

	/**
	 * define e actualiza o dinheiro do jogo
	 * 
	 * @param dinheiro nova quantidade de dinheiro
	 */
	private void setDinheiro(int dinheiro) {
		this.dinheiro = dinheiro;
		dinheiroLbl.setText("" + dinheiro);
	}

	/**
	 * define e actualiza as vidas do jogo
	 * 
	 * @param vidas novo número de vidas
	 */
	private void setVidas(int vidas) {
		this.vidas = vidas;
		if (vidas <= 0) {
			vidas = 0;
			vidasLbl.setText("" + vidas);
			// roundOver = true;
		} else
			vidasLbl.setText("" + vidas);
	}

	/** Lê a informação e prepara as torres que o jogo suporta. */
	private void setupTorres() {
		TorreReader treader = new TorreReader();

		try {
			towerInfo = treader.lerficheiro(new File("data/torres/TowerInfo.txt"));
			for (int i = 0; i < towerInfo.length; i++) {
				criaTorre(i, towerInfo[i]);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "erro na leitura do ficheiro data/torres/TowerInfo.txt", "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/** prepara as mensagens do final do nível */
	private void setupMensagens() {
		ComponenteSimples vit = new ComponenteSimples(ImageLoader.getLoader().getImage("data/misc/vitoria.png"));
		msgVitoria = new ComponenteTemporario(vit, 60);
		int px = (getWidth() - msgVitoria.getComprimento()) / 2;
		int py = (getHeight() - msgVitoria.getAltura()) / 2;
		msgVitoria.setPosicao(new Point(px, py));

		ComponenteSimples go = new ComponenteSimples(ImageLoader.getLoader().getImage("data/misc/gameover.png"));
		msgGameOver = new ComponenteTemporario(go, 60);
		px = (getZonaJogo().getWidth() - msgGameOver.getComprimento()) / 2;
		py = (getZonaJogo().getHeight() - msgGameOver.getAltura()) / 2;
		msgGameOver.setPosicao(new Point(px, py));

		ComponenteSimples rw = new ComponenteSimples(ImageLoader.getLoader().getImage("data/misc/roundwin.png"));
		msgRoundWin = new ComponenteTemporario(rw, 60);
		px = (getZonaJogo().getWidth() - msgRoundWin.getComprimento()) / 2;
		py = (getZonaJogo().getHeight() - msgRoundWin.getAltura()) / 2;
		msgRoundWin.setPosicao(new Point(px, py));

		ComponenteSimples tc = new ComponenteSimples(ImageLoader.getLoader().getImage("data/misc/trackcomplete.png"));
		msgTrackComplete = new ComponenteTemporario(tc, 60);
		px = (getZonaJogo().getWidth() - msgTrackComplete.getComprimento()) / 2;
		py = (getZonaJogo().getHeight() - msgTrackComplete.getAltura()) / 2;
		msgTrackComplete.setPosicao(new Point(px, py));
	}

	/**
	 * Prepara um botão de criação de uma torre
	 * 
	 * @param idx índice da torre na lista de torres
	 * @param ti  informação da torre a ser criada
	 */
	private void criaTorre(int idx, TowerInfo ti) {
		JCheckBox novoControlo = new JCheckBox();
		novoControlo.setBackground(Color.white);
		novoControlo.setHorizontalAlignment(SwingConstants.CENTER);
		novoControlo.setBounds(new Rectangle((idx % 4) * 35, (idx / 4) * 35 + 1, 35, 35));
		novoControlo.setIcon(ti.getIcon());
		novoControlo.addMouseListener(ouveTorres);
		novoControlo.addActionListener(ouveTorres);
		novoControlo.setActionCommand("" + idx);
		painelTorres.add(novoControlo);
	}

	/**
	 * Constroi o jogo
	 */
	public EstouroJogo() {
		super();
		setupInterface();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	/** Prepara a interface gráfica */
	private void setupInterface() {
		infoPanel.setBounds(new Rectangle(7, 6, 140, 200));
		infoPanel.setVisible(false);
		this.setSize(637, 513);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setTitle("ESTouro");
		this.pack();

		setupTorres();
		setupMensagens();
	}

	/**
	 * Classe que vai ouvir os botões de criação de torres
	 * A cada botão vai ficar associado um índice correspondente ao local da
	 * TorreInfo em que se encontra a informação das torres
	 */
	class OuveControlosTorres extends MouseAdapter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JCheckBox cb = (JCheckBox) e.getSource();
			// ver qual o índice associado ao botão
			torresInserirIdx = Integer.parseInt(cb.getActionCommand());
			// ver se há dinheiro para comprar a torre
			if (towerInfo[torresInserirIdx].getPrice() > dinheiro)
				return;
			torreSel = towerInfo[torresInserirIdx].getTower();
			estadoAtual = new EstadoInserirTorre();
		}

		public void mouseEntered(MouseEvent e) {
			JCheckBox cb = (JCheckBox) e.getSource();
			int torreIdx = Integer.parseInt(cb.getActionCommand());
			TowerInfo ti = towerInfo[torreIdx];
			infoPanel.setInfo(ti.getName(), ti.getPrice(), ti.getDescription(), ti.getPrice() < dinheiro);
			infoPanel.setVisible(true);
		}

		public void mouseExited(MouseEvent e) {
			infoPanel.setVisible(false);
		}
	}

	/**
	 * classe para processar os eventos do rato que ocorrem na zona de jogo
	 */
	private class OuveZonaJogo extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			estadoAtual.mousePressed(e.getPoint());
		}

		public void mouseReleased(MouseEvent e) {
			estadoAtual.mouseReleased(e.getPoint());
		}

		public void mouseDragged(MouseEvent e) {
			estadoAtual.mouseDragged(e.getPoint());
		}

		public void mouseMoved(MouseEvent e) {
			estadoAtual.mouseMoved(e.getPoint());
		}
	}

	/**
	 * classe que representa a superclasse dos estados do jogo
	 */
	private class EstadoJogo {
		public void desenhar(Graphics2D g) {
		}

		public void mouseDragged(Point p) {
		}

		public void mousePressed(Point p) {
		}

		public void mouseReleased(Point p) {
		}

		public void mouseMoved(Point p) {
		}
	}

	/** classe que representa o estado de escolher torre */
	private class EstadoSelecionarTorre extends EstadoJogo {
		@Override
		public void desenhar(Graphics2D g) {
			if (torreSel != null)
				torreSel.desenhaRaioAcao(g);
		}

		@Override
		public void mousePressed(Point p) {
			torreSel = mundo.getTowerAt(p);
			if (torreSel != null) {
				ManipuladorTorre man = ManipuladorCreator.criarManipulador(torreSel);
				estadoAtual = new EstadoManipularTorre(man);
				towerConfigPanel.setSelecionada(torreSel);
				towerConfigPanel.setVisible(true);
			} else
				towerConfigPanel.setVisible(false);
			zonaJogo.repaint();
		}

		@Override
		public void mouseMoved(Point p) {
		}
	}

	/** classe que representa o estado de inserir torre */
	private class EstadoInserirTorre extends EstadoJogo {

		@Override
		public void desenhar(Graphics2D g) {
			torreSel.desenhaRaioAcao(g);
			g.drawImage(torreSel.getComponente().getSprite(), torreSel.getComponente().getPosicao().x,
					torreSel.getComponente().getPosicao().y, null);
		}

		@Override
		public void mousePressed(Point p) {
			torreSel = towerInfo[torresInserirIdx].getTower();
			Torre t = torreSel.clone();
			setDinheiro(dinheiro - towerInfo[torresInserirIdx].getPrice());
			t.setPosicao(p);
			mundo.addTower(t);
			estadoAtual = new EstadoSelecionarTorre();
		}

		@Override
		public void mouseMoved(Point p) {
			torreSel.setPosicao(p);
			zonaJogo.repaint();
		}
	}

	/** Representa o estado de usar um dos manipuladores */
	private class EstadoManipularTorre extends EstadoJogo {

		ManipuladorTorre manipulator;

		public EstadoManipularTorre(ManipuladorTorre manipulator) {
			this.manipulator = manipulator;
		}

		@Override
		public void desenhar(Graphics2D g) {
			manipulator.desenhar(g);
		}

		@Override
		public void mouseDragged(Point p) {
			manipulator.mouseDragged(p);
			zonaJogo.repaint();
		}

		@Override
		public void mouseReleased(Point p) {
			manipulator.mouseReleased(p);
			zonaJogo.repaint();
			estadoAtual = new EstadoSelecionarTorre();
		}
	}

	/** classe auxiliar que guarda a informação sobre as pistas */
	class ConfigPistas {
		int firstRound;
		int lastRound;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		JPanel jContentPane = null;
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setPreferredSize(new Dimension(633, 479));
			jContentPane.add(getZonaJogo(), BorderLayout.CENTER);
			jContentPane.add(getZonaLateral(), BorderLayout.EAST);
		}
		return jContentPane;
	}

	/**
	 * Este metodo inicializa a zonaJogo e também tem o código do desenho do mundo
	 */
	private JPanel getZonaJogo() {
		if (zonaJogo == null) {
			zonaJogo = new JPanel() {
				public void paint(Graphics g) {
					drawGameArea((Graphics2D) g);
				}
			};
			zonaJogo.setLayout(new GridBagLayout());
			zonaJogo.setPreferredSize(new Dimension(483, 479));
			zonaJogo.setSize(483, 479);
			zonaJogo.setBackground(Color.pink);
			OuveZonaJogo ouve = new OuveZonaJogo();
			zonaJogo.addMouseListener(ouve);
			zonaJogo.addMouseMotionListener(ouve);
		}
		return zonaJogo;
	}

	/**
	 * preparar a zona lateral (zona das torres e configurador)
	 */
	private JPanel getZonaLateral() {
		JPanel zonaLateral = new JPanel();
		zonaLateral.setLayout(null);
		zonaLateral.setPreferredSize(new Dimension(150, 479));
		zonaLateral.setBackground(new Color(106, 102, 203));
		zonaLateral.add(getZonaStatus(), null);
		zonaLateral.add(getZonaTorres(), null);
		zonaLateral.add(getZonaUpgrades(), null);
		zonaLateral.add(getComecarBT(), null);
		// zonaLateral.add(getLoadBT(), null);
		// zonaLateral.add(getSaveBT(), null);

		return zonaLateral;
	}

	/**
	 * Preparar a zona de status
	 */
	private JPanel getZonaStatus() {
		JPanel zonaStatus = null;
		if (zonaStatus == null) {
			roundLbl = new JLabel();
			roundLbl.setBounds(new Rectangle(80, 57, 67, 19));
			roundLbl.setForeground(new Color(255, 112, 111));
			roundLbl.setHorizontalAlignment(SwingConstants.TRAILING);
			roundLbl.setText("\u20ac\u20ac");
			roundLbl.setFont(new Font("Arial", Font.BOLD, 16));
			JLabel jLabel11 = new JLabel();
			jLabel11.setBounds(new Rectangle(5, 57, 71, 19));
			jLabel11.setForeground(new Color(255, 112, 111));
			jLabel11.setText("Round");
			jLabel11.setFont(new Font("Arial", Font.BOLD, 16));
			vidasLbl = new JLabel();
			vidasLbl.setBounds(new Rectangle(80, 36, 67, 19));
			vidasLbl.setForeground(new Color(255, 112, 111));
			vidasLbl.setHorizontalAlignment(SwingConstants.TRAILING);
			vidasLbl.setText("��");
			vidasLbl.setFont(new Font("Arial", Font.BOLD, 16));
			JLabel jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(5, 36, 71, 19));
			jLabel1.setForeground(new Color(255, 112, 111));
			jLabel1.setText("Vidas");
			jLabel1.setFont(new Font("Arial", Font.BOLD, 16));
			dinheiroLbl = new JLabel();
			dinheiroLbl.setBounds(new Rectangle(81, 6, 65, 26));
			dinheiroLbl.setForeground(new Color(255, 112, 111));
			dinheiroLbl.setText("��");
			dinheiroLbl.setHorizontalAlignment(SwingConstants.TRAILING);
			dinheiroLbl.setFont(new Font("Arial", Font.BOLD, 16));
			JLabel jLabel = new JLabel();
			jLabel.setText("Dinheiro");
			jLabel.setFont(new Font("Arial", Font.BOLD, 16));
			jLabel.setForeground(new Color(255, 112, 111));
			jLabel.setBounds(new Rectangle(4, 5, 76, 26));
			zonaStatus = new JPanel();
			zonaStatus.setLayout(null);
			zonaStatus.setPreferredSize(new Dimension(150, 80));
			zonaStatus.setLocation(new Point(0, 1));
			zonaStatus.setSize(new Dimension(150, 80));
			zonaStatus.setBackground(new Color(255, 203, 203));
			zonaStatus.add(jLabel, null);
			zonaStatus.add(dinheiroLbl, null);
			zonaStatus.add(jLabel1, null);
			zonaStatus.add(vidasLbl, null);
			zonaStatus.add(jLabel11, null);
			zonaStatus.add(roundLbl, null);
		}
		return zonaStatus;
	}

	/**
	 * prepara a zona das torres
	 */
	private JPanel getZonaTorres() {
		if (painelTorres == null) {
			painelTorres = new JPanel();
			painelTorres.setLayout(null);
			painelTorres.setBackground(Color.white);
			painelTorres.setSize(new Dimension(150, 120));
			painelTorres.setLocation(new Point(0, 80));
		}
		return painelTorres;
	}

	/**
	 * prepara a zona de upgrades
	 */
	private JPanel getZonaUpgrades() {
		JPanel zonaUpgrades = new JPanel();
		zonaUpgrades.setLayout(null);
		zonaUpgrades.setBackground(new Color(238, 249, 101));
		zonaUpgrades.setBounds(new Rectangle(0, 200, 150, 220));
		zonaUpgrades.add(infoPanel, null);
		zonaUpgrades.add(getPainelConfigTorre(), null);

		return zonaUpgrades;
	}

	/** prepara a zona de configuração das torres */
	private ConfiguradorTorres getPainelConfigTorre() {
		if (towerConfigPanel == null) {
			towerConfigPanel = new ConfiguradorTorres();
			towerConfigPanel.setBounds(new Rectangle(0, 5, 150, 220));
			towerConfigPanel.setVisible(false);
		}
		return towerConfigPanel;
	}

	/** cria o botão de começar */
	private JButton getComecarBT() {
		if (comecarBt == null) {
			comecarBt = new JButton();
			// comecarBT.setBounds(new Rectangle(2, 423, 146, 59));
			comecarBt.setBounds(new Rectangle(2, 422, 146, 60));
			comecarBt.setText("Começar");
			comecarBt.addActionListener(e -> comecarRound());
		}
		return comecarBt;
	}

	/**
	 * Arranca com o jogo
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EstouroJogo estouro = new EstouroJogo();
		estouro.setVisible(true);
		estouro.play();
		// Para testar um nível em particular, comentar a linha anterior e usar
		// estouro.play("data/niveis/nivel_4_2.txt", 10000, 200);
		// ou para usar um dos níveis de testar balões (ou torres)
		// estouro.play("data/niveis_teste/teste_rosa.txt", 10000, 200);
	}
}
