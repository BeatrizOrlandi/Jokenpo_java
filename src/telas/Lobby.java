package telas;

import static main.Main.BACKGROUND_COLOR;
import static main.Main.CUSTOMIZED_BLUE;
import static main.Main.fredoka;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import main.Main;

public class Lobby extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage background;
	private String pathBackground = "/background-lobby.png";
	
	private ImageIcon tesoura= new ImageIcon("res/tesoura.png");
	private ImageIcon papel= new ImageIcon("res/papel.png");
	private ImageIcon pedra= new ImageIcon("res/pedra.png");
	private ImageIcon selectedTesoura= new ImageIcon("res/select_tesoura.png");
	private ImageIcon selectedPapel= new ImageIcon("res/select_papel.png");
	private ImageIcon selectedPedra= new ImageIcon("res/select_pedra.png");
	
	private JLabel lblLupa;
	private JLabel lblTesoura;
	private JLabel lblPapel;
	private JLabel lblPedra;
	private JLabel lblPlacar;

	private JButton btnConfirm;
	private ButtonGroup radioGroup;
	
	private String jogadaArdversario = null;
	private boolean adversarioJogou = false;
	private boolean fimRound = false;
	private String nomeVencedor = null;
	
	private String[] placar;
	
	public Lobby()
	{	
		try 
		{
			background = ImageIO.read(getClass().getResource(pathBackground));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
		setLayout(null);
				
		/**
		 * RadioButtons 
		 */
		radioGroup = new ButtonGroup();
		
		JRadioButton btnTesoura = new JRadioButton(tesoura);
		btnTesoura.setName("TESOURA");
		btnTesoura.setSelected(true);
		btnTesoura.setBackground(BACKGROUND_COLOR);
		btnTesoura.setBounds(80, 350, 166, 167);
		btnTesoura.setSelectedIcon(selectedTesoura);
		btnTesoura.setDisabledIcon(tesoura);
		radioGroup.add(btnTesoura);
		add(btnTesoura);
		
		JRadioButton btnPapel = new JRadioButton(papel);
		btnPapel.setName("PAPEL");
		btnPapel.setBackground(BACKGROUND_COLOR);
		btnPapel.setBounds(80, 530, 166, 167);
		btnPapel.setSelectedIcon(selectedPapel);
		btnPapel.setDisabledIcon(papel);
		radioGroup.add(btnPapel);
		add(btnPapel);
		
		JRadioButton btnPedra = new JRadioButton(pedra);
		btnPedra.setName("PEDRA");
		btnPedra.setBackground(BACKGROUND_COLOR);
		btnPedra.setBounds(80, 700, 166, 167);
		btnPedra.setSelectedIcon(selectedPedra);
		btnPedra.setDisabledIcon(pedra);
		radioGroup.add(btnPedra);
		add(btnPedra);
		
		lblLupa = new JLabel(new ImageIcon(getClass().getResource("/magnifier.gif")));
		lblLupa.setBounds(860, 400, 200, 200);
		add(lblLupa);
		
		btnConfirm = new JButton("Confirmar");
		btnConfirm.setFont(fredoka);
		btnConfirm.setBackground(CUSTOMIZED_BLUE);
		btnConfirm.setBounds(500, 700, 200, 40);
		add(btnConfirm);	
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
		/**
         * Esconde a jogada do outro jogador
         */
		if(jogadaArdversario != null &&jogadaArdversario.equalsIgnoreCase("TESOURA")) {
			lblTesoura = new JLabel(new ImageIcon(getClass().getResource("/select_tesoura.png")));
			lblTesoura.setBounds(860, 400, 165, 518);
			add(lblTesoura);
		}  
		else if(jogadaArdversario != null &&jogadaArdversario.equalsIgnoreCase("PAPEL")) {
			lblPapel = new JLabel(new ImageIcon(getClass().getResource("/select_papel.png")));
			lblPapel.setBounds(860, 400, 165, 518);
			add(lblPapel);
		}
		
        else if(jogadaArdversario != null && jogadaArdversario.equalsIgnoreCase("PEDRA")) {
        	lblPedra = new JLabel(new ImageIcon(getClass().getResource("/select_pedra.png")));
			lblPedra.setBounds(860, 400, 165, 518);
			add(lblPedra);
        }
        
        
        g.setColor(CUSTOMIZED_BLUE);
        
        /**
         * Placar
         */
   	 	g.setFont(fredoka.deriveFont((float) 45));
   	 	g.drawString("Round " + placar[4], 510, 400);
   	 	
   	 	String strPlacar = placar[0] + "  " + placar[1] + "  " + "X" + "  " + placar[3] + "  " + placar[2]; 
   	 	if(lblPlacar != null)
   	 		remove(lblPlacar);
   	 	lblPlacar = new JLabel(strPlacar, SwingConstants.CENTER);
   	 	lblPlacar.setFont(fredoka.deriveFont((float) 28));
   	 	lblPlacar.setForeground(CUSTOMIZED_BLUE);
   	 	lblPlacar.setBounds(350, 220, 500, 40);
   	 	add(lblPlacar);
        
   	 	/**
   	 	 * Informações que somente aparecem enquanto o round est� sendo jogado
   	 	 */
        if(!fimRound) {
            g.setFont(fredoka.deriveFont((float) 16));
             
        	g.drawString("Selecione sua jogada clicando", 470, 650);
        	//g.drawString("sobre a seta correspondente!", 470, 665);   
        	
       	 	g.setFont(fredoka.deriveFont((float) 20));
       	 	
        	if(!adversarioJogou) {
				g.drawString("Aguardando jogada", 870, 620);
				g.drawString("adversária", 920, 640);
           } else {
           		g.drawString("Jogada Confirmada!", 870, 520);
           }
        }
        /**
         * Fim do round
         */
        else {
			
        	if(nomeVencedor != null && !nomeVencedor.equalsIgnoreCase("null")){
                g.setFont(fredoka.deriveFont((float) 40));
                g.drawString(nomeVencedor, 430, 550);
            	g.drawString("venceu o round!", 450, 600);

        	} else {
        		g.setFont(fredoka.deriveFont((float) 50));
            	g.drawString("Empate!", 500, 500);
        	}
			
        }
	}

	public JButton getBtnConfirm() {
		return btnConfirm;
	}

	public void setJogadas(String[] jogadas) {
		this.jogadaArdversario = (getJogada().equals(jogadas[0])) ? (jogadas[1]) : (jogadas[0]);
		validate();
		repaint();
	}
	
	public void setAdversarioJogou(boolean adversarioJogou) {
		if(this.adversarioJogou)
			return;
		
		this.adversarioJogou = adversarioJogou;
		if(adversarioJogou)
			lblLupa.setVisible(false);
		validate();
		repaint();
	}
	
	public void setPlacar(String[] placar) {
		this.placar = placar;
		validate();
		repaint();
	}
	
	public void setNomeVencedorRound(String nomeVencedor) {
		fimRound = true;
		btnConfirm.setVisible(false);
		this.nomeVencedor = nomeVencedor;
		validate();
		repaint();
	}
	
	public void nextRound() {
		/**
		 * Refaz todas as configura��es iniciais
		 */
		jogadaArdversario = null;
		adversarioJogou = false;
		fimRound = false;
		nomeVencedor = null;
		
		if(lblTesoura != null)
        		lblTesoura.setVisible(false);
		if(lblPapel != null)
				lblPapel.setVisible(false);
		if(lblPedra != null)
				lblPedra.setVisible(false);
		lblLupa.setVisible(true);

		
		btnConfirm.setText("Confirmar");
		btnConfirm.setEnabled(true);
		btnConfirm.setVisible(true);
		
		for (Enumeration<AbstractButton> buttons = radioGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            button.setEnabled(true);
        }
		
		validate();
		repaint();
	}
	
	public String getJogada() {
		String buttonName = null;
		
		for (Enumeration<AbstractButton> buttons = radioGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            button.setEnabled(false);

            if (button.isSelected())
                buttonName = button.getName();
        }
        return buttonName;
	}		
}

