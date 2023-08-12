package telas;

import static java.awt.Color.RED;
import static main.Main.CUSTOMIZED_BLUE;
import static main.Main.fredoka;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;
import jokenpo.Sound;


public class Menu extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage background;
	private String pathBackground = "/background.png";

	private JTextField inputNickname;
	private JButton btnJoin;
	private JButton btnCreateRoom;
	private JButton btnExit;
	private JButton btnMute;
	Sound som = new Sound();

	public Menu()
	{	
		try 
		{
			background = ImageIO.read(getClass().getResource(pathBackground));
			paintComponent(background.getGraphics());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
		setLayout(null);
		
		inputNickname = new JTextField("Player 1");
		inputNickname.setFont(fredoka);
		inputNickname.setBounds(250, 500, 700, 60);
		add(inputNickname);
		
		btnJoin = new JButton("Juntar-se");
		btnJoin.setFont(fredoka);
		btnJoin.setBackground(CUSTOMIZED_BLUE);
		btnJoin.setBounds(500, 600, 200, 40);
		add(btnJoin);
		
		btnCreateRoom = new JButton("Criar Sala");
		btnCreateRoom.setFont(fredoka);
		btnCreateRoom.setBackground(CUSTOMIZED_BLUE);
		btnCreateRoom.setBounds(500, 650, 200, 40);
		add(btnCreateRoom);
		
		btnExit = new JButton("Sair");
		btnExit.setFont(fredoka);
		btnExit.setBackground(CUSTOMIZED_BLUE);
		btnExit.setBounds(500, 700, 200, 40);
		add(btnExit);
	
		som.sound("res/start.wav");
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	        g.drawImage(background, 0, 0, null);
	        
	        g.setColor(CUSTOMIZED_BLUE);
	        g.setFont(fredoka);
	        g.drawString("Informe seu nome", 479, 480);
	}

	public void paintErrorMessage() {
		Graphics g = background.getGraphics();
		
		g.setColor(RED);
        g.setFont(fredoka);
        g.drawString("Erro ao iniciar Servidor", 455, 800);
	}
	
	public String getNickname() {
		return inputNickname.getText();
	}

	public JButton getBtnJoin() {
		return btnJoin;
	}

	public void setBtnJoin(JButton btnJoin) {
		this.btnJoin = btnJoin;
	}

	public JButton getBtnCreateRoom() {
		return btnCreateRoom;
	}

	public void setBtnCreateRoom(JButton btnCreateRoom) {
		this.btnCreateRoom = btnCreateRoom;
	}

	public JButton getBtnExit() {
		return btnExit;
	}

	public void setBtnExit(JButton btnExit) {
		this.btnExit = btnExit;
	}
	
	public JButton getBtnMute() {
		return btnMute;
	}

	public void setBtnMute(JButton btnMute) {
		this.btnMute = btnMute;
	}
}
