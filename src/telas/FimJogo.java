package telas;

import static main.Main.CUSTOMIZED_BLUE;
import static main.Main.fredoka;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import jokenpo.Sound;
import main.Main;

public class FimJogo extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage background;
	private String pathBackground = "/background.png";

	private JButton btnVoltar;
	private JLabel lblPlacar;
	private JLabel lblVencedor;
	
	private String nomeVencedor;
	private String[] placar;
	
	/**
	 * Som
	 * 
	 */
	Sound som = new Sound();

	public FimJogo(String nomeVencedor)
	{	
		this.nomeVencedor = nomeVencedor;
		
		try 
		{
			background = ImageIO.read(getClass().getResource(pathBackground));
			paintComponent(background.getGraphics());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
		setLayout(null);
		
		btnVoltar = new JButton("Voltar ao Menu Principal");
		btnVoltar.setFont(fredoka);
		btnVoltar.setBackground(CUSTOMIZED_BLUE);
		btnVoltar.setBounds(360, 650, 500, 40);
		add(btnVoltar);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	        g.drawImage(background, 0, 0, null);
	        
            lblVencedor = new JLabel(nomeVencedor + " venceu o jogo!", SwingConstants.CENTER);
            lblVencedor.setFont(fredoka.deriveFont((float) 40));
            lblVencedor.setForeground(CUSTOMIZED_BLUE);
            lblVencedor.setBounds(0, 460, 1200, 40);
            if(nomeVencedor != null && !nomeVencedor.equalsIgnoreCase("empate")){
    	   	 	som.sound("res/win.wav");
				add(lblVencedor);

        	}
        	
        	else {
                g.setFont(fredoka.deriveFont((float) 50));
                lblVencedor.setText("Empate!");
                add(lblVencedor);
        	}
	        
            
	        /**
	         * Placar
	         */
	   	 	
	   	 	if(placar == null) return;
	   	 	
	   	 	String strPlacar = placar[0] + "  " + placar[1] + "  " + "X" + "  " + placar[3] + "  " + placar[2]; 
	   	 	lblPlacar = new JLabel(strPlacar, SwingConstants.CENTER);
	   	 	lblPlacar.setFont(fredoka.deriveFont((float) 30));
	   	 	lblPlacar.setForeground(CUSTOMIZED_BLUE);
	   	 	lblPlacar.setBounds(0, 500, 1200, 40);
	   	 	add(lblPlacar);
	}
	
	public JButton getBtnVoltar() {
		return btnVoltar;
	}
	
	public void setPlacar(String[] placar) {
		this.placar = placar;
		validate();
		repaint();
	}
}

