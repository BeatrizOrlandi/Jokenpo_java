package conectividade.server;

import static conectividade.Flag.INICIAR;
import static conectividade.Flag.PLACAR;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import jokenpo.Jogador;
import jokenpo.Jogo;

public class Server extends Thread {

	private ServerSocket serverSocket;
	private int port;
	private byte[] address;
	
	private ArrayList<Socket> clientSockets;
	private ArrayList<RequestHandler> requestHandlers;
	
	private ArrayList<String> nomesJogadores;
	private Jogo jogo;

	
	public Server(int port, byte[] address) {
		this.port = port;
		this.address = address;
		this.setName("Server");
		
		clientSockets = new ArrayList<Socket>();
		requestHandlers = new ArrayList<RequestHandler>();
		nomesJogadores = new ArrayList<String>();
	}
	
	public Server(int port, String hostName) {
		this.port = port;
		try {
			this.address = InetAddress.getByName(hostName).getAddress();
			this.setName("Server");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean recebendoConexoes = true;
		while (recebendoConexoes) {
			try {
				if(serverSocket.isClosed()) {
					break;
				}
				
				System.out.println("Aguardando conex�o");

				Socket cliente = serverSocket.accept();
				clientSockets.add(cliente);
		        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
		        
		        RequestHandler requestHandler = new RequestHandler(cliente, this);
		        requestHandlers.add(requestHandler);
				requestHandler.start();
				
				/**
				 * Limita que apenas dois clientes possam se conectar
				 */
				if(clientSockets.size() == 2)
					recebendoConexoes = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Espera informa��es do Cliente antes de iniciar a partida
		 */
		try {
			Server.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/**
		 * Se existem dois clientes conectados, iniciar partida
		 */
		if(clientSockets.size() == 2) {
			jogo = new Jogo(nomesJogadores.get(0), nomesJogadores.get(1));
			sendToClients(INICIAR + "1");
			sendToClients(PLACAR + getPlacar());
			
		}
	}

	public boolean startServer() {
		try {
			System.out.println("Iniciando Servidor no endere�o " 
						+ InetAddress.getByAddress(address).getHostAddress() + ":" + port + "...");
			serverSocket = new ServerSocket(port);
			
			this.start();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void stopServer() {
		System.out.println("Encerrando Servidor...");
		for (int i = 0; i < clientSockets.size(); i++) {
			try {
				requestHandlers.get(i).interrupt();
				clientSockets.get(i).close();
			} catch (IOException e) {e.printStackTrace();}
		}
		this.interrupt();
		try {
			serverSocket.close();
		} catch (IOException e) {e.printStackTrace();}
	}

	public String getAddress() {
		try {
			return InetAddress.getByAddress(address).getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "0.0.0.0";
	}

	public void sendToClients(String message) {
		for (RequestHandler requestHandler : requestHandlers) {
			requestHandler.sendToClient(message);
		}
	}
	public ArrayList<String> getNomesJogadores() {
		return nomesJogadores;
	}
	
	public Jogo getJogo() {
		return jogo;
	}
	
	public String getPlacar() {
		Jogador j1 = jogo.getJogador1();
		Jogador j2 = jogo.getJogador2();
		return (j1.getNome() + "-" + j1.getVitorias() + "-" + j2.getNome() + "-" + j2.getVitorias() + "-" + jogo.getRoundAtual());
	}
	
	public String getJogadas() {
		Jogador j1 = jogo.getJogador1();
		Jogador j2 = jogo.getJogador2();
		return (j1.getJogadaAtual().getNome() + "-" + j2.getJogadaAtual().getNome());
	}
	
	public void resetarJogadas() {
		Jogador j1 = jogo.getJogador1();
		Jogador j2 = jogo.getJogador2();
		
		j1.setJogadaAtual(null);
		j2.setJogadaAtual(null);
	}
	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}
}