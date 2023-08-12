package jokenpo;

public class Jogador {
	private String nome;
	private int vitorias;
	private Jogada jogadaAtual;
	

	public Jogador(String nome) {
		vitorias = 0;
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getVitorias() {
		return vitorias;
	}
	
	public void setVitorias(int vitorias) {
		this.vitorias = vitorias;
	}
	
	public Jogada getJogadaAtual() {
		return jogadaAtual;
	}
	
	public void setJogadaAtual(Jogada jogadaAtual) {
		this.jogadaAtual = jogadaAtual;
	}


}
