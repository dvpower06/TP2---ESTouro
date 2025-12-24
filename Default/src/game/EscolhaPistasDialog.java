package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import prof.jogos2D.util.ImageLoader;

/**
 * Janela que apresenta as pistas disponíveis para que o jogador possa escolher
 * em qual delas quer jogar.
 */
public class EscolhaPistasDialog extends JDialog {

    private int track = -1; // qual a pista escolhida
    private boolean usarLast = false; // ou é para usar o último jogo gravado?

    /**
     * Cria a janela de escolha de pistas
     * 
     * @param owner   janela mãe (o jogo)
     * @param nPistas número de pistas a apresentar
     */
    public EscolhaPistasDialog(Window owner, int nPistas) {
        super(owner, "Escolha de pista");

        // ler a imagem de fundo da janela
        ImageLoader loader = ImageLoader.getLoader();
        ImageIcon thumbnails[] = new ImageIcon[nPistas];
        Image imagemFundo = loader.getImage("data/misc/trackchooser.jpg");

        // criar o painel onde apresentar as pistas
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        JPanel fundo = new JPanel(gbl) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagemFundo, 0, 0, null);
            }
        };
        Dimension dim = new Dimension(imagemFundo.getWidth(null), imagemFundo.getHeight(null));
        fundo.setMinimumSize(dim);
        fundo.setPreferredSize(dim);

        // ler as imagens reduzidas das pistas
        for (int i = 0; i < nPistas; i++) {
            thumbnails[i] = new ImageIcon(loader.getImage("data/pistas/thumb" + (i + 1) + ".jpg"));
            JButton btTrack = new JButton(thumbnails[i]);
            int x = i;
            btTrack.addActionListener(e -> {
                track = x;
                setVisible(false);
            });
            btTrack.setMargin(new Insets(2, 2, 2, 2));
            btTrack.setBorderPainted(false);
            gbc.gridwidth = (i + 1) % 2 == 0 ? GridBagConstraints.REMAINDER : GridBagConstraints.RELATIVE;
            fundo.add(btTrack, gbc);
        }

        JButton bt = new JButton("Carregar último jogo");
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        bt.addActionListener(e -> {
            usarLast = true;
            setVisible(false);
        });
        fundo.add(bt, gbc);

        setContentPane(fundo);
        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * Retorna o nímero da pista escolhida pelo jogador. Se não escolheu pista,
     * escolhe a primeira pista (pista 0)
     * 
     * @return o número da pista escolhida
     */
    public int getPista() {
        return track != -1 ? track : 0;
    }

    /**
     * Indica se é para usar o último nível ou começar numa pista nova
     * 
     * @return true se é para usar o último nível
     */
    public boolean usarSave() {
        return usarLast;
    }
}
