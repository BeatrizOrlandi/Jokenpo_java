package conectividade.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Classe respons�vel por receber e enviar mensagens entre Cliente e o Servidor
 */
public class RequestHandler extends Thread {
	private Socket socket;
	private Client cliente;
	private BufferedReader in;
	private PrintWriter out;

	RequestHandler(Socket socket, Client cliente) {
		this.socket = socket;
		this.cliente = cliente;
		this.setName("Client Request Handler" + socket.getInetAddress());
		
		/**
		 * in - Input de mensagens do cliente
		 * out - Output de mensasens para o cliente
		 */
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		try {
			/**
			 * Loop que fica constantemente verificando novas mensagens do Servidor
			 */
			boolean stop = false;
			do {
				String[] line = in.readLine().split(":");
				String flag = line[0];
				String value = line[1];
				
				if("INICIAR".equals(flag)) {
					cliente.startLobby();
				}
				
				if("ADVERSARIOJOGOU".equals(flag)) {
					cliente.setAdversarioJogou(value);
				}
				
				if("PLACAR".equals(flag)) {
					String[] placar = value.split("-");
					cliente.setLobbyPlacar(placar);

				}
				
				if("PLACARFINAL".equals(flag)) {
					String[] placar = value.split("-");
					cliente.setFimJogoPlacar(placar);
				}
				
				if("JOGADAS".equals(flag)) {
					String[] jogadas = value.split("-");
					cliente.setLobbyJogadas(jogadas);
					
				}
				
				if("PROX".equals(flag)) {
					cliente.lobbyNextRound();	
				}

				if("VENCEDOR".equals(flag)) {
					cliente.setLobbyVencedorRound(value);
				}
				
				if("VENCEDORFINAL".equals(flag)) {
					cliente.showFimJogo(value);		
				}
				
				if("STOP".equals(flag)) {
					sendToServer("Encerrando Conexão");
					stop = true;
				}
				
			} while (!stop);
			
			in.close();
			out.close();
			socket.close();
			this.interrupt();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param message - Mensagem a ser enviada para o servidor
	 */
	public void sendToServer(String message) {
		out.println(message);
		out.flush();
	}

	/**
	 * @param message - Mensagem a ser enviada para o server
	 * @param getResponse - Indica se o Cliente deve esperar por uma resposta do server
	 * @return String - resposta do Servidor
	 */
	public String sendToServer(String message, boolean getResponse) {
		sendToServer(message);
		
		if(getResponse) {
			try {
				String response = (String) in.readLine();
	
				return response;
			} catch (Exception e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
		return null;
	}
}