package conectividade.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import main.Main;

public class Client {

	private Socket clientSocket;
	private int port;
	private byte[] address;
	
	/**
	 * Respons�vel por receber e enviar mensagens entre o Servidor e o Cliente
	 */
	private RequestHandler requestHandler;
	
	private Main mainWindow;

	public Client(int port, byte[] address, Main main) {
		this.port = port;
		this.address = address;
		this.mainWindow = main;
	}
	
	public Client(int port, String hostName, Main main) {
		this.port = port;
		try {
			this.address = InetAddress.getByName(hostName).getAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.mainWindow = main;
	}

	public boolean startClient() {
		try {
			clientSocket = new Socket(InetAddress.getByAddress(address), port);
			requestHandler = new RequestHandler(clientSocket, this);
			requestHandler.start();
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean stopClient() {
		try {
			requestHandler.interrupt();
			clientSocket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void sendToServer(String message) {
		requestHandler.sendToServer(message);
	}

	public String sendToServer(String message, boolean getResponse) {
		return requestHandler.sendToServer(message, getResponse);
	}
	
	public void startLobby() {
		mainWindow.configureLobby();
	}
	
	public void setAdversarioJogou(String nomeJogador) {
		mainWindow.lobby.setAdversarioJogou(!nomeJogador.equals(mainWindow.nomeJogador));
	}
	
	public void setLobbyPlacar(String[] placar){
		mainWindow.lobby.setPlacar(placar);
	}
	
	public void setLobbyJogadas(String[] jogadas){
		mainWindow.lobby.setJogadas(jogadas);
	}
	
	public void setLobbyVencedorRound(String nomeVencedor){
		mainWindow.lobby.setNomeVencedorRound(nomeVencedor);
	}
	
	public void lobbyNextRound() {
		mainWindow.lobby.nextRound();
	}
	
	public void showFimJogo(String nomeVencedor) {
		mainWindow.configureFimJogo(nomeVencedor);
	}
	
	public void setFimJogoPlacar(String[] placar){
		mainWindow.fimJogo.setPlacar(placar);
	}
}
