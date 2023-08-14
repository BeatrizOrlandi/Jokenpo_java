package main;

import static conectividade.Flag.JOGADA;
import static conectividade.Flag.NICKNAME;
import static conectividade.Flag.STOP;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import conectividade.client.Client;
import conectividade.server.Server;
import static conectividade.Flag.PLACARFINAL;
import static conectividade.Flag.VENCEDORFINAL;
import telas.CreateRoom;
import telas.FimJogo;
import telas.JoinRoom;
import telas.Lobby;
import telas.Menu;
import jokenpo.Sound;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Leitores
	 */
	private static BufferedReader reader;
	
	/**
	 * Tela
	 */
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 900;
	private static Main mainWindow;
	
	/**
	 * Estilizacao
	 */
	public static final Color CUSTOMIZED_BLUE = new Color(119, 179, 214);
	public static final Color BACKGROUND_COLOR = new Color(255, 222, 89);
	public static Font fredoka; 
	
	/**
	 * Panels
	 */
	private JPanel currentPanel = null;
	public Lobby lobby;
	public FimJogo fimJogo;
	
	/**
	 * Conectividade
	 */
	private static boolean hasServer;
	private static boolean hasClient;
	public static Server server;
	public static Client client;
	public static int defaultPort = 12345;
	public static File ipServer = new File("ip.txt");
	
	/**
	 * Som
	 */
	Sound som = new Sound();
	
	
	/*
	 * Nome Jogador
	 */
	public String nomeJogador;
	
	public void initComponents()
	{
		/**
		 * Inicializacao da tela
		 */
		configureFont();
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		getContentPane().setLayout(new FlowLayout(0, 0, 0));
		
		setTitle("Jokenpo!");
		setResizable(false);
		
		/**
		 * A tela se inicia com o menu
		 */
		configureMenu();
		//configureLobby();
		
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
		
		Toolkit toolKit = Toolkit.getDefaultToolkit();
		Image img = toolKit.getImage(getClass().getResource("/cursor.png"));
		Point hotSpot = new Point(0, 0);
		Cursor SQl = toolKit.createCustomCursor(img, hotSpot, "Loading");
		mainWindow.setCursor(SQl);
	}
	
	private void configureFont() { 
		try {
			fredoka = Font.createFont(0, new File("res/FredokaOne-Regular.ttf"));
		} catch (IOException e) {
			e.printStackTrace();
			fredoka =  new Font ("Garamond", Font.BOLD , 11);
		} catch (FontFormatException e) {
			e.printStackTrace();
			fredoka =  new Font ("Garamond", Font.BOLD , 11);
		}
		
		fredoka = fredoka.deriveFont(Font.BOLD, 25);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(fredoka);
	}
	
	/**
	 * Configuracao de Action Listeners
	 */
	
	private void configureMenuActionListeners(Menu menu) {
		
		menu.getBtnJoin().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				som.sound("res/select.wav");
				nomeJogador = menu.getNickname();
				
				configureJoinRoom();
			}
		});
		
		menu.getBtnCreateRoom().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				som.sound("res/select.wav");
				/**
				 * Armazena nome do jogador
				 */
				nomeJogador = menu.getNickname();
				
				/**
				 * Inicia Servidor e Cliente
				 */
				server = getServerByIpTxt(ipServer);
				hasServer = server.startServer();
				
				client = new Client(defaultPort, server.getAddress(), mainWindow);
				hasClient = client.startClient();
				
				/**
				 * Redesenha a tela
				 */
				if(hasServer && hasClient) {
					client.sendToServer(NICKNAME + nomeJogador);
					configureCreateRoom();
				}
				
				/**
				 * Erro ao iniciar server
				 */
				else {
					menu.paintErrorMessage();
					validate();
					repaint();
				}
			}
		});
		
		menu.getBtnExit().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				som.sound("res/select.wav");
				System.exit(0);
			}
		});

	}

	private void configureJoinRoomActionListeners(JoinRoom joinRoom) {
		joinRoom.getBtnJoin().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				som.sound("res/select.wav");
				String ip = joinRoom.getRoomCode().getText();
				byte[] address = convertStringToAddress(ip);
						
				client = new Client(defaultPort, address, mainWindow);
				hasClient = client.startClient();
				
				if(hasClient) {
					client.sendToServer(NICKNAME + nomeJogador);			
				}
				
				/**
				 * Erro ao iniciar client
				 */
				else {
					joinRoom.paintErrorMessage();
					validate();
					repaint();	
				}
			}
		});
		
		joinRoom.getBtnVoltar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				som.sound("res/select.wav");
				configureMenu();
			}
		});
	}
	
	private void configureCreateRoomActionListeners(CreateRoom createRoom) {
		createRoom.getBtnVoltar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				som.sound("res/select.wav");
				client.sendToServer(STOP + "1");
				client.stopClient();

				configureMenu();
			}
		});
	}
	
	private void configureLobbyActionListeners(Lobby lobby) {
		lobby.getBtnConfirm().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				som.sound("res/select.wav");
				String jogada = lobby.getJogada();
				lobby.getBtnConfirm().setEnabled(false);
				lobby.getBtnConfirm().setText(jogada + "!");
				client.sendToServer(JOGADA + jogada + ":" + nomeJogador);
			}
		});

		lobby.getBtnEncerra().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				som.sound("res/select.wav");
				String vencedorFinal = server.getJogo().verificaFim();
				server.sendToClients(VENCEDORFINAL + vencedorFinal);
				server.sendToClients(PLACARFINAL + server.getPlacar());
			}
		});
	}
	
	private void configureFimJogoActionListeners(FimJogo fimJogo) {
		fimJogo.getBtnVoltar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				som.sound("res/select.wav");
				client.sendToServer(STOP + "1");
				client.stopClient();

				if(hasServer){
					server.stopServer();
				}
				configureMenu();
			}
		});
	}
	
	private byte[] convertStringToAddress(String ip) {
		String[] addressString = ip.split("\\.");
		int tam = addressString.length;
		byte[] address = new byte[tam];

		for (int i = 0; i < addressString.length; i++) {
			address[i] = (byte) Integer.parseInt(addressString[i]);
		}
		
		return address;
	}
	
	private Server getServerByIpTxt(File file) {
		if (file.exists()) {
			try {
				reader = new BufferedReader(new FileReader(file.getName()));
				String ip = reader.readLine();
				byte[] address = convertStringToAddress(ip);
				return new Server(defaultPort, address);
			} 
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Iniciando servidor em 'localhost'");
			}
		}
		
		return new Server(defaultPort, "localhost");
	}
	
	/**
	 * Configuracao dos Panels
	 */
	
	public void configureMenu() {
		if(currentPanel != null)
			remove(currentPanel);
		
		Menu menu = new Menu();
		currentPanel = menu;
		configureMenuActionListeners(menu);
		add(menu);
		validate();
		repaint();
	}
	
	public void configureJoinRoom() {
		if(currentPanel != null)
			remove(currentPanel);
		
		JoinRoom joinRoom = new JoinRoom();
		currentPanel = joinRoom;
		configureJoinRoomActionListeners(joinRoom);
		add(joinRoom);
		validate();
		repaint();
	}
	
	public void configureCreateRoom() {
		if(currentPanel != null)
			remove(currentPanel);
		
		CreateRoom createRoom = new CreateRoom();
		currentPanel = createRoom;
		configureCreateRoomActionListeners(createRoom);
		add(createRoom);
		validate();
		repaint();
	}
	
	public void configureLobby() {
		if(currentPanel != null)
			remove(currentPanel);
		
		lobby = new Lobby();
		currentPanel = lobby;
		configureLobbyActionListeners(lobby);
		
		
		add(lobby);
		validate();
		repaint();		
	}
	
	public void configureFimJogo(String nomeVencedor) {
		if(currentPanel != null)
			remove(currentPanel);
		
		fimJogo = new FimJogo(nomeVencedor);
		currentPanel = fimJogo;
		configureFimJogoActionListeners(fimJogo);
		
		add(fimJogo);
		validate();
		repaint();
	}
	
	public static void main(String[] args) {
		mainWindow = new Main();
		mainWindow.initComponents();
	}
}
