package jokenpo;

public class Jogo {
	private Jogador jogador1, jogador2;
	private int roundAtual = 1;
	
	
	public Jogo() {}
	
	public Jogo(String nickJogador1, String nickJogador2) {
		jogador1 = new Jogador(nickJogador1);
		jogador2 = new Jogador(nickJogador2);
	};
	
	/**
	 * 
	 * @param jogada1 - jogada realizada pelo jogador 1
	 * @param jogada2 - jogada realizada pelo jogador 2
	 * @return String - nome do jogador vencedor.
	 */
	public String fazJogada() {
		if(jogador1.getJogadaAtual().ganhaDe(jogador2.getJogadaAtual())) {
			roundAtual = getRoundAtual() + 1;
			jogador1.setVitorias(jogador1.getVitorias()+1);
			return jogador1.getNome();
		}

		if(jogador2.getJogadaAtual().ganhaDe(jogador1.getJogadaAtual())) {
			roundAtual = getRoundAtual() + 1;
			jogador2.setVitorias(jogador2.getVitorias()+1);
			return jogador2.getNome();
		}
		
		/**
		 * Empate
		 */
		roundAtual = getRoundAtual() + 1;
		return null;
	}
	
	public boolean armazenaJogada(String nome, String nomeJogada) {
		if(nome.equals(jogador1.getNome())) {
			jogador1.setJogadaAtual(Jogada.getJogada(nomeJogada));
		}
		else if(nome.equals(jogador2.getNome())) {
			jogador2.setJogadaAtual(Jogada.getJogada(nomeJogada));
		}
		
		/**
		 * Se ambas as jogadas estiverem preenchidas, retorna "true" para q possa
		 * ser verificado o vencedor 
		 */
		if(jogador1.getJogadaAtual() != null && jogador2.getJogadaAtual() != null) {
			return true;
		}
		return false;
	}
	
	public String verificaFim(){
		if(roundAtual > 5) {
			if(jogador1.getVitorias() > jogador2.getVitorias()) {
				return jogador1.getNome();
			}else if(jogador1.getVitorias() < jogador2.getVitorias()) {
				return jogador2.getNome();
			}else {
				return "Empate";
			}
		}
		return null;
	}

	public int getRoundAtual() {
		return roundAtual;
	}
	
	public Jogador getJogador1() {
		return jogador1;
	}

	public void setJogador1(Jogador jogador1) {
		this.jogador1 = jogador1;
	}

	public Jogador getJogador2() {
		return jogador2;
	}

	public void setJogador2(Jogador jogador2) {
		this.jogador2 = jogador2;
	}
}
